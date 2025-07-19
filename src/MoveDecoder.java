
public class MoveDecoder {

	private Board b;
	
	private int parseSquare(String pos) {
		if(pos.length() != 2 || !b.validPos(pos)) return -1;
		return (pos.charAt(1) - '1') * 8 + (pos.charAt(0) - 'a'); 
	}
	
	private String parseIndex(int index) {
		int x = index % 8;
		int y = index / 8;
		return String.format("%c%d", 'a' + x, y+1);
	}
	
	public MoveDecoder(Board board) {
		b = board;
	}
	
	public String[] decode(Color turn, String input) {
		String[] out = new String[2];
		// Pawn move
		if(input.length() == 2 && !b.validPos(input)) {
			System.out.println("Not a legal Pawn move.");
			return null;
		}
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
			System.out.printf("Multiple of your %ss can reach that square. Specify which %s.", type.toString(), type.toString());
			return null;
		}
		else {
			out[0] = parseIndex(idxMover);
			out[1] = destStr;
			return out;
		}
	}

}
