package jagger.Chess.bots;
import java.util.ArrayList;

import jagger.Chess.Board;
import jagger.Chess.Color;
import jagger.Chess.MoveDecoder;

public abstract class Bot {
	protected Color color;
	protected Board board;
	
	MoveDecoder dc;
	
	public Bot(Color color, Board board) {
		this.color = color;
		this.board = board;
		dc = new MoveDecoder(board);
	}
	
	abstract public String getNextMove();
}
