import java.util.Scanner;

/**
 * Code for testing the password cracking using different settings
 * @author Catie Baker
 *
 */
public class PasswordDriver {
	
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Should the password contain uppercase letters only, lowercase letters only, neither or both?");
		String let = in.nextLine().trim().toLowerCase();
		boolean allowUpper = false;
		boolean allowLower = false;
		boolean allowNums = false;
		if(let.contains("upper") || let.contains("both")) {
			allowUpper = true;
		}
		if(let.contains("lower") || let.contains("both")) {
			allowLower = true;
		}
		System.out.println("Should the password contain numbers? Y or N");
		String num = in.nextLine().trim().toLowerCase();
		if(num.startsWith("y")) {
			allowNums = true;
		}
		System.out.println("What special characters (e.g. *, !, etc.) should the password allow? Type all possible without spaces (e.g. !@#$)");
		String special = in.nextLine().trim().toLowerCase();
		System.out.println("What is the max length of the password?");
		int max = in.nextInt();
		System.out.println("What is the minimum length of the password?");
		int min = in.nextInt();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
		PasswordChecker check = new PasswordChecker((new RandomPasswordGenerator(allowUpper, allowLower, allowNums, special, max, min)).generateRandomPassword());
		PasswordCracker crack = new PasswordCracker(check, allowUpper, allowLower, allowNums, special, max, min);
		long start = System.currentTimeMillis();
		String foundPassword = crack.crackPassword();
		long end = System.currentTimeMillis();
		System.out.println("Password found in "+(end-start)+" msec. The password was "+foundPassword);
		in.close();
	}
	
	
}
