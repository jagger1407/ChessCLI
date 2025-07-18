import java.util.Scanner;

public class Game {
	
	Board board;
	boolean running;
	boolean ingame;
	Color turn;
	int turnCounter = 0;
	ActionHandler[] actions;
	
	String[] args = null;
	
	void start() {
		ingame = true;
		board = new Board();
		System.out.println("Board initialized.");
		turn = Color.values()[(turnCounter+1) % Color.values().length];
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
		if(args == null || args.length < 1 || args[0].isEmpty() || args[1].isEmpty())  {
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
		
		turnCounter++;
		turn = Color.values()[(turnCounter+1) % Color.values().length];
		if(board.inCheckmate(turn)) {
			System.out.printf("Checkmate!\nGame ended, %s wins.\n", Color.values()[(turnCounter) % Color.values().length].toString());
			ingame = false;
			return;
		}
		if(board.inCheck(turn)) {
			System.out.println("Check!");
		}
		System.out.printf("Turn %d. %s to move.\n", turnCounter / 2 + 1, turn.toString());
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
		};
	}
	
	public static void main(String[] args) {
		Game g = new Game();
		Scanner input = new Scanner(System.in);
		
		
		while(g.running) {
			System.out.print("> ");
			String inStr = input.nextLine();
			if(inStr.isEmpty()) continue;
			if(inStr.contains(" ")) {
				int argc = inStr.split(" ").length - 1;
				g.args = new String[argc];
				System.arraycopy(inStr.split(" "), 1, g.args, 0, argc);
				inStr = inStr.split(" ")[0];
			}
			boolean found = false;
			for(int i=0;i<g.actions.length;i++) {
				if(g.actions[i].action.equals(inStr)) {
					found = true;
					g.actions[i].run();
				}
			}
			if(!found) {
				System.out.println("Unknown action '" + inStr + "'.");
			}
		}
		
		input.close();
	}

}
