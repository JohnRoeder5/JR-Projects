import java.util.ArrayList;

public class Movies {
	private String movieName; 
	private ArrayList<String> qAnswers; 
	
	
	public Movies(String movie, ArrayList<String> ans) {
		this.movieName = movie; 
		this.qAnswers = ans; 
	}
	
	public ArrayList<String> getAns() {
		return this.qAnswers; 
		
	} 
	
	public String getMov() {
		return movieName; 
	}
	
}
