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
	
	public boolean validPos(String pos) {
		if(pos.length() != 2) return false;
		if(!Character.isAlphabetic(pos.charAt(0)) || !Character.isDigit(pos.charAt(1))) return false;
		return true;
	}
	
	public Piece pieceOn(String position) {
		if(!validPos(position)) return null;
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
		fillMoves();
	}
	
	// TODO: Add En Passant
	void addPawnMoves(ArrayList<Integer> list, int x, int y) {
		Piece p = pieceOn(x,y);
		boolean blocked = false;
		
		if(p.getColor() == Color.White) {
			if(pieceOn(x, y+1) == null) list.add(pos(x,y+1));
			else blocked = true;
			if(!blocked && !p.hasMoved() && pieceOn(x,y+2) == null) list.add(pos(x,y+2));
			if(x > 0 && pieceOn(x-1, y+1) != null && pieceOn(x-1, y+1).getColor() != p.getColor()) list.add(pos(x-1, y+1));
			if(x < 7 && pieceOn(x+1, y+1) != null && pieceOn(x+1, y+1).getColor() != p.getColor()) list.add(pos(x+1, y+1));
		}
		else {
			if(pieceOn(x, y-1) == null) list.add(pos(x,y-1));
			else blocked = true;
			if(!blocked && !p.hasMoved() && pieceOn(x,y-2) == null) list.add(pos(x,y-2));
			if(x > 0 && pieceOn(x-1, y-1) != null && pieceOn(x-1, y-1).getColor() != p.getColor()) list.add(pos(x-1, y-1));
			if(x < 7 && pieceOn(x+1, y-1) != null && pieceOn(x+1, y-1).getColor() != p.getColor()) list.add(pos(x+1, y-1));
		}
	}
	// TODO: Clean up a bit (see addRookMoves)
	void addBishopMoves(ArrayList<Integer> list, int x, int y) {
		// Up Left
		for(int i=x-1;i>=0;i--) {
			int nx = i;
			int ny = y+x-i;
			if(ny >= 8) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != pieceOn(x, y).getColor()) {
					list.add(pos(nx,ny));
				}
				if(p.getType() == PieceType.King) continue;
				else break;
			}
			list.add(pos(nx,ny));
		}
		// Up Right
		for(int i=x+1;i<8;i++) {
			int nx = i;
			int ny = y+i-x;
			if(ny >= 8) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != pieceOn(x, y).getColor()) {
					list.add(pos(nx,ny));
				}
				if(p.getType() == PieceType.King) continue;
				else break;
			}
			list.add(pos(nx,ny));
			
		}
		// Down Left
		for(int i=x-1;i>=0;i--) {
			int nx = i;
			int ny = y-x+i;
			if(ny < 0) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != pieceOn(x, y).getColor()) {
					list.add(pos(nx,ny));
				}
				if(p.getType() == PieceType.King) continue;
				else break;
			}
			list.add(pos(nx,ny));
		}
		// Down Right
		for(int i=x+1;i<8;i++) {
			int nx = i;
			int ny = y-i+x;
			if(ny < 0) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != pieceOn(x, y).getColor()) {
					list.add(pos(nx,ny));
				}
				if(p.getType() == PieceType.King) continue;
				else break;
			}
			list.add(pos(nx,ny));
		}
	}
	// TODO: Clean up a bit (see addRookMoves)
	void addKnightMoves(ArrayList<Integer> list, int x, int y) {
		int nx[] = { x-1, x+1, x-2, x+2 };
		int ny[] = { y+2, y+2, y+1, y+1, y-2, y-2, y-1, y-1 };
		for(int i=0;i<8;i++) {
			if(nx[i%4] >= 0 && nx[i%4] < 8 && ny[i] >= 0 && ny[i] < 8) {
				if(pieceOn(nx[i%4],ny[i]) == null || pieceOn(nx[i%4],ny[i]).getColor() != pieceOn(x,y).getColor()) {
					list.add(pos(nx[i%4],ny[i]));
				}
			}
		}
	}
	void addRookMoves(ArrayList<Integer> list, int x, int y) {
		// Left
		for(int i=x-1;i>=0;i--) {
			if(pieceOn(i,y) != null && pieceOn(i,y).getColor() == pieceOn(x,y).getColor()) break;
			list.add(pos(i,y));
			if(pieceOn(i,y) != null && pieceOn(i,y).getType() != PieceType.King) break;
		}
		// Right
		for(int i=x+1;i<8;i++) {
			if(pieceOn(i,y) != null && pieceOn(i,y).getColor() == pieceOn(x,y).getColor()) break;
			list.add(pos(i,y));
			if(pieceOn(i,y) != null && pieceOn(i,y).getType() != PieceType.King) break;
		}
		// Up
		for(int i=y+1;i<8;i++) {
			if(pieceOn(x,i) != null && pieceOn(x,i).getColor() == pieceOn(x,y).getColor()) break;
			list.add(pos(x,i));
			if(pieceOn(x,i) != null && pieceOn(x,i).getType() != PieceType.King) break;
		}
		// Down
		for(int i=y-1;i>=0;i--) {
			if(pieceOn(x,i) != null && pieceOn(x,i).getColor() == pieceOn(x,y).getColor()) break;
			list.add(pos(x,i));
			if(pieceOn(x,i) != null && pieceOn(x,i).getType() != PieceType.King) break;
		}
	}
	// TODO: Add Castling
	void addKingMoves(ArrayList<Integer> list, int x, int y) {
		for(int cy=y-1;cy<=y+1;cy++) {
			if(cy < 0 || cy >= 8) continue;
			for(int cx=x-1;cx<=x+1;cx++) {
				if(cx < 0 || cx >= 8) continue;
				if(cx == x && cy == y) continue;
				if(pieceOn(cx,cy) != null && pieceOn(cx,cy).getColor() == pieceOn(x,y).getColor()) continue;
				
				if(!list.contains(pos(cx,cy))) list.add(pos(cx,cy));
			}
		}
	}
	
	private boolean simulateMove(int src, int dest) {
		Color c = pieces[src].getColor();
		int kingPos = -1;
		Piece pdest = pieces[dest];
		
		ArrayList<Integer> op = new ArrayList<Integer>();
		
		boolean check = false;
		
		for(int i=0;i<pieces.length;i++) {
			if(pieces[i] != null) {
				if(pieces[i].getColor() != c) op.add(i);
				if(pieces[i].getColor() == c && pieces[i].getType() == PieceType.King) kingPos = i;
			}
		}
		
		if(src == pos("d1") && dest == pos("h5")) {
			System.out.println("test");
		}
		
		pieces[dest] = pieces[src];
		pieces[src] = null;
		fillMoves();
		
		for(int pos : op) {
			Piece p = pieces[pos];
			for(int i=0;i<p.possibleMoves.size();i++) {
				if(p.possibleMoves.get(i) == kingPos) check = true;
			}
		}
		
		pieces[src] = pieces[dest];
		pieces[dest] = pdest;
		fillMoves();
		
		return check;
	}
	
	public void fillMoves() {
		for(int i=0;i<pieces.length;i++) {
			Piece p = pieces[i];
			if(p == null) continue;
			p.possibleMoves.clear();
			int x = i % 8;
			int y = i / 8;
			
			switch(p.getType()) {
			case Pawn:
				addPawnMoves(p.possibleMoves, x, y);
				break;
			case Bishop:
				addBishopMoves(p.possibleMoves, x, y);
				break;
			case Knight:
				addKnightMoves(p.possibleMoves, x, y);
				break;
			case Rook:
				addRookMoves(p.possibleMoves, x, y);
				break;
			case Queen:
				addBishopMoves(p.possibleMoves, x, y);
				addRookMoves(p.possibleMoves, x, y);
				break;
			case King:
				addKingMoves(p.possibleMoves, x, y);
				break;
			default:
				break;
			}
		}
	}
	
	public void updatePossibleMoves() {
		fillMoves();
		for(int i=0;i<pieces.length;i++) {
			Piece p = pieces[i];
			if(p == null) continue;
			for(int move : p.possibleMoves) {
				if(simulateMove(i, move)) {
					p.possibleMoves.remove(Integer.valueOf(move));
				}
			}
		}
	}
	
	public boolean inCheck(Color color) {
		int kingPos = -1;
		ArrayList<Piece> opp = new ArrayList<Piece>();
		
		for(int i=0;i<pieces.length;i++) {
			if(pieces[i] == null) continue;
			if(pieces[i].getColor() != color) {
				opp.add(pieces[i]);
				continue;
			}
			if(pieces[i].getType() == PieceType.King && pieces[i].getColor() == color) kingPos = i;
		}
		
		for(Piece p : opp) {
			for(int i=0;i<p.possibleMoves.size();i++) {
				int move = p.possibleMoves.get(i);
				if(move == kingPos) return true;
			}
		}
		return false;
	}
	public boolean inCheckmate(Color color) {
		boolean checkmate = true;
		ArrayList<Piece> piecePos = new ArrayList<Piece>();
		for(int i=0;i<pieces.length;i++) {
			Piece p = pieces[i];
			if(p == null) continue;
			if(p.getColor() == color) piecePos.add(p);
		}
		
		for(Piece p : piecePos) {
			if(p.possibleMoves.size() > 0) checkmate = false;
		}
		
		return checkmate;
	}
	
	public boolean movePiece(String piecePosition, String destination) {
		Piece p = pieceOn(piecePosition);
		if(p == null) return false;
		if(!validPos(destination)) return false;
		
		boolean checked = inCheck(p.getColor());
		
		if(!p.possibleMoves.contains(pos(destination))) {
			System.out.println("This is not a legal move.");
			return false;
		}
		Piece dest = pieceOn(destination);
		
		pieces[pos(destination)] = pieces[pos(piecePosition)];
		pieces[pos(piecePosition)] = null;
		fillMoves();
		
		if(checked && inCheck(p.getColor())) {
			System.out.println("This move is not legal as it doesn't stop the check.");
			pieces[pos(piecePosition)] = pieces[pos(destination)];
			pieces[pos(destination)] = dest;
			fillMoves();
			return false;
		}
		if(dest != null) {
			System.out.printf("%s's %s captures %s's %s on %s!\n", p.getColor().toString(), p.getName(), dest.getColor().toString(), dest.getName(), destination);
		}
		else {
			System.out.printf("%s's %s moves to %s.\n", p.getColor().toString(), p.getName(), destination);
		}
		p.setMoved(true);
		return true;
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
	public String toString(String piecePosition) {
		if(!validPos(piecePosition)) return toString();
		if(pieceOn(piecePosition) == null) return toString();
		
		Piece p = pieceOn(piecePosition);
		
		String split = " +---+---+---+---+---+---+---+---+";
		String board = "";
		board += split + "\n";
		for(int y=7;y>=0;y--) {
			String rank = String.format("%d|", y+1);
			for(int x=0;x<8;x++) {
				// 1|
				String square = pieceOn(x,y) == null ? " " : pieceOn(x,y).toString();
				if(p.possibleMoves.contains(pos(x,y))) {
					rank += String.format(">%s<|", square);
				}
				else {
					rank += String.format(" %s |", square);
				}
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
