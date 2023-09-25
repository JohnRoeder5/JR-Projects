import java.util.Scanner;

/**
 * Simple driver class for converting a braille file 
 * given a file with the mapping between braille 
 * character and text
 * 
 * @author Catie Baker
 *
 */
public class HW4Driver { 

	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What file holds the alphabet");
		String code = keyboard.nextLine().trim();
		BrailleTree tree = new BrailleTree(code);
		System.out.println("What is the file to convert?");
		String convert = keyboard.nextLine().trim();
		System.out.println("What is the file to write to?");
		String write = keyboard.nextLine().trim();
		tree.translateFile(convert, write); 

	}

}
