package jagger.Chess;
import java.util.Scanner;

import bots.Bot;
import bots.RandomBot;

public class Game {
	
	Board board;
	boolean running;
	boolean ingame;
	boolean promoting;
	boolean botMatch;
	Piece promotingPiece;
	Color turn;
	Color playerColor = null;
	int turnCounter;
	ActionHandler[] actions;
	
	MoveDecoder dc;
	Bot bot;
	
	String[] args = null;
	
	void start() {
		if(ingame) {
			System.out.println("A game is currently running. Resign if you want to start a new game.");
			return;
		}
		if(args == null || args[0].equals("pvp")) {
			botMatch = false;
		}
		else if(args[0].equals("bot")) {
			botMatch = true;
			String sides[] = { "black", "white" };
			if(args.length < 2 || args[1] == null) {
				System.out.println("If a bot match is started, your color must be selected.");
				System.out.println("Syntax: start bot [white|black]");
				return;
			}
			for(int i=0;i<sides.length;i++) {
				if(args[1].toLowerCase().equals(sides[i])) {
					playerColor = Color.values()[i];
				}
			}
			if(playerColor == null) {
				System.out.printf("'%s' is not a valid option.\n", args[1]);
				return;
			}
		}
		ingame = true;
		promoting = false;
		promotingPiece = null;
		board = new Board();
		System.out.println("Board initialized.");
		turnCounter = 0;
		turn = Color.values()[(turnCounter+1) % Color.values().length];
		dc = new MoveDecoder(board);
		if(playerColor != null) {
			bot = new RandomBot(Color.values()[(turnCounter+2) % Color.values().length], board);
		}
		System.out.printf("Turn %d. %s to move.\n", turnCounter / 2 + 1, turn.toString());
	}
	void printBoard() {
		if(!ingame) {
			System.out.println("No game running.");
			return;
		}
		if(args != null && args.length > 0) {
			showPossibleMoves();
			return;
		}
		System.out.println(board.toString());
	}
	void exit() {
		running = false;
	}
	void printMoves() {
		if(args == null || args.length < 1 || args[0].isEmpty()) return;
		if(!ingame) {
			System.out.println("No game running.");
			return;
		}
		if(!board.validPos(args[0])) {
			System.out.printf("'%s' is not a valid board position.\n", args[0]);
			return;
		}
		Piece p = board.pieceOn(args[0]);
		if(p == null) {
			System.out.println("No piece on that square.");
			return;
		}
		if(p.possibleMoves.isEmpty()) {
			System.out.printf("The %s on %s has no legal moves.\n", p.getName(), args[0]);
			return;
		}
		System.out.printf("The %s on %s has these moves:\n", p.getName(), args[0]);
		for(int i=0;i<p.possibleMoves.size();i++) {
			int move = p.possibleMoves.get(i);
			System.out.print(Board.coord(move % 8, move / 8));
			if(board.pieceOn(move) != null) {
				System.out.print(" captures " + board.pieceOn(move).getName());
			}
			System.out.print("\n");
		}
	}
	void showPossibleMoves() {
		if(args == null || args.length < 1 || args[0].isEmpty()) return;
		if(!ingame) {
			System.out.println("No game running.");
			return;
		}
		if(!board.validPos(args[0])) {
			System.out.printf("'%s' is not a valid board position.\n", args[0]);
			return;
		}
		System.out.println(board.toString(args[0]));
	}
	
	void move() {
		if(args == null || args.length < 2 || args[0].isEmpty() || args[1].isEmpty())  {
			System.out.println("No arguments given.");
			return;
		}
		if(!ingame) {
			System.out.println("No game running.");
			return;
		}
		if(!board.validPos(args[0])) {
			System.out.printf("'%s' is not a valid board position.\n", args[0]);
			return;
		}
		if(!board.validPos(args[1])) {
			System.out.printf("'%s' is not a valid board position.\n", args[1]);
			return;
		}
		if(board.pieceOn(args[0]) == null) {
			System.out.println("There is nothing on this square.");
			return;
		}
		if(board.pieceOn(args[0]).getColor() != turn) {
			System.out.println("This piece is the wrong color.");
			return;
		}
		
		boolean successful = board.movePiece(args[0], args[1]);
		if(!successful) return;
		
		Piece p = board.pieceOn(args[1]);
		if(p.getType() == PieceType.Pawn && (args[1].charAt(1) == '1' || args[1].charAt(1) == '8')) {
			System.out.println("What piece do you want to promote to?");
			promotingPiece = p;
			promoting = true;
			return;
		}
		nextTurn();
	}
	
