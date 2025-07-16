import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		Board b = null;
		Scanner input = new Scanner(System.in);
		
		boolean running = true;
		boolean ingame = false;
		while(running) {
			System.out.print("> ");
			String inStr = input.nextLine();
			if(inStr.equals("start")) {
				ingame = true;
				b = new Board();
				System.out.println("Board initialized.");
			}
			else if(inStr.equals("board")) {
				if(!ingame) {
					System.out.println("No game running.");
					continue;
				}
				System.out.println(b.toString());
			}
			else if(inStr.equals("exit") || inStr.equals("quit") || 
					inStr.equals("end") || inStr.equals("stop")) {
				running = false;
			}
		}
		
		input.close();
	}

}
