import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * A class to compare timings of different operations on different
 * data structures
 * 
 * @author Catie Baker and John Roeder
 *
 */
public class CompareDataStructures {


	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		System.out.println("What is the file with the data to add?");
		String addFile = keyboard.nextLine();
		System.out.println("What data structure do you want to time");
		String dataStructure = keyboard.nextLine();
		Collection<Integer> nums = null;
		if(dataStructure.equals("ArrayList")) {
			nums = new ArrayList<Integer>();
		}
		else if(dataStructure.equals("LinkedList")) {
			nums = new LinkedList<Integer>();
		}
		else if(dataStructure.equals("SortedArrayList")) {
			nums = new SortedArrayList<Integer>();
		}
		else if(dataStructure.equals("TreeSet")) {
			nums = new TreeSet<Integer>();
		}
		else if(dataStructure.equals("HashSet")) {
			nums = new HashSet<Integer>();
		}
		else {
			System.out.println("Invalid data structure");
			System.exit(0);
		}
		keyboard.close();
		CompareDataStructures.testCollection(nums,addFile);

	}

	/**
	 * Gives the times of a series of methods that call add, contains
	 * and find and remove max
	 * @param items the collection to test the operation on
	 * @param addFile the file of data to add to the collections
	 */
	public static void testCollection(Collection<Integer> items, String addFile) {
		System.out.println(items.getClass());
		long startTime = System.currentTimeMillis();
		CompareDataStructures.addFileData(addFile,items);
		long endTime = System.currentTimeMillis();
		long total = endTime-startTime;
		System.out.println("Add: "+total);
		
		startTime = System.currentTimeMillis();
		CompareDataStructures.count(items);
		endTime = System.currentTimeMillis();
		total = endTime-startTime;
		System.out.println("Count: "+total);
		
		startTime = System.currentTimeMillis();
		CompareDataStructures.removeAllReverseSortedOrder(items);
		endTime = System.currentTimeMillis();
		total = endTime-startTime;
		System.out.println("Remove Reverse Sorted Order: "+total);
		
		
	}

	/**
	 * Adds all the data in the provided file to the provided collection
	 * @param filename the file with the numbers to add
	 * @param items the collection to add the numbers to
	 */
	public static void addFileData(String filename, Collection<Integer> items) {
		//read in 
		try {
			String vals; 
			Scanner scan = new Scanner(new File(filename));
			while(scan.hasNextInt()) {
				int data = scan.nextInt(); 
				items.add(data); 
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	/**
	 * Generates n random integers and counts how many are in the
	 * provided collection, where n is the size of items
	 * @param items the collection to search
	 * @return the number of random integers that were in the collection
	 */
	public static int count(Collection<Integer> items) {
		//generate ints
		int size = items.size(); 
		int count = 0; 
		int n; 
		for(int i = 0; i< size; i++) {
			n = (int) (Math.random() * Integer.MAX_VALUE); 
			if (items.contains(n)) {
				count++; 
			}
		}
		return count;
	}
	
	/**
	 * Removes all the items in the collection, removing the largest item in the
	 * list each time
	 * @param items the collection to empty
	 */
	public static void removeAllReverseSortedOrder(Collection<Integer> items) {
		//remove
		int size = items.size();  
		while(!items.isEmpty()) {
			findRemoveMax(items);   
		}
	}


	/**
	 * Finds and removes the largest item in the collection
	 * @param items the collection to remove from
	 * @return the item removed
	 */
	public static Integer findRemoveMax(Collection<Integer> items) {
		if(items instanceof SortedArrayList) {
			return ((SortedArrayList<Integer>) items).remove(items.size()-1);
		}
		else if(items instanceof TreeSet) {
			Integer max = ((TreeSet<Integer>) items).last();
			items.remove(max);
			return max;
		}
		else {
			Integer max = Collections.max(items);
			items.remove(max);
			return max;
		}
	}

}
