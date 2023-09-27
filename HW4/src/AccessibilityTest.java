
public class AccessibilityTest {
	private String category; 
	private String googleResult; 
	private String waveResult;
	private String sortsiteResult; 
	private String aslintResult; 
	private String tDescription; 

	/**
	 * Constructor for the fields to be used 
	 * @param category
	 * @param googleResult
	 * @param waveResult
	 * @param sortsiteResult
	 * @param aslintResult
	 * @param tDescription
	 */
	public AccessibilityTest(String category, String googleResult, String waveResult, String sortsiteResult, String aslintResult, String tDescription) {
		this.category = category; 
		this.googleResult = googleResult; 
		this.waveResult = waveResult; 
		this.sortsiteResult = sortsiteResult; 
		this.aslintResult = aslintResult; 
		this.tDescription = tDescription; 
	}
	
	/**
	 * accessor for category
	 * @return
	 */
	public String getCategory(){
		return category; 
	}
	
	/**
	 * accessor for result of google test
	 * @return
	 */
	public String getGoogleR() {
		return googleResult;
	}
	
	/**
	 * accessor for result of the wave checker test
	 * @return
	 */
	public String getWave() {
		return waveResult; 
	}
	
	/**
	 * accessor for the result of the sortsite checker test 
	 * @return
	 */
	public String getSortSite() {
		return sortsiteResult; 
	}
	
	/**
	 * accessor for the aslint checker test
	 * @return
	 */
	public String getAslint() {
		return aslintResult; 
	}
	
	/**
	 * accessor for the test description
	 * @return
	 */
	public String getTdescription() {
		return tDescription;
	}
	
	/**
	 * converts to string format
	 */
	public String toString() {
		return this.category + ":"+tDescription+ ", Google: "+ googleResult + ", WAVE: "+waveResult+", SortSite: "+sortsiteResult+", ASLint: "+aslintResult+"."; 
	}
}
