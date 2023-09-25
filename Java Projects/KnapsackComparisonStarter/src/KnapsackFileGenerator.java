import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * A class to generate a random file that meets the formatting requirements of
 * the knapsack hw
 * 
 * @author Catie Baker
 *
 */
public class KnapsackFileGenerator {
	public static final int AVG_COST = 100;
	public static final int AVG_WEIGHT = 12;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("What is the name of the file you want to save to?");
		String filename = in.next();
		System.out.println("How many possible items should there be?");
		int numItems = in.nextInt();
		KnapsackFileGenerator.generateFile(filename, numItems);
	}

	/**
	 * Generates a random file in the format expected by the Knapsack with the
	 * specified parameters.
	 * 
	 * @param filename the name of the file to generate
	 * @param numItems the number of possible items
	 */
	public static void generateFile(String filename, int numItems) {

		try {
			FileWriter out = new FileWriter(new File(filename));
			Random r = new Random();
			int maxWeight = 0;
			String s = "";
			for (int i = 1; i <= numItems; i++) {
				String name = "Item " + i;
				int val = (int) Math.round(r.nextGaussian(AVG_COST, AVG_COST / 4));
				int weight = (int) Math.round(r.nextGaussian(AVG_WEIGHT, AVG_WEIGHT / 4));
				s += val + " " + weight + " " + name + "\n";
				maxWeight += weight;
			}
			int knapsackHolds = (int) Math.round(r.nextGaussian(maxWeight/2, maxWeight / 4));
			out.write(knapsackHolds + "\n");
			out.write(s);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
