import java.util.Scanner;

/**
 * DO NOT MODIFY THIS CLASS!!!
 * 
 * A class to determine how many coins a robot can pick up for the specified 
 * grid given different starting points
 * 
 * 
 * @author Catie Baker
 *
 */
public class CoinCollectorDriver {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("What is the name of the file that has the grid?");
		String filename = in.next();
		CoinCollector coins = new CoinCollector(filename);
		while(true) {
			System.out.println("What is your starting row?");
			int row = in.nextInt();
			System.out.println("What is your starting column?");
			int col = in.nextInt();
		
			long startTime = System.currentTimeMillis();
			int numCoins = coins.findMaxCoinsTopDown(row, col);
			long endTime = System.currentTimeMillis();
			System.out.println("The top down approach took "+(endTime-startTime)+" ms");
			System.out.println("The robot picked up "+numCoins+" coins\n");
			
			startTime = System.currentTimeMillis();
			numCoins = coins.findMaxCoinsBottomUp(row, col);
			endTime = System.currentTimeMillis();
			System.out.println("The bottom-up approach took "+(endTime-startTime)+" ms");
			System.out.println("The robot picked up "+numCoins+" coins");

			
			
			System.out.println("Do you want to run it with another starting location? (yes or no)");
			String ans = in.next();
			if(ans.toLowerCase().charAt(0)=='n') {
				break;
			}
		}
		

	}
}
