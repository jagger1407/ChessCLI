package bots;

import java.util.ArrayList;

import jagger.Chess.*;

public class RandomBot extends Bot {

	public RandomBot(Color color, Board board) {
		super(color, board);
	}
	
	@Override
	public String getNextMove() {
		String move = "move ";
		ArrayList<Integer> plist = new ArrayList<Integer>();
		for(int i=0;i<64;i++) {
			Piece p = board.pieceOn(i);
			if(p != null && p.getColor() == color && p.possibleMoves.size() > 0) plist.add(i);
		}
		
		int idxPiece = (int)(Math.random() * 100) % plist.size();
		Piece p = board.pieceOn(plist.get(idxPiece));
		move += dc.parseIndex(plist.get(idxPiece));
		
		move += " ";
		
		int idxMove = (int)(Math.random() * 100) % p.possibleMoves.size();
		move += dc.parseIndex(p.possibleMoves.get(idxMove));
		
		return move;
	}

}
