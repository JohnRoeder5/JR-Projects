import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * class that read in a file and interprets results of accessibility tests
 * @author John Roeder
 *
 */
public class AccessibilityResults {
	private ArrayList<AccessibilityTest> results; 
	
	/**
	 * scans in the given file using a list. 
	 * @param filename
	 */
	public AccessibilityResults(String filename)  {
		this.results = new ArrayList<AccessibilityTest>(); 
		try {
			Scanner infile = new Scanner(new File(filename));
			while (infile.hasNextLine()) {
				String category = infile.next().toLowerCase(); 
				String googleResult = infile.next().toLowerCase(); 
				String waveResult = infile.next().toLowerCase(); 
				String sortsiteResult = infile.next().toLowerCase(); 
				String aslintResult = infile.next().toLowerCase(); 
				String tDescription = infile.nextLine().toLowerCase(); 
				AccessibilityTest newResults = new AccessibilityTest(category, googleResult, waveResult, sortsiteResult, aslintResult, tDescription); 
				this.results.add(newResults); 
				 
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error");
			
		} 
	}
	/**
	 * finds the size of the arraylist we have made 
	 * @return
	 */
	public int numTests() {
		int size = (this.results.size()); 
		return size;  
	}
	
	/**
	 * Takes a test detail and displays test information of all tests that match that category
	 * Only uses the tdescription which is the back portion of each line so we only want to look there. 
	 * Should take a parameter that is only the lines of test description
	 */
	public void showTestResults(String tDescription) {
		 int count = 0; 
		 for (AccessibilityTest newResults : this.results) {
			 if (newResults.getTdescription().contains(tDescription)) {
				 System.out.println(newResults);
				 count++; 
			 }
		 }
		 System.out.println("total tests matching: " +count);
	}
	
	/**
	 * takes a category from the user and counts the amount of times that that word appears and 
	 * displays the test information for that category. 
	 * @param category - specifies the category that the user wants to check for.
	 */
	public void showByCategory(String category) {
		int count = 0; 
		for (AccessibilityTest newResults : this.results) {
			if (newResults.getCategory().contains(category)) {
				System.out.println(newResults);
				count++; 
			}
		}
		System.out.println("There are "+count+ " categories."); 
	}
	
	
	/**
	 * Shows the number of categories that had the checkers failed across the board
	 * and prints out those failed tests. Also keeps a running total of the failed test count
	 */
	public void showAllFailed() {  
		int failedTests= 0;  
		for (AccessibilityTest newResults : this.results) {
			if ((newResults.getGoogleR().contains("notfound") && newResults.getWave().contains("notfound")) && (newResults.getSortSite().contains("notfound") && newResults.getAslint().contains("notfound"))) {
				failedTests++; 
				System.out.println(newResults);
			}
		}
		System.out.println("There were "+ failedTests+ " failed tests.");
	}
	/**
	 * Finds the number of tests that resulted in either error or error_paid. variable info is used as a counter for the number of tests error or error_paid.
	 * @param tests - one of the four test types given by the user. 
	 * @param category - specifies the category the user wants to check the test results for. 
	 * @return returns a count of the total number of error and error_paid tests. 
	 */
	public void numPass(String tests, String category) { 
		int info = 0; 
		for (AccessibilityTest newResults: this.results) {
			if (newResults.getCategory().contains(category)) {
				if (("google checker").contains(tests) && (newResults.getGoogleR().contains("error") || newResults.getGoogleR().contains("error_paid"))) {
					info++;
				}
				if (("wave checker").contains(tests) && (newResults.getWave().contains("error") || newResults.getGoogleR().contains("error_paid"))) {
					info++; 
				}
				if (("sortsite checker").contains(tests) && (newResults.getSortSite().contains("error") || newResults.getGoogleR().contains("error_paid"))) {
					info++; 
				}
				if (("aslint").contains(tests) && (newResults.getAslint().contains("error") || newResults.getGoogleR().contains("error_paid"))) {
					info++; 
				}
			}
		}
		System.out.println("There are " +info+ " tests resulting in error or error_paid"); 
	}
}


