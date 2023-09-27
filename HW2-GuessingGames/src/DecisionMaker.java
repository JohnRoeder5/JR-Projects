import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;


/**
 * This class uses the provided data to determine what
 * questions to ask to narrow in on a specific item. All
 * questions come from a list of possible questions that
 * can be answered yes/not
 * 
 * @author Catie Baker and John Roeder
 *
 */
public class DecisionMaker {
	private int qAsk; 
	
	//unmodifiable questions list
	private ArrayList<String> questions;  
	
	//add modifiable questions list 
	private ArrayList<String >qMod; 
	
	//unmodifiable questions HashMap
	private HashMap<String, Movies> movieOptions; 
	
	//modifiable movies list
	private ArrayList<String> movies; 
	

	
	/**
	 * Reads in the questions that can be asked and the possible
	 * choices as well as their answers
	 * @param filename the name of the file that stores the data. 
	 * The file starts off with the number of questions that are possible 
	 * to be asked. Then it lists each question, one per line. Finally, 
	 * it ends will all the possible choices and their answers to the questions.
	 * The possible choice and its answer are separated by commas. Each possibility
	 * is on a single line.
	 */
	public DecisionMaker(String filename) {
		try {  
			this.qMod = new ArrayList<String>();
			this.questions = new ArrayList<String>(); 
			this.movieOptions = new HashMap<String, Movies>(); 
			this.movies = new ArrayList<String>();
			String movie = ""; 
			Scanner scan = new Scanner(new File(filename));
			this.qAsk = scan.nextInt();
			scan.nextLine(); 
			System.out.println(this.qAsk);
			for (int i = 0; i<this.qAsk; i++) {
				String line = scan.nextLine(); 
				this.questions.add(line);
				this.qMod.add(line); 
			}
			System.out.println(this.questions);
			while(scan.hasNextLine()) {
				String[] movies = scan.nextLine().split(",");
				ArrayList<String> indAnswers = new ArrayList<String>();
				for(int j = 1; j<movies.length; j++) {
					movie = movies[0]; 
					indAnswers.add(movies[j]) ;
				}
				this.movies.add(movie); 
				//System.out.println(indAnswers);
				//make hash map with movie as the key 
				Movies newMovie = new Movies(movie, indAnswers); 
				this.movieOptions.put(movie, newMovie); 
			}
			System.out.println(this.movies);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * Given the specified question, update the possible answers 
	 * to only the options that match the provided answer and remove
	 * the asked question from the questions that can be asked
	 * @param question the question that was asked
	 * @param answer the answer to the question (yes = true, and no = false)
	 */
	public void pruneOptions(String question, boolean answer) {
		//0th index has a space in it so questions are on the +1 spot. 
		//ie question 1 will be found in index 1 and corresponds to answer 0. and so on
		 
		this.movies = getMatches(question, answer); 
		//use new q list here 
		this.qMod.remove(question); 
	}
	
	/**
	 * Gets the matches for questions. used to find the best possible question to ask. 
	 * searches index of question. gets movies from hashmap. sets the answer value to compare to the desired answer
	 * returns list of available movies. 
	 * @param question - the question that could possibly be asked
	 * @param answer - the desired answer
	 * @return - list of available movies. 
	 */
	public ArrayList<String> getMatches(String question, boolean answer) {
		boolean yn;
		int indexVal = 0;
		ArrayList<String> tempList = new ArrayList<String>(); 
		//search for index of the question that was asked 
		indexVal = this.questions.indexOf(question); 
		//find the corresponding answer to the question we found 
		for(int j = 0;j < this.movies.size() ; j++) {
			//how to get the movie name from the HashMap
			Movies mov = this.movieOptions.get(movies.get(j)); 
			ArrayList<String> ans = mov.getAns(); 
			//set the answer value to compare to the movie's answers 
			if (ans.get(indexVal).equals("yes")) {
				yn = true; 
			}
			else {
				yn = false; 
			}
			//if answer at the index does not match the answer desired then remove the corresponding move as an option and update the options
				if(answer == yn) {
					String toAdd = this.movies.get(j); 
					tempList.add(toAdd); 
				}
		}
		return tempList; 
	}
	
	/**
	 * Determines the next question to ask. To determine the next question,
	 * it looks at how each question partitions the data and selects the question
	 * that does the best in the worst case (e.g. the response for yes and no are
	 * as close to each other as possible)
	 * @return the best question to ask next
	 */
	public String getNextQuestion() {
		//want to get pruneOptions result list to see what movies are eliminated by what questions.
		String bestQ = ""; 
		int bestYesRemain = Integer.MAX_VALUE; 
		int bestNoRemain = 0; 
		for(int i = 0 ; i < this.qMod.size(); i++) {
			String ques = this.qMod.get(i); 
			
			//make helper to do this 
			//call helper here 
			int yesSize= getMatches(ques, true).size(); 
			int noSize = Math.abs(this.movies.size() - yesSize);  
			 
			  
			
			int testDiff = Math.abs(yesSize - noSize); 
			//System.out.println(ques);
			//System.out.println(testDiff);
			if(testDiff < Math.abs(bestYesRemain - bestNoRemain)) {
				bestYesRemain= yesSize; 
				bestNoRemain=noSize   ; 
				bestQ = ques; 
			}
		}
		//System.out.println("hre"+bestQ);
		return bestQ; 
	}

	/**Determines if there are any unasked questions remaining
	 * @return true if there is at least one unasked questions, false otherwise
	 */
	public boolean hasMoreQuestions() {
		//make this the new list 
		if(this.qMod.size() >  0) {
			return true; 
		}
		else {
			return false; 
		}
	}

	/**
	 * Determines if there are multiple options remaining
	 * @return true if there are 2 or more possible choices remaining, false
	 * otherwise
	 */
	public boolean multiplePossibilitiesRemain() {
		//make this the changing list 
		if(this.movies.size() < 2 ) {
			return false; 
		}
		else {
			return true; 
		}
	}

	/**
	 * Returns the options that are still possible
	 * based on the responses to earlier questions
	 * @return the list of options remaining
	 */
	public List<String> getRemainingOptions() {
		//make this the changing list 
		return this.movies; 
	}
	
	/**
	 * Resets the decision maker so that all options are still possible
	 * and no questions have been answered
	 */
	public void reset() {
		// loop through the unchanged movie list and add those items back into the changed list. make sure to clear first. 
		//same for the questions list. 
		this.movies.clear();
		this.qMod.clear();
		//use for each loop for hashmap  
		for (Entry<String, Movies> hm : this.movieOptions.entrySet()) {
			String key = hm.getKey(); 
			this.movies.add(key); 
		}
		//System.out.println(this.questions);
		for (int i = 0; i < this.questions.size(); i++) {
			String q = this.questions.get(i); 
			this.qMod.add(q); 
			
		}
		//System.out.println(this.qMod);
		//System.out.println(this.movies);
	}
}
