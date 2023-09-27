import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for solving the knapsack problem using
 * a backtracking approach. The Knapsack problem is
 * given a list of items with associated value and weights,
 * determines which items should be selected to
 * maximize the value given a weight constraint.
 * 
 * @author Catie Baker
 *
 */
public class KnapsackBacktrack {
	private List<KnapsackItem> items;
	private int maxWeight;
	private List<KnapsackItem> bestItems;
	private int bestVal;
	private boolean solved;
	

	/**
	 * Creates the criteria of the knapsack problem to be solved.
	 * The file is where the criteria are coming from. The file
	 * will first have the weight limit. Then each item is provided
	 * one per line with the value first, then the weight and then the 
	 * name of the item
	 * @param filename the file with the data about the problem
	 */
	public KnapsackBacktrack(String filename) {
		try {
			Scanner infile = new Scanner(new File(filename));
			this.solved = false;
			this.items = new ArrayList<KnapsackItem>();
			this.maxWeight = infile.nextInt();
			while(infile.hasNextLine()) {
				int val = infile.nextInt();
				int weight = infile.nextInt();
				String name = infile.nextLine().trim();
				this.items.add(new KnapsackItem(name,val,weight));
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	/**
	 * Determine the set of items that will maximize the value of the collection
	 * while remaining at or below the weight limit
	 * @return the value of the best solution
	 */
	public int solve() {
		this.bestItems = solve(0, new ArrayList<KnapsackItem>());
		this.bestVal = this.getKnapsackValue(bestItems);
		this.solved = true;
		return this.bestVal;
	}
	
	/**
	 * Determines the best collection of items to maximize the value while fitting 
	 * the weight constraint given the collection of items in current that were selected
	 * of the first index items
	 * @param index the index of the current item I am determining whether or not
	 * to include
	 * @param current the current state of the collection before we have determined
	 * whether to add the current item
	 * @return the collection of items that have the best value given current as its
	 * starting point
	 */
	private List<KnapsackItem> solve(int index, List<KnapsackItem> current){
		if(index == this.items.size()) {
			return current;
		}
		else {
			if(this.getKnapsackWeight(current)+this.items.get(index).getWeight()>this.maxWeight) {
				return this.solve(index+1, current);
			}
			else {
				List<KnapsackItem> withNext = new ArrayList<KnapsackItem>(current);
				withNext.add(this.items.get(index));
				List<KnapsackItem> bestWithIndex = this.solve(index+1, withNext);
				List<KnapsackItem> bestWithoutIndex = this.solve(index+1, current);
				
				if(this.getKnapsackValue(bestWithoutIndex)>this.getKnapsackValue(bestWithIndex)) {
					return bestWithoutIndex;
				}
				else {
					return bestWithIndex;
				}
			}
		}
	}
	
	/**
	 * Sums the value of all the items in the provided collection
	 * @param itemsIncluded the collection of items to get the value of 
	 * @return the value of the collection of items
	 */
	public int getKnapsackValue(List<KnapsackItem> itemsIncluded) {
		int val = 0;
		for(KnapsackItem item : itemsIncluded) {
			val += item.getValue();
		}
		return val;
	}
	
	/**
	 * Sums the weight of all the items in the provided collection
	 * @param itemsIncluded the collection of items to get the weight of 
	 * @return the weight of the collection of items
	 */
	public int getKnapsackWeight(List<KnapsackItem> itemsIncluded) {
		int w = 0;
		for(KnapsackItem item : itemsIncluded) {
			w += item.getWeight();
		}
		return w;
	}
	
	/**
	 * Prints the value of the best collection that fit the constraints
	 * and then prints the list of items in the collection. If the problem
	 * has not already been solved, it will solve it first.
	 */
	public void printSolution() {
		if(!solved) {
			this.solve();
		}
		System.out.println("The best solution has a value of "+this.bestVal);
		System.out.println("The knapsack contains: ");
		for(KnapsackItem item : this.bestItems) {
			System.out.println(item);
		}
	}
}
