package jagger.Chess;
import java.util.ArrayList;

public class Board {
	private Piece[] pieces;
	
	private int pos(int x, int y) {
		return y * 8 + x;
	}
	
	private int pos(String boardPosition) {
		if(!validPos(boardPosition)) return -1;
		String letters = "abcdefgh";
		char file = boardPosition.charAt(0);
		int x = letters.indexOf(file);
		int y = (boardPosition.charAt(1) - '1');
		
		return pos(x, y);
	}
	
	public boolean validPos(String pos) {
		if(pos.length() != 2) return false;
		pos = pos.toLowerCase();
		if(!Character.isAlphabetic(pos.charAt(0)) || !Character.isDigit(pos.charAt(1))) return false;
		if(pos.charAt(0) < 'a' || pos.charAt(0) > 'h') return false;
		if(pos.charAt(1) < '1' || pos.charAt(1) > '8') return false;
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
		updatePossibleMoves();
	}
	
	public Board(String fenPosition) {
		pieces = new Piece[64];
		String[] fenElements = fenPosition.split(" ");
		if(fenElements.length != 6) {
			return;
		}
		String[] ranks = fenElements[0].split("/");
		
		String pstr = "pbnrqk";
		for(int y=7;y>=0;y--) {
			String rank = ranks[7-y];
			int x = 0;
			for(int i=0;i<rank.length();i++) {
				char c = rank.charAt(i);
				if(pstr.indexOf(Character.toLowerCase(c)) != -1) {
					pieces[pos(x,y)] = new Piece(c);
					if(pieces[pos(x,y)].getType() == PieceType.Pawn) {
						if(pieces[pos(x,y)].getColor() == Color.White && y != 1) pieces[pos(x,y)].setMoved(true);
						if(pieces[pos(x,y)].getColor() == Color.Black && y != 6) pieces[pos(x,y)].setMoved(true);
					}
					x++;
				}
				else if(c >= '1' && c <= '8') {
					x += (c - '0');
				}
			}
		}
		String castles = fenElements[2];
		if(castles.equals("-")) {
			for(int i=0;i<pieces.length;i++) {
				if(pieces[i] == null) continue;
				if(pieces[i].getType() == PieceType.Rook) pieces[i].setMoved(true);
			}
		}
		else {
			if(!castles.contains("K")) pieces[pos(7,0)].setMoved(true);
			if(!castles.contains("k")) pieces[pos(7,7)].setMoved(true);
			if(!castles.contains("Q")) pieces[pos(0,0)].setMoved(true);
			if(!castles.contains("q")) pieces[pos(0,7)].setMoved(true);
		}
		
		updatePossibleMoves();
	}
	
	public ArrayList<Piece> getColoredPieces(Color color) {
		ArrayList<Piece> p = new ArrayList<Piece>();
		for(int i=0;i<pieces.length;i++) {
			if(pieces[i] != null && pieces[i].getColor() == color) p.add(pieces[i]);
		}
		return p;
	}
	
