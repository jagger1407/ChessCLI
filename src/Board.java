import java.util.ArrayList;

public class Board {
	private Piece[] pieces;
	
	private int pos(int x, int y) {
		return y * 8 + x;
	}
	
	private int pos(String boardPosition) {
		String letters = "abcdefgh";
		char file = boardPosition.charAt(0);
		int x = letters.indexOf(file);
		int y = Integer.valueOf(boardPosition.substring(1)) - 1;
		
		return pos(x, y);
	}
	
	public Piece pieceOn(String position) {
		return pieces[pos(position)];
	}
	public Piece pieceOn(int x, int y) {
		return pieces[pos(x, y)];
	}
	public Piece pieceOn(int index) {
		return pieces[index];
	}
	
	public Board() {
		pieces = new Piece[64];
		String pstr = "RNB";
		pieces[pos("d1")] = new Piece(PieceType.Queen, Color.White);
		pieces[pos("e1")] = new Piece(PieceType.King, Color.White);
		pieces[pos("d8")] = new Piece(PieceType.Queen, Color.Black);
		pieces[pos("e8")] = new Piece(PieceType.King, Color.Black);
		for(int i=0;i<3;i++) {
			pieces[pos(i, 0)] = new Piece(pstr.charAt(i));
			pieces[pos(7-i, 0)] = new Piece(pstr.charAt(i));
			pieces[pos(i, 7)] = new Piece(pstr.toLowerCase().charAt(i));
			pieces[pos(7-i, 7)] = new Piece(pstr.toLowerCase().charAt(i));
		}
		for(int i=0;i<8;i++) {
			pieces[pos(i, 1)] = new Piece(PieceType.Pawn, Color.White);
			pieces[pos(i, 6)] = new Piece(PieceType.Pawn, Color.Black);
		}
		setPossibleMoves();
	}
	
	private ArrayList<Integer> getPossibleMoves(Piece p, int x, int y) {
		ArrayList<Integer> moves = new ArrayList<Integer>();
		switch(p.getType()) {
		case Pawn:
			boolean blocked = false;
			if(p.getColor() == Color.White) {
				if(y == 7) return moves;
				if(pieces[pos(x, y+1)] == null) moves.add(pos(x, y+1));
				else blocked = true;
				if(!p.hasMoved() && !blocked && pieces[pos(x, y+2)] == null) moves.add(pos(x,y+2));
				if(x > 0 && pieces[pos(x-1, y+1)] != null && pieces[pos(x-1, y+1)].getColor() != p.getColor()) 
					moves.add(pos(x-1, y+1));
				if(x < 7 && pieces[pos(x+1, y+1)] != null && pieces[pos(x+1, y+1)].getColor() != p.getColor()) 
					moves.add(pos(x+1, y+1));
			}
			break;
		case Bishop:
			break;
		case King:
			break;
		case Knight:
			break;
		case Queen:
			break;
		case Rook:
			break;
		default:
			return null;			
		}
		return moves;
	}
	
	// TODO: finish this
	public void setPossibleMoves() {
		for(int i=0;i<pieces.length;i++) {
			if(pieces[i] == null) continue;
			int x = i % 8;
			int y = i / 8;
			pieces[i].possibleMoves = getPossibleMoves(pieces[i], x, y);
		}
	}
	
	public String toString() {
		String split = " +---+---+---+---+---+---+---+---+";
		String board = "";
		board += split + "\n";
		for(int y=7;y>=0;y--) {
			String rank = String.format("%d| ", y+1);
			for(int x=0;x<8;x++) {
				if(pieces[y * 8 + x] != null) {
					rank += pieces[y * 8 + x].toString() + " | ";
				}
				else rank += "  | ";
			}
			board += rank + "\n";
			board += split + "\n";
		}
		board += "   a   b   c   d   e   f   g   h\n";
		return board;
	}
	
	public static String coord(int x, int y) {
		String files = "abcdefgh";
		return files.charAt(x) + Integer.toString(y+1);
	}
}
