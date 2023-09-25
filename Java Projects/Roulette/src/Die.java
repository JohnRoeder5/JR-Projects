/**
 * Class that simulates a single die.
 *   @author Dave Reed
 *   @version 1/10/17
 */
 public class Die {
  private int numSides;   // number of die sides
  private int numRolls;
 
   /**
   * Constructs a 6-sided die object
   */
  public Die() {
    this(6);
  }
  
  /**
   * Constructs a die object
   *   @param sides number of die sides
   */
  public Die(int sides) {
    this.numSides = sides;
    this.numRolls = 0;
  }

  /**
   * Rolls the die, updating the number of rolls.
   *   @return random number between 1 and getNumSides()
   */
  public int roll() {
      this.numRolls++;
    return (int)(Math.random()*this.numSides) + 1;
  }

  /**
   * Reports the number of sides on the die.
   *   @return number of die sides
   */
  public int getNumSides() {
    return this.numSides;
  }
  
  /**
   * Reports the number of times the die has been rolled
   *   @return number of die rolls
   */
  public int getNumRolls() {
    return this.numRolls;
  }
} 
