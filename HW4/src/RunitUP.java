
public class RunitUP {
	public static void main(String[] args) {
		AccessibilityResults test = new AccessibilityResults("src/a11yCheckersResults.txt"); 
		test.showByCategory("content");
		//test.numPass("aslint", "");
		//test.toString();
		//test.showAllFailed();
		//test.showTestResults("");
	}
}
