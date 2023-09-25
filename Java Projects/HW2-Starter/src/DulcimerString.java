import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * class that models a dulcimer string. uses fields dulcnote which is the note from the user and the
 * Q which is the Queue that is created/ 
 * @author 17858 John Roeder
 *
 */
public class DulcimerString {
	private String dulcNote; 
	private Queue<Double> Q;   

	/**
	 * This is the constructor for the class. initializes the fields dulcnote as note and 
	 * Q as a queue. also is used to find the value that determines the size of the queue (val and N working together) and how many
	 * zeroes need to be added to it. 
	 * @param note this is the note that corresponds to the key pressed by the user. 
	 */
	public DulcimerString(String note) {
		this.dulcNote = note;
		this.Q = new LinkedList<Double>(); 
		double val = StdAudio.SAMPLE_RATE * Math.pow(2, ((22 - getOffsetFromMiddleC(note))/ (double)12)) / (double)440; 
		int N = (int) Math.round(val); 
		for (int i = 0; i<N; i++) {
			double zeroes = 0.0; 
			this.Q.add(zeroes); 
		}
	}

	/**
	 * This serves as an accessor method for the note 
	 * @return returns the note that is being played 
	 */
	public String getNote() {
		return this.dulcNote; 
	}

	/**
	 * used to find the base notes offset from center C. this is done by getting rid of the + 
	 * or - signs and finding the offset of the base not. Then based on how may pluses or minus
	 * signs the original note has, it will add 12 or subtract 12 depending on the sign. variables
	 * noteVal = offset of base note. noteNew = the base note that is created by getting rid of 
	 * + and - signs. offset= the total offset of the note. plus and minus are used to keep track
	 * of the plus and minus 12's
	 * @param note the note that is played 
	 * @return returns the total offset of the note from middle C
	 */
	public int getOffsetFromMiddleC(String note) {
		String val; 
		int plus = 0; 
		int minus = 0;
		int offset= 0; 
		int noteVal = 0; 
		String noteNew ="";
		ArrayList<String> values = new ArrayList<String>();
		values.add("A");
		values.add("A#");
		values.add("B");
		values.add("C");
		values.add("C#"); 
		values.add("D");
		values.add("D#"); 
		values.add("E");
		values.add("F");
		values.add("F#");
		values.add("G");
		values.add("G#");
		for (int k = 0; k< note.length(); k++) {
			char let = note.charAt(k); 
			if (let == '+') {
				plus += 12 ;
			}
			else if (let == '-') {
				minus -= 12 ; 
			}
			else {
				noteNew += let; 
			}

		}
		noteVal = values.indexOf(noteNew) - values.indexOf("C");
		offset = noteVal + plus +minus; 
		return offset;
	}

	/**
	 * method that simulates striking the dulcimer. removes all zeroes from the queue and adds 
	 * random values between -0.5 and 0.5 variable val is the random value that is added each time
	 */
	public void strike() {
		//should "strike" the Dulcimer. replaces the values in the Queue with random values
		//-0.5 through 0.5
		double val; 
		double max = 0.5; 
		double min = -0.5;  
		for (int i = 0; i< this.Q.size(); i++) {
			this.Q.poll(); 
			val = Math.random() * (max - min) + min; 
			this.Q.add(val); 

		}
	}

	/**
	 * method that is used to update the values of the queue. takes the front value and the second
	 * following value and takes the average of them. then multiplies by 0.996 which is the decay 
	 * factor. variables frontVal = the value at the front of the queue. addtoEnd = the avg of the first 
	 * two values times 0.966. this is the value that is added to the end. 
	 * @return the value currently stored at the front of the queue.
	 */
	public double sample() {
		//returns the value currently stored in the queue. and removes it then adds 
		//another value to the back that is the average of the 2 multiplied by 0.996
		double frontVal = this.Q.poll(); 
		double addToEnd = ((this.Q.peek() + frontVal)/2) * 0.996; 
		this.Q.add(addToEnd); 
		return frontVal; 
	}
}
