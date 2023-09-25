import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * A class to generate files with numbers for testing purposes
 * 
 * @author Catie Baker
 */
public class FileGenerator {

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the file to write the data?");
		String outFile = keyboard.nextLine();
		System.out.println("What order do you want the data in? Options: [in order, reverse, random]");
		String order = keyboard.nextLine();
		System.out.println("How many items do you want in the file");
		int size = keyboard.nextInt();
		keyboard.close();
		FileGenerator.generateFile(outFile, order, size);

	}
	
	/**
	 * Generates a file of size integers, in the specified order
	 * @param outFile the file to create
	 * @param order the order of the numbers
	 * @param size the number of numbers to write to the file
	 */
	public static void generateFile(String outFile, String order, int size) {
		try {
			FileWriter outfile = new FileWriter(new File(outFile));
			if(order.equals("in order")) {
				for(int i = 0; i<size; i++) {
					outfile.write(i+"\n");
				}
			}
			else if(order.equals("reverse")) {
				for(int i = size; i>0; i--) {
					outfile.write(i+"\n");
				}
			}
			else {
				Random r = new Random();
				for(int i = 0; i<size; i++) {
					int num = r.nextInt();
					outfile.write(num+"\n");
				}
			}
			
			outfile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
