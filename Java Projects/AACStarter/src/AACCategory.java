
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class AACCategory {
	private HashMap<String, String> categories;
	private String name; 
	
	public AACCategory(String name) { 
		this.name = name; 
		this.categories = new HashMap< String, String >(); 
	}
	
	/**
	 * adds the items to the category in the hashmap
	 * @param imageLoc
	 * @param text
	 */
	public void additem(String imageLoc, String text) {
		this.categories.put(imageLoc, text); 
	}
	
	/**
	 * gets the categorys name 
	 * @returns the name of the category
	 */
	public String getCategory() {
		//do we want to extract the actual name out of the this.curr field. the field 
		//is currently holding the whole image location
		if(this.name.isEmpty() == true) {
			return this.name = "";
		}
		else {
			return this.name; 
		}
	}
	
	/**
	 *  gets an array of all the images in a category
	 * @returns an array of all the images in the category
	 */
	public String[] getImages() {
		int len = this.categories.size(); 
		String[] ims = new String[len]; 
		int index = 0; 
		for (String image : this.categories.keySet()) {
			ims[index] = image; 
			index++; 
		}
			System.out.println(ims.toString());
			return ims; 
	}
	
	/**
	 * returns the text associated with the image loc in this category
	 * @return
	 */
	public String getText(String imageLoc) {
		return this.categories.get(imageLoc); 
	}
	
	/**
	 * determines if image is in a given category
	 * @return
	 */
	public boolean hasImage(String imageLoc) {
		if(this.categories.containsKey(imageLoc)) {
			return true;
		}
		else {
			return false; 
		}
	}
}
