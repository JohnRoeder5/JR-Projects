import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReviewAnalysisDriver {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		String filename = in.nextLine(); 
		SentimentsCalculator sent = new SentimentsCalculator(filename);
		while(true) {
			System.out.println("enter the review");
			String word = in.nextLine();
			if(word.equalsIgnoreCase("quit")) {
				break;
			}
			//TO DO determine if word is positive, negative, or neutral
			//based on the user provided threshold
			int phraseResult = sent.sentimentScore(word);
			if (phraseResult > 0) {
				System.out.println( phraseResult+ " phrase is positive");
			}
			else if (phraseResult == 0){
				System.out.println(phraseResult+" phrase is neutral");
			}
			else {
				System.out.println(phraseResult+" phrase is negative");
			}
		}
	}
}
