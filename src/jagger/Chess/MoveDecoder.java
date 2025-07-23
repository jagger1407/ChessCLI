package jagger.Chess;
import java.util.ArrayList;

public class MoveDecoder {

	private Board b;
	
	public int parseSquare(String pos) {
		if(pos.length() != 2 || !b.validPos(pos)) return -1;
		return (pos.charAt(1) - '1') * 8 + (pos.charAt(0) - 'a'); 
	}
	
	public String parseIndex(int index) {
		int x = index % 8;
		int y = index / 8;
		return String.format("%c%d", 'a' + x, y+1);
	}
	
	public MoveDecoder(Board board) {
		b = board;
	}
	public ArrayList<Piece> getPieces(Color side) {
		return b.getColoredPieces(side);
	}
	public String[] decode(Color turn, String input) {
		String[] out = new String[2];
		// Castling
		if(input.equals("O-O") || input.equals("o-o")) {
			int kingPos[] = { 60, 4 };
			int castlePos[] = { 62, 6 };
			Piece p = b.pieceOn(kingPos[turn.ordinal()]);
			if(p != null && p.getType() == PieceType.King && p.getColor() == turn && !p.hasMoved()){
				if(p.possibleMoves.contains(castlePos[turn.ordinal()])) {
					out[0] = parseIndex(kingPos[turn.ordinal()]);
					out[1] = parseIndex(castlePos[turn.ordinal()]);
					return out;
				}
				else {
					System.out.println("You currently cannot castle kingside.");
					return null;
				}
			}
			else {
				System.out.println("Your King has moved, forfeiting all castling rights.");
				return null;
			}
		}
		if(input.equals("O-O-O") || input.equals("o-o-o")) {
			int kingPos[] = { 60, 4 };
			int castlePos[] = { 58, 2 };
			Piece p = b.pieceOn(kingPos[turn.ordinal()]);
			if(p != null && p.getType() == PieceType.King && p.getColor() == turn && !p.hasMoved()){
				if(p.possibleMoves.contains(castlePos[turn.ordinal()])) {
					out[0] = parseIndex(kingPos[turn.ordinal()]);
					out[1] = parseIndex(castlePos[turn.ordinal()]);
					return out;
				}
				else {
					System.out.println("You currently cannot castle queenside.");
					return null;
				}
			}
			else {
				System.out.println("Your King has moved, forfeiting all castling rights.");
				return null;
			}
		}
		// Pawn move
		if(input.length() == 2 && b.validPos(input)) {
			int index = parseSquare(input);
			if(index != -1) {
				out[1] = input;
				int x = input.charAt(0) - 'a';
				int y = input.charAt(1) - '1';
				for(int i=0;i<8;i++) {
					Piece p = b.pieceOn(x, i);
					if(p != null && p.getType() == PieceType.Pawn && p.getColor() == turn) {
						if(p.possibleMoves.contains(index)) {
							out[0] = String.format("%c%d", 'a' + x, i+1);
							return out;
						}
					}
				}
				System.out.println("No pawn reaches this square.");
				return null;
			}
		}
		else if(input.length() == 4 && input.contains("x")) {
			int x = input.charAt(0) - 'a';
			String destStr = input.substring(2);
			if(x < 0 || x >= 8) {
				System.out.printf("There is no '%c' file.\n", input.charAt(0));
				return null;
			}
			if(!b.validPos(input.substring(2))) {
				System.out.printf("%s is not a valid square.\n", destStr);
				return null;
			}
			for(int y=0;y<8;y++) {
				Piece p = b.pieceOn(x, y);
				if(p == null || p.getColor() != turn || p.getType() != PieceType.Pawn) continue;
				if(p.possibleMoves.contains(parseSquare(destStr))) {
					out[0] = String.format("%c%d", input.charAt(0), y+1);
					out[1] = destStr;
					return out;
				}
			}
		}
		// Other moves
		String pieces = "PBNRQK";
		int t = pieces.indexOf(input.charAt(0));
		if(t == -1) {
			System.out.printf("No piece called '%c'.\n", input.charAt(0));
			return null;
		}
		PieceType type = PieceType.values()[t];
		String destStr = "";
		if(input.endsWith("+") || input.endsWith("#"))
			destStr += input.substring(input.length() - 3, input.length() - 1);
		else
			destStr += input.substring(input.length() - 2);
		int dest = parseSquare(destStr);
		int legalMovers = 0;
		int idxMover = -1;
		for(int i=0;i<64;i++) {
			if(b.pieceOn(i) == null || b.pieceOn(i).getColor() != turn || b.pieceOn(i).getType() != type) continue;
			if(b.pieceOn(i).possibleMoves.contains(dest)) {
				legalMovers++;
				idxMover = i;
			}
		}
		if(legalMovers == 0) {
			System.out.printf("None of your %ss can reach that square.\n", type.toString());
			return null;
		}
		else if(legalMovers > 1) {
			System.out.printf("Multiple of your %ss can reach that square. Specify which %s.\n", type.toString(), type.toString());
			return null;
		}
		else {
			out[0] = parseIndex(idxMover);
			out[1] = destStr;
			return out;
		}
	}
	public String encode(String src, String dest) {
		String out = "";
		
		if(!b.validPos(src)) {
			System.out.printf("%s is not a valid square.\n", src);
			return null;
		}
		else if(!b.validPos(dest)) {
			System.out.printf("%s is not a valid square.\n", dest);
			return null;
		}
		
		Piece sp = b.pieceOn(src);
		Piece dp = b.pieceOn(dest);
		if(sp.getType() == PieceType.Pawn) {
			if(dp != null) {
				out = String.format("%cx%s", src.charAt(0), dest);
			}
			else {
				out = dest;
			}
		}
		else {
			out += sp.toString().toUpperCase();
			if(dp != null) out += "x";
			out += dest;
		}
		
		return out;
	}
}
