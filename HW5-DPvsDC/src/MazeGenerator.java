import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

/**
 * A class to generate a random file that meets the formatting requirements
 * of the 
 * @author Catie Baker
 *
 */
public class MazeGenerator {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("What is the name of the file you want to save to?");
		String filename = in.next();
		System.out.println("How many rows should there be?");
		int rows = in.nextInt();
		System.out.println("How many columns should there be?");
		int cols = in.nextInt();
		System.out.println("How many walls should there be?");
		int walls = in.nextInt();
		System.out.println("How many cells with coins should there be?");
		int coins = in.nextInt();
		MazeGenerator.generateFile(filename, rows, cols, walls, coins);
	}
	

	/**
	 * Generates a random file in the format expected by the CoinCollector
	 * with the specified parameters.
	 * @param filename the name of the file to generate
	 * @param rows the number of rows in the grid
	 * @param cols the number of columns in the grid
	 * @param walls the number of walls in the grid
	 * @param coins the number of coins in the grid
	 */
	public static void generateFile(String filename, int rows, int cols, int walls, int coins) {
		char[][] grid = new char[rows][cols];
		for(int row = 0; row<rows; row++) {
			for(int col = 0; col<cols; col++) {
				grid[row][col] = ' ';
			}
		}
		Random r = new Random();
		while(walls>0) {
			int row = r.nextInt(rows);
			int col = r.nextInt(cols);
			if(grid[row][col] == ' ') {
				grid[row][col] = 'x';
				walls--;
			}
		}
		while(coins>0) {
			int row = r.nextInt(rows);
			int col = r.nextInt(cols);
			if(grid[row][col] == ' ') {
				int numCoins = r.nextInt(9)+1;
				grid[row][col] = (char) ('0'+numCoins);
				coins--;
			}
		}
		try {
			FileWriter out = new FileWriter(new File(filename));
			out.write(rows+"\n");
			out.write(cols+"\n");
			for(int row = 0; row<rows; row++) {
				for(int col = 0; col<cols; col++) {
					out.write(grid[row][col]);
				}
				out.write("\n");
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
