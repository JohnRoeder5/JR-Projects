import java.util.Scanner;

public class BattleshipDriver {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		System.out.println("What file has the Battleship Game to be solved? ");
		String words = scan.nextLine().trim();
		Battleship play = new Battleship(words);
		boolean result = play.solve();
		if (result == true) {
			play.toString(); 
		}
		else {
			System.out.println("the given puzzle has no solution for boat placement");
		}
		
	}
}
