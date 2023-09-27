import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class SentimentsCalculator {
	private ArrayList<SentimentsClass> sentiments; 

	/**
	 * Creates an object that can be used to determine the sentiments of 
	 * words and phrases based on a set of training data.
	 * @param filename the name of the file that contains the training data
	 * to determine the sentiments of different words
	 * @throws FileNotFoundException 
	 */
	public SentimentsCalculator(String filename) { 
		this.sentiments = new ArrayList<SentimentsClass>();   
		String nextWord; 
		int index; 
		try {
			Scanner infile = new Scanner(new File(filename));
			while (infile.hasNextLine()) {
				String sentimentScore = infile.next();
				String line = infile.nextLine().toLowerCase();
				String[] sentimentPhrase = line.split(" ") ; 
				for (int i = 1; i < sentimentPhrase.length; i++ ) {
					nextWord = sentimentPhrase[i];
					nextWord = noPunctuation(nextWord);  
					if(nextWord  == null) {
						continue; 
					}
					index = this.search(nextWord); 
					if (index >=0) {
						//is it getting a word or an int index
						//also increment is not incrementing 
						this.sentiments.get(index).increment(sentimentScore); 
					}

					else {
						//add the new word to the list  
						SentimentsClass results = (new SentimentsClass(nextWord)); 
						results.increment(sentimentScore );
						this.sentiments.add(results); 
					}
				}
			}	 

		}	

		catch (FileNotFoundException e) {
			System.out.println("Error");
		}	
	}
	/**
	 * Looks up the next word read in from the file in the larger list. if the word is 
	 * in the list it will return its index. if not then it will return -1
	 * @param lookup - this is the word read in that is being looked up on the list
	 * @return returns an index or -1
	 */
	public int search(String lookup) { 
		for (int i = 0; i < this.sentiments.size(); i++ ) {
			if (this.sentiments.get(i).getWord().equals(lookup)) {
				return i;
			}
		}
		return -1;

	}

	/**
	 * Takes a word and then eliminates all unnecessary punctuation from that word. 
	 * @param nextWord - the word that is being checked for unnecessary punctuation. 
	 * @return returns a word with the eliminated unnecessary punctuation. 
	 */
	private String noPunctuation(String nextWord) { 
		String newWord = null;
		char endString; 
		String word = nextWord.toLowerCase(); 
		for (int i = 0; i < word.length(); i++) {
			char val = word.charAt(i);
			if(Character.isLetter(val)) {
				endString = word.charAt(word.length()-1);
				if(Character.isLetter(endString)) {
					return word ; 
				}
				else {
					for (int j = word.length()-1; j<=0; j--) {
						char LastVal = word.charAt(j); 
						if (Character.isLetter(LastVal)) {
							return word.substring(i, j+1) ; 
						}
					}

				}
			}	
		}
		return newWord; 
	}

	/**
	 * Determines the sentiments of the provided text by analyzing the 
	 * sentiments of the individual words. The text can be determined to 
	 * be positive, negative, or neutral/unknown.
	 * @param phrase the word or phrase to determine the sentiments of
	 * @return a positive number if the sentiments are positive, 
	 * 		   a negative number if the sentiments are negative,
	 * 		   zero if the sentiments are neutral or unknown
	 */
	public int sentimentScore(String phrase) {
		String[] sentence = phrase.split(" ");
		String words;  
		int wordScore = 0; 
		int phraseScore = 0; 
		for (int i = 0; i < sentence.length; i++) {
			words = sentence[i]; 
			words = noPunctuation(words); 
			wordScore = this.sentimentWordScore(words); 
			phraseScore += wordScore; 
		}
		return phraseScore; 
	}

	

	/**
	 * Determines the sentiments of the provided word by analyzing the 
	 * frequency that the word appears in a positive, negative or neutral
	 * review. The text can be determined to be positive, negative, 
	 * or neutral/unknown.
	 * @param word the word to determine the sentiments of
	 * @return a positive number if the sentiments are positive, 
	 * 		   a negative number if the sentiments are negative,
	 * 		   zero if the sentiments are neutral or unknown
	 */
	public int sentimentWordScore(String word) {
		int pos = 0; 
		int neg = 0; 
		int neutral = 0; 
		int wordScore = this.search(word); 
		if (wordScore >= 0) {
			pos = this.sentiments.get(wordScore).getPositive(); 
			neg = this.sentiments.get(wordScore).getNegative();
			neutral = this.sentiments.get(wordScore).getNeutral(); 
			if(pos > neg) {
				if (pos > neutral) {
					wordScore = 1; 
				}
				else {
					wordScore = 0; 
				}
			}
			if(neg > pos) {
				if (neg > neutral) {
					wordScore = -1; 
				}
				else {
					wordScore = 0; 
				}
			}
			if(neutral > pos){
				if(neutral > neg) {
					wordScore = 0; 
				}
				else {
					wordScore = -1; 
				}
			}
		}
		else {
			System.out.println("word/phrase not found in reviews");
			wordScore = 0; 
		}
		//System.out.println(pos+" "+ neg+ " " + " " +neutral);
		return wordScore; 		
	}

}
