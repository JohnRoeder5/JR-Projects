
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Performance {
	 
	public Performance() { 
	}
	
	/**
	 * 
	 * @param <T> - 
	 * @param numbers - the list that we are searching to look up the indexes for the items we are adding. 
	 * @param desired - desired is the number we are searching the list for.
	 * @return - returns the index of where the number should go in the list numbers. 
	 */
	public static <T extends Comparable<? super T>> int binarySearch(List<Integer> numbers, Integer desired) {
		int start = 0; 
        int end = numbers.size()-1;
        int index =0; 
        while(start <= end ) {
        	int mid = start + ((end - start) / 2);
        	for (int i = 0; i<numbers.size(); i++) { 
        		int comp = desired.compareTo(numbers.get(mid)); 
        		if (i == comp) {
        			 index =  mid;  
        		}
        		else if(i>comp) { 
        			start = mid + 1;  
        		}
        		else {
        			end = mid-1; 
        		}
        	}
        }
		return index; 
	}
	
	/**
	 * 
	 * @param numbers - numbers is the list we are adding the values to. 
	 * @param n - n is the size of the list. we are adding 0 through n items. 
	 */
	public static void addInOrder(List<Integer> numbers, int n) {  
		int index; 
		for (int i = 0; i<n; i++) { 
        	index = Collections.binarySearch(numbers, i); 
        	if(index < 0) {
        		index= Math.abs(index) -1 ; 
        	}
        	//System.out.println(numbers);
        	numbers.add(index, i); 
        }
    }
	
	/**
	 * 
	 * @param numbers -  numbers is the list we are adding the values to.
	 * @param n - n is the size of the list. we are adding 0 through n items.
	 */
	public static void addInReverseOrder(List<Integer> numbers, int n) {
		int index; 
        for (int i = n; i>0; i--) { 
        	index = Collections.binarySearch(numbers, i); 
        	if(index < 0) {
        		index= Math.abs(index) -1 ; 
        	}
        	//System.out.println(numbers);
        	numbers.add(index, i); 
        }
	}
	
	/**
	 * 
	 * @param numbers -  numbers is the list we are adding the values to.
	 * @param n - n is the size of the list. we are adding 0 through n items.
	 */
	public static void addInRandomOrder(List<Integer> numbers, int n) {
		int index; 
        for (int i = 0; i<n; i++) { 
        	int num = (int) (Math.random() * Integer.MAX_VALUE); 
        	index = Collections.binarySearch(numbers, num); 
        	if(index < 0) {
        		index= Math.abs(index) -1 ; 
        	}
        	//System.out.println(numbers);
        	numbers.add(index, i); 
        }
	}
}
