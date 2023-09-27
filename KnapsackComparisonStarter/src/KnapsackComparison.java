import java.util.Scanner;

/**
 * DO NOT MODIFY THIS CLASS!!!
 * 
 * A class to compare a backtracking and genetic algorithm approximation
 * approach so solving the knapsack problem
 * 
 * @author Catie Baker
 *
 */
public class KnapsackComparison {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		while (true) {
			System.out.println("What is the name of the file that has the data?");
			String filename = in.nextLine();
			KnapsackBacktrack exact = new KnapsackBacktrack(filename);
			KnapsackGenetic approx = new KnapsackGenetic(filename);

			long startTime = System.currentTimeMillis();
			int exactValue = exact.solve();
			long endTime = System.currentTimeMillis();
			System.out.println("The backtracking approach took " + (endTime - startTime) + " ms");
			exact.printSolution();

			startTime = System.currentTimeMillis();
			int approxValue = approx.solve();
			endTime = System.currentTimeMillis();
			System.out.println("The approximation approach took " + (endTime - startTime) + " ms");
			approx.printSolution();
			double perDiff = 100.0 * (double) (exactValue - approxValue) / (double) exactValue;
			System.out.println(String.format("The approximation was %.1f%% less", perDiff));

			System.out.println("Do you want to run the comparison on another file? (yes or no)");
			String ans = in.nextLine();
			if (ans.toLowerCase().charAt(0) == 'n') {
				break;
			}
		}

	}
}
