import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class PerformanceDriver {
	
	public static void main(String[] args) { 
		Scanner userInput = new Scanner(System.in);
        while(true) { 
    		System.out.println("enter the size of the list");
    		String val = userInput.nextLine();
			int num = Integer.parseInt(val); 
			Performance p = new Performance();
			
			System.out.println("Size:     ArrayList addInOrder     LinkedList addInOrder   ArrayList addInReverseOrder   LinkedList addInReverseOrder   ArrayList addInOrderRandom   LinkedList addInRandomOrder");
		for(int i =0; i< 5;  i++) {
			List<Integer> arr = new ArrayList<Integer>();  	 
			long startTime = System.currentTimeMillis();
			p.addInOrder(arr, num);
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			
			
			List<Integer> LL = new LinkedList<Integer>();
			long startTimeLL = System.currentTimeMillis();
			p.addInOrder(LL, num);
			long endTimeLL = System.currentTimeMillis();
			long durationLL = endTimeLL - startTimeLL;
			
			
			//for reverse
			List<Integer> arrReverse = new ArrayList<Integer>();
			long startTimeRev = System.currentTimeMillis();
			p.addInReverseOrder(arrReverse, num); 
			long endTimeRev = System.currentTimeMillis();
			long durationRev = endTimeRev - startTimeRev;
			
			
			List<Integer> LLReverse = new LinkedList<Integer>();
			long startTimeLLrev = System.currentTimeMillis();
			p.addInReverseOrder(LLReverse, num);
			long endTimeLLrev = System.currentTimeMillis();
			long durationLLrev = endTimeLLrev - startTimeLLrev;
			
			
			//for random
			List<Integer> arrRandom = new ArrayList<Integer>(); 
			long startTimeRandom = System.currentTimeMillis();
			p.addInRandomOrder(arrRandom, num);
			long endTimeRandom = System.currentTimeMillis();
			long durationRandom = endTimeRandom - startTimeRandom;
			 
			
			List<Integer> LLRandom = new LinkedList<Integer>();
			long startTimeLLran = System.currentTimeMillis();
			p.addInRandomOrder(LLRandom, num);
			long endTimeLLran = System.currentTimeMillis();
			long durationLLran = endTimeLLran - startTimeLLran;
			
			System.out.println( num+":                " + duration+" msec                    " + durationLL+ " msec                         " + durationRev+" msec                        " + durationLLrev+" msec                        "+ durationRandom+" msec                    "+ durationLLran+ " msec");
			 
			num = num*2;
			
		}
		 
		
			
			
		}
	}
}
