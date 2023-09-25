import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Uses a genetic algorithm to find an approximate solution to
 * the Knapsack Problem. The Knapsack problem is
 * given a list of items with associated value and weights,
 * determines which items should be selected to
 * maximize the value given a weight constraint.
 * @author Catie Baker & John Roeder
 *
 */
public class KnapsackGenetic {
	private ArrayList<KnapsackItem> items;  
	private int totalWeight; 
	private int totalItems;  
	private ArrayList<String> chromosomes; 
	private int generationCount; 
	private double lastAvg; 
	private int optimalChromeVal; 
	private ArrayList<KnapsackItem> solutionItems; 
	private int bestS; 
	private int terminationGen; 
	
	/**
	 * Creates the criteria of the knapsack problem to be solved.
	 * The file is where the criteria are coming from. The file
	 * will first have the weight limit. Then each item is provided
	 * one per line with the value first, then the weight and then the 
	 * name of the item
	 * @param filename the file with the data about the problem
	 */
	public KnapsackGenetic(String filename) {
		int val; 
		int weight; 
		String name = ""; 
		try {
			this.items = new ArrayList<KnapsackItem>(); 
			Scanner scan = new Scanner(new File(filename));
			this.totalWeight = scan.nextInt(); 
			scan.nextLine();
			while(scan.hasNextLine()) {
				String line = scan.nextLine(); 
				String[] knapitem = line.split(" "); 
				val = Integer.parseInt(knapitem[0]); 
				weight = Integer.parseInt(knapitem[1]); 
				name = (knapitem[2] + " "+ knapitem[3]); 
				KnapsackItem knapsack = new KnapsackItem(name, val, weight); 
				this.items.add(knapsack); 
				 
			}
			System.out.println(name + "!");
			System.out.println(this.items);
			//get the size of the items in the list to find how many items there are 
			this.totalItems = this.items.size();
			this.solutionItems = new ArrayList<KnapsackItem>(); 
			this.chromosomes = new ArrayList<String>(); 
			this.lastAvg = 0;    
			this.generationCount=0; 
			this.optimalChromeVal = 0; 
			this.bestS = 0; 
			
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
		//start with initial random population. 
		List<String> current = generateInitialPopulation();
		//check the fitness of any chromosomes in the population and see if it is any better than the last population.
		//while termination is false keep going
		while(!termination(current)) {
			current = generateNextGeneration(current); 		
		}
		//once it terminates that means that the generation it is on has been the most optimal within yet
		Collections.sort(current, new evalFit());
		String optimal = current.get(current.size()-1); 
		for(int it = 0; it< optimal.length(); it++){
			if(optimal.charAt(it)=='1') {
				KnapsackItem item  = this.items.get(it); 
				this.solutionItems.add(item); 
			}
			else {
				//item is not in the optimal solution so do nothing. 
			}
		}
		this.optimalChromeVal = evaluateFitness(optimal); 
		System.out.println(this.optimalChromeVal);
		System.out.println(this.generationCount);
		
		return this.optimalChromeVal;
	}
	
	
	public boolean termination(List<String> population) {
		//make sure list is sorted. 
		Collections.sort(population, new evalFit());
		//termination conditions...
		//find the average of a population and use to compare to future gens
		int currValue = 0;  
		String eval;  
		int tempFit;  
		for (int i = 0; i< population.size(); i++) {
			eval = population.get(i); 
			tempFit = evaluateFitness(eval); 
			currValue += tempFit; 
		}
		//get the average of the population
		int currAvg = currValue / population.size(); 
		
		//get the best val from given population
		String currbest = population.get(population.size()-1);
		int currBV = evaluateFitness(currbest);
		
		//termination conditions...
		//if average of the population hasnt gotten better than the last best in 100 generations.
		//means that none of the generated offspring were better than the ones already in population
		//if the best value of a population hasnt gotten better in the last 100 generations. 
		if(this.terminationGen > 50 && this.generationCount >= 200 && !(currBV > this.bestS)) {
			return true; 
		}
		
		//if generation count exceeds 100 successive generations without any of them being better than the last then terminate. 
		if(!(currBV > this.bestS)) {
			this.terminationGen++; 
		}
		else {
			this.terminationGen = 0; 
		}
		this.lastAvg = currAvg; 
		this.bestS = currBV; 
		this.generationCount++; 
		return false;
				
	}
	
	/**
	 * Determines that starting set of potential solutions for the genetic
	 * algorithm. The potential solutions should be strings of 0s or 1s
	 * with each character in the string representing one of the possible
	 * items, with 0 meaning that the item is not included in the collection
	 * and 1 meaning that the item is included in the collection
	 * @return the list of randomly generated potential solutions
	 */
	private List<String> generateInitialPopulation(){
		//represents the idea of the chromosome. so will need a count of items 
		 //in order for something to be a potential solution, it has to have a weight that is acceptable for the knapsack. 
		//so need to generate random and then check them. 
		Random random = new Random(); 
		ArrayList<String> chromosomes = new ArrayList<String>(); 
		//should create the list of all possible populations. 
		//to create an individual chromosome.
		String chromosome = "";
		//run for arbitrary amount of time.  so rn 50 chromosomes should be made. 
		for (int k = 0; k < 50; k++) {
			//need to reset the chromosome when finished. 
			for (int i = 0; i<this.totalItems; i++) {
				int toAdd = (int) random.nextInt(2);  
				chromosome+= toAdd; 
				if(chromosome.length() == this.totalItems) {
					//then add to list and reset 
					chromosomes.add(chromosome);
					chromosome = ""; 
				}
			}
		}
		
		//System.out.println(chromosomes);
		return chromosomes;
		
		
		//number of potential solutions does not matter and you can have duplicates
	}
	
	class evalFit implements Comparator<String>{
		
		public int compare(String val1, String val2) {
			int eval1 = evaluateFitness(val1); 
			int eval2 = evaluateFitness(val2); 
			if (eval1> eval2) {
				return 1; 
			}
			else if(eval1==eval2) {
				return 0; 
			}
			else {
				return -1; 
			}
		}
	}
	
	/**
	 * Generates new offspring from the current set of potential solutions
	 * and replaces some of the current generation with the new offspring. In 
	 * addition, random mutations will occur with some frequency.
	 * @param currGeneration the current set of potential solutions
	 * @return the next generation of potential solutions which will be a mix
	 * of potential solutions from the current generation and new potential
	 * solutions that are generated as offspring of the current generation
	 */
	private List<String> generateNextGeneration(List<String> currGeneration){
		//could use evaluate fitness to determine the most fit chromosomes
		ArrayList<String> nextGen = new ArrayList<String>(); 
		ArrayList<String> vfit = new ArrayList<String>();
		
		//sort the list and then just take half of it
		Collections.sort(currGeneration, new evalFit());
		//System.out.println("Curr Gen: "+ currGeneration);
		//getting the most fit vals starting from the last index moving to the n/2 index
		for (int i = currGeneration.size()-1; i >= currGeneration.size() /2 ; i--) {
			String fitVals = currGeneration.get(i); 
			vfit.add(fitVals); 
		}
		//System.out.println("vfit before offspring: " + vfit);
		
		nextGen = makeOffspring(vfit);
		for(int i = 0; i< nextGen.size(); i++) {
			String value = nextGen.get(i); 
			vfit.add(value); 
		}
		//System.out.println("vfit after: "+ vfit);
		return vfit;
	}
	
	
	
	public ArrayList<String> makeOffspring(List<String> fit) {
		//upon recieving the lsit of fit soluitions from generate next generation, we will split these values and create new ones.
		//create list for new half to be stored 
		ArrayList<String> newHalf = new ArrayList<String>(); 
		
		//reproduction scheme: 
		Random rand = new Random(); 
		//randomly grabs parents and randomly splits them
		for(int i = 0; i< (fit.size() / 2); i++) {
			//determining random index within size of list -fit- to grab a random parent 
			int splitDex1 = rand.nextInt(fit.size());
			int splitDex2 = rand.nextInt(fit.size());
			//cannot have the same chromosome as parent 1 and 2 
			//if parents are the same then get a new parent. 
			if(splitDex1 == splitDex2) {
				splitDex2 = rand.nextInt(fit.size()); 
			}
			//getting chromosome at index
			String chrome1 = fit.get(splitDex1);
			String chrome2 = fit.get(splitDex2); 
			//determining random area to split chromosome between 1, chromosome length -2
			//need to split them at the same index so only need one where split
			int whereSplit1 = rand.nextInt(1, chrome1.length()-2); 
			 

			//split the parents into two pieces. Split both chromosomes at the same place
			//do this so that when we add pieces together they are still of correct length 
			String frontHalf1 = chrome1.substring(0, whereSplit1); 
			String backHalf1 = chrome1.substring(whereSplit1);
			
			String frontHalf2 = chrome2.substring(0, whereSplit1); 
			String backHalf2 = chrome2.substring(whereSplit1);
			
			//combine parents and add them to the new list. 
			String newadd1 = frontHalf1 + backHalf2; 
			String newadd2 = frontHalf2 + backHalf1;
			
			//call mutate method to add any mutations
			String add1 = mutationRate(newadd1); 
			String add2 = mutationRate(newadd2); 
			newHalf.add(add1); 
			newHalf.add(add2); 
		}
		//return the new half of the generation
		return newHalf;
		
	}
	
	public String mutationRate(String newadd) {
		//mutation rate to 5% chance
		Random rando = new Random(); 
		for(int i = 0; i<newadd.length(); i++) {
			double mutation = Math.random();
			
			if(mutation <= 0.05) {
				//less than 0.05 then mutate
				if(newadd.charAt(i)== '0') {
					newadd.replace(newadd.charAt(i), '1'); 
				}
				else {
					newadd.replace(newadd.charAt(i), '0');
				}	
			}
		}
		//returns the new string w/ or without mutations. 
		return newadd; 
	}
	
	/**
	 * Determines the fitness (value of the knapsack) of the potential solution.
	 * If the solution is over the weight limit, it will return 0. If it is at
	 * or under the weight limit, it will return the sum of the values of the 
	 * items that are in the potential solution.
	 * @param potential the potential solution which is a string of 0s or 1s
	 * with each character in the string representing one of the possible
	 * items, with 0 meaning that the item is not included in the collection
	 * and 1 meaning that the item is included in the collection
	 * @return 0 if the solution is over the weight limit, or the sum of the values
	 * of the items in the solution if the solution is at or below the weight limit
	 */
	private int evaluateFitness(String potential) {
		int knapVal=0; 
		int knapWeight=0; 
		for(int i = 0; i< potential.length(); i++) {
			if (potential.charAt(i)=='1') {
				//add the items value to the total knapsack value
				KnapsackItem item = this.items.get(i); 
				int val = item.getValue(); 
				int weight = item.getWeight(); 
				knapVal += val; 
				knapWeight+= weight; 
			}
		}
		//if the weight is greater than that allowed for the knapsack then return 0
		if(knapWeight <= this.totalWeight) {
			return knapVal; 
		}
		//If it is at or under the weight limit, it will return the sum of the values 
		//of the items that are in the potential solution.
		else {
			return 0; 
		}
	}
	
	/**
	 * Prints the value of the best collection that fit the constraints
	 * and then prints the list of items in the collection. If the problem
	 * has not already been solved, it will solve it first.
	 */
	public void printSolution() {
		//taken from backtrack 
		System.out.println("The best solution has a value of "+ this.optimalChromeVal);
		System.out.println("The knapsack contains: "+ this.solutionItems);
		for(KnapsackItem item : this.solutionItems) {
			System.out.println(item);
		}
	}
}


