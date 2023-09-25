import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
/**
 * A password generator where you can control what characters can
 * be in the password and what length the password needs to be 
 * 
 * @author Catie Baker and John Roeder
 *
 */
public class RandomPasswordGenerator {
	private final static String[] ups = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V" ,"B", "N", "M"};
	private final static String[] lows = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"}; 
	private final static String[] nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
	private String[] specials; 
	private int maxPass; 
	private int minPass; 
	private int upCount; 
	private int lowCount;
	private int specCount; 
	private int numCount; 
	
	
	
	
	
	/**
	 * Creates a password generator with the specified properties. For each category that is true, the password 
	 * must contain at least one character of that category (e.g. if numbers is true, the password must contain at 
	 * least one number).
	 * @param upper indicates whether the password should contain upper case letters
	 * @param lower indicates whether the password should contain lower case letters
	 * @param numbers indicates whether the password should contain numbers
	 * @param special indicates what special characters are possible (the empty string indicates no special 
	 * characters are possible)
	 * @param maxLen the maximum length of the password
	 * @param minLen the minimum length of the password
	 */
	public RandomPasswordGenerator(boolean upper, boolean lower, boolean numbers, String special, int maxLen, int minLen) {
		this.specials = special.split(""); 
		this.maxPass = maxLen; 
		this.minPass = minLen; 
		this.upCount= 0; 
		this.lowCount = 0; 
		this.specCount = 0; 
		this.numCount = 0; 
		//to keep track of which categories need to be used 
		if(upper == true) {
			this.upCount +=1; 
		}
		if(lower == true) {
			this.lowCount +=1; 
		}
		if(!special.isEmpty()) {
			this.specCount +=1; 
		}
		if(numbers == true) {
			this.numCount +=1; 
		}
	}
	
	/**
	 * Creates a password checker for the specified password
	 * @param password the password to create the checker for
	 * @return the password checker
	 */
	public PasswordChecker getPasswordChecker(String password) {
		return new PasswordChecker(password);
	}
	
	/**
	 * Generates a random password using the characteristics of the generator. The password
	 * length will be between the min and max length (inclusive) and include at least one
	 * of each of the specific characters.
	 * @return the random password that was generated
	 */
	public String generateRandomPassword() {
		//pass through all 4 types of lists??
		// while sum of category counts < whatever val is. always have it be 4. 0-4 //// use arraylist and collections.shuffle
		//0 - upper, 1 - lower, 2 - special, 3 - numbers
		int upC = 0;
		int lowC = 0; 
		int specC = 0; 
		int numC = 0; 
		int[] categories = {this.upCount, this.lowCount, this.specCount, this.numCount}; 
		ArrayList<String> passShuff = new ArrayList<String>();  
		String pass = "";
		int passval = (int) (Math.random() * (this.maxPass - this.minPass)) + this.minPass ; 
		while((upC + lowC + specC + numC) < passval) {
			if(categories[0] != 0) {
				int index = (int) (Math.random() * (26)) ; 
				String add = ups[index]; 
				passShuff.add(add); 
				upC +=1; 
			}
			if(categories[1]!= 0) { 
				int index = (int) (Math.random() * (26)) ;
				String add = lows[index]; 
				passShuff.add( add); 
				lowC+=1; 
			}
			if(categories[2]!=0) {
				int index = (int) (Math.random() * this.specials.length); 
				String add = this.specials[index]; 
				passShuff.add( add); 
				specC += 1;  
			}
			if(categories[3] != 0) {
				int index = (int) (Math.random() * (9)); 
				String add = nums[index]; 
				passShuff.add( add); 
				numC+=1; 
			}
			
		}
		System.out.println(passShuff);
		Collections.shuffle(passShuff);
		for(int i = 0; i<passShuff.size(); i++) {
			pass+= passShuff.get(i); 
		}
		System.out.println(pass);
		return pass; 
		
	}

}
