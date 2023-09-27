
public class SentimentsClass {
	private String word; 
	private int positiveCount;
	private int negativeCount;
	private int neutralCount;  
	 
	 
	
	public SentimentsClass(String nextWord) {
		this.word = nextWord; 
		this.positiveCount = 0;
		this.negativeCount = 0; 
		this.neutralCount = 0; 
		
	}
	
	public String getWord() {
		return word; 
	}

	public int getNegative() {
		return negativeCount;
	}
	
	public int getPositive() {
		return positiveCount; 
	}
	
	public int getNeutral() {
		return neutralCount; 
	}
	
	public void increment(String sentiScore) {
		if (sentiScore.contains("3") || sentiScore.contains("4")) {
			this.positiveCount++; 
		}
		else if (sentiScore.contains("2")) {
			this.neutralCount++; 
		}
		else {
			this.negativeCount++; 
		}
	} 
	
	
	
	
}
