import java.util.Scanner;

public class Game {
	
	Board board;
	boolean running;
	boolean ingame;
	ActionHandler[] actions;
	
	String[] args = null;
	
	void start() {
		ingame = true;
		board = new Board();
		System.out.println("Board initialized.");
	}
	void printBoard() {
		if(!ingame) {
			System.out.println("No game running.");
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
			new ActionHandler("moves", () -> printMoves())
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