	// TODO: Add En Passant
	void addPawnMoves(ArrayList<Integer> list, int x, int y) {
		Piece p = pieceOn(x,y);
		boolean blocked = false;
		
		if(y >= 7 || y <= 0) return;
		
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
		Piece bishop = pieceOn(x, y);
		// Up Left
		for(int ny=y+1;ny<8;ny++) {
			int nx = x - (ny - y);
			if(nx < 0) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != bishop.getColor()) list.add(pos(nx, ny));
				break;
			}
			list.add(pos(nx,ny));
		}
		// Up Right
		for(int ny=y+1;ny<8;ny++) {
			int nx = x + (ny - y);
			if(nx >= 8) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != bishop.getColor()) list.add(pos(nx, ny));
				break;
			}
			list.add(pos(nx,ny));
		}
		// Down Left
		for(int ny=y-1;ny>=0;ny--) {
			int nx = x - (y - ny);
			if(nx < 0) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != bishop.getColor()) list.add(pos(nx, ny));
				break;
			}
			list.add(pos(nx,ny));
		}
		// Down Right
		for(int ny=y-1;ny>=0;ny--) {
			int nx = x + (y - ny);
			if(nx >= 8) break;
			Piece p = pieceOn(nx, ny);
			if(p != null) {
				if(p.getColor() != bishop.getColor()) list.add(pos(nx, ny));
				break;
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
		if(pieceOn(x,y).hasMoved()) return;
		// Kingside Castling
		if(pieceOn(7,y) != null && pieceOn(7,y).getType() == PieceType.Rook && !pieceOn(7,y).hasMoved()) {
			if(pieceOn(6,y) == null && pieceOn(5,y) == null) 
				list.add(pos(6,y));
		}
		// Queenside Castling
		if(pieceOn(0,y) != null && pieceOn(0,y).getType() == PieceType.Rook && !pieceOn(0,y).hasMoved()) {
			if(pieceOn(3,y) == null && pieceOn(2,y) == null && pieceOn(1,y) == null) 
				list.add(pos(2,y));
		}
	}
	
	private boolean simulateMove(int src, int dest) {
		// I get the color of the piece
		Color c = pieces[src].getColor();
		// Since this function returns whether this move ends in check,
		boolean check = false;
		boolean movedBefore = pieces[src].hasMoved();
		// we need to know the position of our king.
		int kingPos = -1;
		// For undoing the move later, we need to know what was on the destination square.
		Piece pdest = pieces[dest];
		
		// We need to get all the opponent's pieces to check their next possible moves
		ArrayList<Integer> op = new ArrayList<Integer>();
		
		// This one is for saving where each piece on the board CURRENTLY is.
		ArrayList<Integer> positions = new ArrayList<Integer>();
		// Now, we loop through the squares of the board
		for(int i=0;i<pieces.length;i++) {
			if(pieces[i] != null) {
				// If this square has a piece, save it and save it to op if it's an opponent piece
				positions.add(i);
				if(pieces[i].getColor() != c) op.add(i);
				// Getting our king position with this one
				if(pieces[i].getColor() == c && pieces[i].getType() == PieceType.King) kingPos = i;
			}
		}
		// Now this one stores the state of each piece on the board
		Object state[] = new Object[positions.size()];
		for(int i=0;i<state.length;i++) {
			state[i] = pieces[positions.get(i)].possibleMoves.clone();
		}
		
		// This is the move we're simulating, putting the piece from src to dest
		pieces[dest] = pieces[src];
		pieces[src] = null;
		if(pieces[dest].getType() == PieceType.King) kingPos = dest;
		pieces[dest].setMoved(true);
		//Castling
		int difference = dest - src;
		int rook = -1;
		int y = dest / 8;
		if(pieces[dest].getType() == PieceType.King && (difference == 2 || difference == -2)) {
			// Kingside
			if(difference > 0) {
				rook = pos(7, y);
				pieces[pos(5,y)] = pieces[rook];
				pieces[rook] = null;
			}
			// Queenside
			else {
				rook = pos(0, y);
				pieces[pos(3,y)] = pieces[rook];
				pieces[rook] = null;
			}
		}
		// fillMoves() just fills each piece's legal move list with their moves disregarding checks
		fillMoves();
		
		// Here we see if the move we just made ends in check for us. If so, it's an illegal move.
		for(int pos : op) {
			Piece p = pieces[pos];
			for(int i=0;i<p.possibleMoves.size();i++) {
				if(p.possibleMoves.get(i) == kingPos) check = true;
			}
		}
		
		// Since this is a simulation, we gotta undo the move.
		pieces[dest].setMoved(movedBefore);
		pieces[src] = pieces[dest];
		pieces[dest] = pdest;
		// Castling
		// Kingside
		if(rook == pos(7, y)) {
			pieces[rook] = pieces[pos(5, y)];
			pieces[pos(5,y)] = null;
		}
		// Queenside
		else if(rook == pos(0, y)) {
			pieces[rook] = pieces[pos(3, y)];
			pieces[pos(3,y)] = null;
		}
		// This loop resets the state of each piece, INCLUDING checks for ending in check.
		for(int i=0;i<positions.size();i++) {
			pieces[positions.get(i)].possibleMoves = (ArrayList<Integer>)state[i];
		}
		
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
		// We fill the move of each piece disregarding checks
		fillMoves();
		for(int i=0;i<pieces.length;i++) {
			Piece p = pieces[i];
			if(p == null) continue;
			// We clone the ArrayList because we're modifying it
			// by removing the moves that would end with you being in check.
			ArrayList<Integer> moves = (ArrayList<Integer>)p.possibleMoves.clone();
			for(int m=0;m<moves.size();m++) {
				int move = moves.get(m);
				if(simulateMove(i, move)) {
					p.possibleMoves.remove(Integer.valueOf(move));
				}
			}
			// After all of this, it should be ensured that all moves left are legal.
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
	private boolean hasNoMoves(Color color) {
		boolean moveless = true;
		ArrayList<Piece> piecePos = new ArrayList<Piece>();
		for(int i=0;i<pieces.length;i++) {
			Piece p = pieces[i];
			if(p == null) continue;
			if(p.getColor() == color) piecePos.add(p);
		}
		
		for(Piece p : piecePos) {
			if(p.possibleMoves.size() > 0) moveless = false;
		}
		
		return moveless;
	}
	
	public boolean inStalemate(Color color) {
		return !inCheck(color) && hasNoMoves(color);
	}
	
	public boolean inCheckmate(Color color) {
		return inCheck(color) && hasNoMoves(color);
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
		// Castling
		int difference = pos(destination) - pos(piecePosition);
		if(p.getType() == PieceType.King && (difference == 2 || difference == -2)) {
			int y = pos(destination) / 8;
			// Kingside
			if(difference > 0) {
				pieces[pos(5,y)] = pieces[pos(7,y)];
				pieces[pos(5,y)].setMoved(true);
				pieces[pos(7,y)] = null;
				System.out.printf("%s castles Kingside.\n", p.getColor().toString());
			}
			// Queenside
			else {
				pieces[pos(3,y)] = pieces[pos(0,y)];
				pieces[pos(3,y)].setMoved(true);
				pieces[pos(0,y)] = null;
				System.out.printf("%s castles Queenside.\n", p.getColor().toString());
			}
			updatePossibleMoves();
			p.setMoved(true);
			return true;
		}
		
		updatePossibleMoves();
		if(dest != null) {
			System.out.printf("%s's %s captures %s's %s on %s!\n", p.getColor().toString(), p.getName(), dest.getColor().toString(), dest.getName(), destination);
		}
		else {
			System.out.printf("%s's %s moves to %s.\n", p.getColor().toString(), p.getName(), destination);
		}
		p.setMoved(true);
		return true;
	}
	
	public String getPosition(Color turn, int turnCounter) {
		String out = "";
		
		int frees = 0;
		for(int y = 7;y >= 0; y--) {
			for(int x=0;x<8;x++) {
				Piece p = pieces[pos(x,y)];
				if(p != null) {
					if(frees != 0) {
						out += frees;
						frees = 0;
					}
					out += p.toString();
					continue;
				}
				frees++;
			}
			if(frees != 0) {
				out += frees;
				frees = 0;
			}
			if(y != 0) out += "/";
		}
		
		if(turn == Color.White) out += " w ";
		else out += " b ";
		
		boolean[] castling = new boolean[] { true, true, true, true };
		String crights = "KQkq";
		int[] crooks = new int[] { pos(7,0), pos(0,0), pos(7,7), pos(0,7) };
		
		Piece wk = pieces[pos(4,0)];
		Piece bk = pieces[pos(4,7)];
		
		if(wk == null || wk.getType() != PieceType.King || wk.getColor() != Color.White || wk.hasMoved()) {
			castling[0] = false;
			castling[1] = false;
		}
		if(bk == null || bk.getType() != PieceType.King || bk.getColor() != Color.Black || bk.hasMoved()) {
			castling[2] = false;
			castling[3] = false;
		}
		for(int i=0;i<4;i++) {
			Color c = i > 1 ? Color.Black : Color.White;
			Piece p = pieces[crooks[i]];
			if(p == null || p.getType() != PieceType.Rook || p.getColor() != c || p.hasMoved()) castling[i] = false;
			
			if(castling[i]) {
				out += crights.charAt(i);
			}
		}
		if(!castling[0] && !castling[1] && !castling[2] && !castling[3]) out += "-";
		
		// TODO: Add En Passant
		// This is just a placeholder
		out += " - ";
		
		// TODO: Add 50-move draw
		// This is just a placeholder
		out += "0 ";
		
		out += Integer.toString(turnCounter / 2 + 1);
		
		return out;
	}
	
	public String toString(boolean reverse) {
		String split = " +---+---+---+---+---+---+---+---+";
		String board = "";
		board += split + "\n";
		if(reverse) {
			for(int y=0;y<8;y++) {
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
		}
		else {
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
		}
			
		board += "   a   b   c   d   e   f   g   h\n";
		return board;
	}
	public String toString(String piecePosition, boolean reverse) {
		if(!validPos(piecePosition)) return toString(reverse);
		if(pieceOn(piecePosition) == null) return toString(reverse);
		
		Piece p = pieceOn(piecePosition);
		
		String split = " +---+---+---+---+---+---+---+---+";
		String board = "";
		board += split + "\n";
		if(reverse) {
			for(int y=0;y<8;y++) {
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
		}
		else {
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
		}
		
		board += "   a   b   c   d   e   f   g   h\n";
		return board;
	}
	
	public static String coord(int x, int y) {
		String files = "abcdefgh";
		return files.charAt(x) + Integer.toString(y+1);
	}
}
