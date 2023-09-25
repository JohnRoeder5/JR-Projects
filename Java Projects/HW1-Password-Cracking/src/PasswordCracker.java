/**
 * A class to crack a password by using a brute force approach to try all possible passwords
 * 
 * @author Catie Baker and John Roeder
 *
 */
public class PasswordCracker {

	private String[] specVals; 
	private int minLength; 
	private int maxLength; 
	private PasswordChecker checkP;  
	private boolean ups; 
	private boolean lows; 
	private boolean nums;
	private String finalP; 

	/**
	 * Creates password cracker that can generate all possible passwords that fit the given
	 * criteria
	 * @param check the password checker that determines when you have found the password
	 * @param upper indicates whether the password should contain upper case letters
	 * @param lower indicates whether the password should contain lower case letters
	 * @param numbers indicates whether the password should contain numbers
	 * @param special indicates what special characters are possible (the empty string indicates no special 
	 * characters are possible)
	 * @param maxLen the maximum length of the password
	 * @param minLen the minimum length of the password
	 */
	public PasswordCracker(PasswordChecker check, boolean upper, boolean lower, boolean numbers, String special, int maxLen, int minLen) {
		this.specVals = special.split(""); 
		this.minLength = minLen; 
		this.maxLength = maxLen; 
		this.checkP= check; 
		this.ups = upper;  
		this.lows = lower; 
		this.nums = numbers;

	}

	/**
	 * Trys all possible passwords that fit the criteria and returns the password when it is found. It will
	 * start by trying all the minimum length passwords, then min+1, and so forth up to and including the max 
	 * length passwords
	 * @return the password or the empty string if no passwords match
	 */
	public String crackPassword() { 
		//if correct return password, if not add 1 to minlength up to the max 
		//while loop for password attempts are less than the max and incorrect??
		String start=""; 
		for(int i = this.minLength; i <=this.maxLength; i++) {
			start = tryPasswordsOfLen(i); 
			if(this.checkP.checkPassword(start)) {
				return start; 
			}
		} 
		return "";  
	}

	/**
	 * Creates all passwords of the specific length and compares them using the password checker
	 * and then return the password it found or the empty string if no password matched
	 * @param len the length of the password to try
	 * @return the password or the empty string if no passwords match
	 */
	public String tryPasswordsOfLen(int len) {
		return this.tryPasswordsOfLen(len, ""); 
	}

	/**
	 * Recursively creates all possible passwords by adding each possible character to the
	 * starting string until the starting string is the target length. When it is the target length, it checks
	 * to see if it is the correct password and returns the password it was correct or the empty string if it is
	 * not correct. As soon as the password is found, it stops trying the different passwords and returns the 
	 * cracked password
	 * @param len the length of the password to try
	 * @param start the start of the string
	 * @return the password if it was found or the empty string if it was not found
	 */
	private String tryPasswordsOfLen(int len, String start) {
		//If so, return the password found. If not try the next possibility. If there are no more possibilities, return the empty string.
		// Base case : you have the target length. Return the password if found or the empty string if not found
		String uppers = "QWERTYUIOPASDFGHJKLZXCVBNM"; 
		String lowers = "qwertyuiopasdfghjklzxcvbnm"; 
		String numberVals = "1234567890"; 
		String required = "";
		String finalP =""; 


		//build list of required values
		if(this.ups == true) { 
			required+=  uppers;  
		}
		if(this.lows == true) {
			required += lowers; 
		}
		if (this.nums == true) {
			required += numberVals; 
		}
		for(int i = 0 ; i < this.specVals.length; i++) {
			String specials = this.specVals[i] ;
			required += specials; 
		}

		if(len == start.length() ) {
			if(this.checkP.checkPassword(start) == true) { 
				System.out.println(start);
				return start; 
			} 
		}

		//Recursive Case: For each possible letter, add it to the starting string; then determine if the password is found with that start.
		else {
			for(int i = 0 ; i< required.length(); i++) {
				char val = required.charAt(i);  
				//System.out.println(start+val);
				finalP = tryPasswordsOfLen(len, start+val); 
				if(this.checkP.checkPassword(finalP)) {
					return finalP; 
				}
			}

		}

		return""; 
	} 
}
