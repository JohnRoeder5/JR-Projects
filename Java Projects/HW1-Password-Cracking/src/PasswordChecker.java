import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Encryption of password based on SHA-256
 * 
 * encrypt this String from Geeks for Geeks tutorial:
 * https://www.geeksforgeeks.org/sha-1-hash-in-java/
 * 
 * @author Catie Baker
 *
 */
public class PasswordChecker {
	private String passwordHash;
	
	/**
	 * Creates an object that can check if the user 
	 * @param password the text to keep encrypted
	 */
	public PasswordChecker(String password) {
		this.passwordHash = PasswordChecker.encryptThisString(password);
	}
	
	/**
	 * Determines if the provided text matches the encrypted text
	 * @param possiblePassword the text to check
	 * @return true if they match, false otherwise
	 */
	public boolean checkPassword(String possiblePassword) {
		return passwordHash.equals(PasswordChecker.encryptThisString(possiblePassword));
	}
	
	/**
	 * Encrypts the input using SHA-256
	 * @param input the text to excrypt
	 * @return the Hex value of the encrypted text
	 */
	public static String encryptThisString(String input)
    {
        try {
            // getInstance() method is called with algorithm SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");
 
            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());
 
            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);
 
            // Convert message digest into hex value
            String hashtext = no.toString(16);
 
            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
 
            // return the HashText
            return hashtext;
        }
 
        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
	
	
	

}
