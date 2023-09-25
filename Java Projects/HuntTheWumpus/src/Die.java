/**
 * Models a die object which can have any number of sides 
 * @author John Roeder
 * @version 5-26-2022
 */

public class Die {
	private int numSides;
	private int prevRoll; 
	private int numRolls; 
	
	// creates die object w number of sides and the value of the previous roll
	public Die(int numSides) {
		this.numSides = numSides;
		this.prevRoll = 1;
		this.numRolls = 0; 
	}
	
	public Die () {
		this(6);
	}
	
	// returns number of sides on die
	public int getNumSides() {
		return this.numSides; 
	}
	
	//returns the number of times the die has been rolled 
	public int getNumRolls() {
		return this.numRolls; 
	}
	
	//sims a random roll, returns value rolled 
	public int roll() {
		this.prevRoll = (int)(Math.random()*this.numSides) + 1;
		this.numRolls = this.numRolls + 1;
		return prevRoll; 
	}
	
}
