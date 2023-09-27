/**
 * Class that store the information about an item that can
 * be stored in the knapsack
 * 
 * @author Catie Baker
 *
 */
public class KnapsackItem {
	private String name;
	private int weight;
	private int value;
	
	/**
	 * Creates an item with the given properties
	 * @param name the name of the item
	 * @param val the items value
	 * @param w the items weight
	 */
	public KnapsackItem(String name, int val, int w) {
		this.name = name;
		this.value = val;
		this.weight = w;
	}
	
	/**
	 * Gets the name of the item
	 * @return the name of the item
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Gets the value in dollars of the item
	 * @return the dollar value of the item
	 */
	public int getValue() {
		return this.value;
	}
	
	/**
	 * Gets the weight of the item
	 * @return the weight of the item in pounds
	 */
	public int getWeight() {
		return this.weight;
	}
	
	/**
	 * Returns a string that has the name, followed by value and weight
	 * of the item
	 */
	public String toString() {
		return this.name+" Value: "+this.value+" Weight: "+this.weight;
	}
}
