 /**
 * Class that models an American-style roulette wheel (with numbers 1-36, 0, and 00).
 *   @author Dave Reed
 *   @version 9/14/17
 */
public class RouletteWheel {
    private Die roller;          // used for generating the random spin

    /**
     * Constructor for objects of class RouletteWheel
     */
    public RouletteWheel() {
        this.roller = new Die(38);
    }

    /**
     * Simulates a single spin of the roulette wheel.
     *   @return the slot value obtained by the spin (e.g., "12" or "00") 
     */
    public String spin() {
        int number = this.roller.roll();
        if (number == 38) {
            return "00";
        }
        else if (number == 37) {
            return "0";
        }
        else {
            return ""+number;
        }
    }
    
    /**
     * Determines the color for a particular number on the wheel.
     *   @param slotValue the slot value on the wheel (e.g., "12" or "00")
     *   @return that slot value's color (either "red", "black", or "green")
     */
    public String getColor(String slotValue) {
        String redNums = " 1 3 5 7 9 12 14 16 18 19 21 23 25 27 30 32 34 36 ";
        String blackNums = " 2 4 6 8 10 11 13 15 17 20 22 24 26 28 29 31 33 35 ";
        
        if (redNums.contains(" "+slotValue+" ")) {
            return "red";
        }
        else if (blackNums.contains(" "+slotValue+" ")) {
            return "black";
        }
        else {
            return "green";
        }
    }

    /**
     * Determines the parity for a particular number on the wheel.
     *   @param slotValue the slot value on the wheel (e.g., "12" or "00")
     *   @return that slot value's parity (either "odd", "even", or "zero")
     */
    public String getParity(String slotValue) {
        int numVal = Integer.parseInt(slotValue);
        if (numVal == 0) {
            return "zero";
        }
        else if (numVal % 2 == 0) {
            return "even";
        }
        else {
            return "odd";
        }
    }
    
    /**
     * Reports the number of times the wheel has been spun.
     *   @return the number of spins so far
     */
    public int getNumberOfSpins() {
        return this.roller.getNumRolls();
    }
}
