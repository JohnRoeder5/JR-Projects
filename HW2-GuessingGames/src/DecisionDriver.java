import java.util.List;
import java.util.Scanner;
/**
 * DO NOT CHANGE
 * 
 * This class plays a guessing game based on the user provided file
 * 
 * @author Catie Baker
 *
 */
public class DecisionDriver {
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);


		System.out.println("What is the filename that has the data?");
		String filename = in.nextLine().trim();
		DecisionMaker chooser = new DecisionMaker(filename);
		boolean playAgain = true;
		while(playAgain) {
			while(chooser.hasMoreQuestions() && chooser.multiplePossibilitiesRemain()) {
				String nextQ = chooser.getNextQuestion();
				System.out.println(nextQ);
				String ans = in.nextLine().toLowerCase();
				if(ans.startsWith("y")) {
					chooser.pruneOptions(nextQ, true);
				}
				else {
					chooser.pruneOptions(nextQ, false);
				}
			}
			List<String> poss = chooser.getRemainingOptions();
			System.out.println("Here are the remaining options:");
			for(String p: poss) {
				System.out.println(p);
			}
			System.out.println("Do you want to choose again?");
			String ans = in.nextLine().toLowerCase();
			if(ans.startsWith("n")) {
				playAgain = false;
			}
			chooser.reset();
		}
	}
}