	void promote() {
		PieceType[] types = PieceType.values();
		for(int i=0;i<types.length;i++) {
			if(args[0].equals("king")) {
				System.out.println("You can't make a second king. Are you stupid?");
				return;
			}
			if(args[0].equals("pawn")) {
				System.out.println("You can't stay a pawn this is a PROMOTION. Are you stupid?");
				return;
			}
			if(args[0].equals(types[i].toString().toLowerCase())) {
				promotingPiece.promote(types[i]);
				promoting = false;
				args = null;
				board.updatePossibleMoves();
				nextTurn();
				return;
			}
		}
		System.out.printf("There is no piece called '%s'.\n", args[0]);
		args = null;
	}
	
	void loadBoard() {
		if(args == null || args.length < 1 || args[0].isEmpty()) return;
		 
		if(args.length != 6) {
			System.out.println("This is not a valid FEN-notation.");
			return;
		}
		if(args[1].equals("w")) {
			if(args[5].equals("1")) turnCounter = -1;
			else turnCounter = (args[5].charAt(0) - '0') * 2 - 3;
		}
		else if(args[1].equals("b")) {
			turnCounter = (args[5].charAt(0) - '0') * 2 - 2;
		}
		else {
			System.out.println("(w)hite or (b)lack, those are the only 2 sides.");
			return;
		}
		ingame = true;
		board = new Board(String.join(" ", args));
		dc = new MoveDecoder(board);
		System.out.println("Board position loaded.");
		nextTurn();
	}
	
	void export() {
		if(board == null) {
			System.out.println("No game has been played yet.");
			return;
		}
		System.out.println("The current FEN-Position of the board is:");
		System.out.println(board.getPosition(turn, turnCounter));
	}
	
	void resign() {
		if(!ingame) {
			System.out.println("No game running.");
			return;
		}
		Color winner = turn;
		Color loser = Color.values()[(turnCounter) % Color.values().length];
		System.out.printf("%s resigns. %s wins!\n", winner, loser);
		ingame = false;
	}
	
	public Game() {
		running = true;
		ingame = false;
		
		actions = new ActionHandler[] {
			new ActionHandler("start", () -> start()),
			new ActionHandler("board", () -> printBoard()),
			new ActionHandler("exit", () -> exit()),
			new ActionHandler("end", () -> exit()),
			new ActionHandler("quit", () -> exit()),
			new ActionHandler("stop", () -> exit()),
			new ActionHandler("moves", () -> printMoves()),
			new ActionHandler("move", () -> move()),
			new ActionHandler("load", () -> loadBoard()),
			new ActionHandler("resign", () -> resign()),
			new ActionHandler("export", () -> export()),
		};
	}
	
	void nextTurn() {
		turnCounter++;
		turn = Color.values()[(turnCounter+1) % Color.values().length];
		if(board.inCheckmate(turn)) {
			System.out.printf("Checkmate!\nGame ended, %s wins.\n", Color.values()[(turnCounter) % Color.values().length].toString());
			ingame = false;
			return;
		}
		if(board.inStalemate(turn)) {
			System.out.printf("%s is in Stalemate.\nGame ends in a draw.\n", turn.toString());
			ingame = false;
			return;
		}
		if(board.inCheck(turn)) {
			System.out.println("Check!");
		}
		System.out.printf("Turn %d. %s to move.\n", turnCounter / 2 + 1, turn.toString());
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		Scanner input = new Scanner(System.in);
		
		
		while(g.running) {
			String inStr;
			if(g.ingame && g.botMatch && g.turn != g.playerColor) {
				inStr = g.bot.getNextMove();
			}
			else {
				System.out.print("> ");
				inStr = input.nextLine();
			}
			if(inStr.isEmpty()) continue;
			if(inStr.contains(" ")) {
				int argc = inStr.split(" ").length - 1;
				g.args = new String[argc];
				System.arraycopy(inStr.split(" "), 1, g.args, 0, argc);
				inStr = inStr.split(" ")[0];
			}
			if(g.promoting) {
				g.args = new String[] { inStr.toLowerCase() };
				g.promote();
				continue;
			}
			boolean found = false;
			for(int i=0;i<g.actions.length;i++) {
				if(g.actions[i].action.equals(inStr)) {
					found = true;
					g.actions[i].run();
					g.args = null;
				}
			}
			if(!found) {
				if(g.ingame) {
					String[] move = g.dc.decode(g.turn, inStr);
					if(move == null) continue;
					g.args = move;
					g.move();
					continue;
				}
				System.out.println("Unknown action '" + inStr + "'.");
			}
		}
		
		input.close();
	}

}
