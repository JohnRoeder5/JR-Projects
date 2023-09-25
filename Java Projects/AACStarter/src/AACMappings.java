import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;

public class AACMappings {
	private Map<String, AACCategory> maps;  
	//field to keep track of category. 
	//should this field be String or of type AACCategory ?
	private AACCategory curr; 
	
	public AACMappings(String filename) {
		// initialize this.curr somehow?
		this.curr = null; 
		this.maps = new HashMap<String, AACCategory>(); 
		try {
			String text = "";  
			String imageLoc="";  
			Scanner scan = new Scanner(new File(filename));
			while (scan.hasNextLine()){
				String line  = scan.nextLine();
				int spaceIndex = line.indexOf(" ");
					imageLoc = line.substring(0, spaceIndex); 
					text = line.substring(spaceIndex);  
					
					//is the imageLoc the same thing as the current category we are trying to track?
					System.out.println(imageLoc);
					System.out.println(text);
					if(imageLoc.charAt(0)== '>') {
						imageLoc = imageLoc.substring(1); 
					}
					else {
						this.curr = null; 
					}
					add(imageLoc, text);	
			}
			this.curr = null; 
		}
			
		 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	/**
	 * adds mapping to the current category also creates a new category
	 * parameters - imageLoc and text 
	 */
	public void add(String imageLoc, String text) {
		//text is not of the type AACCategory
		if(this.curr != null) {
			this.curr.additem(imageLoc, text); 
			 
		}
		else {
		//create the category of type AACC to put into the AACM
			//set this.keepTrack = to below?
			//AACCategory newHash = new AACCategory(this.getCurrentCategory());
			//newHash.additem(imageLoc, text);
			//this.maps.put(imageLoc, newHash);
			this.curr = new AACCategory(text); 
			this.maps.put(imageLoc, this.curr); 
		}
	}
	
	/**
	 * gets the current category
	 * @return the name of the curret category. or returns the empty string. 
	 */
	public String getCurrentCategory(){
		//return current page or empty string if page is empty
		//as of right now this.curr.getCAtegory is null and wont work
		
		//maybe use isCategory here?
		if(this.curr !=null) { 
			//reset this.curr if has image is empty?
			String name = this.curr.getCategory();
			return name;
		}	 
		else {
			return "";  
		}
	}
	
	
	/**
	 * provides an array of the images in the current category
	 * @return an array of the image locations
	 */
	public String[] getImageLocs(){
		 //returns an array of images for the currently selected category or the home page 
		
		if(this.curr != null) {
			String[] images = this.curr.getImages(); 
			return images; 
		}
		else {
			int len = this.maps.size(); 
			String[] images = new String[len]; 
			int index = 0; 
			for (String image : this.maps.keySet()) {
				images[index] = image; 
				index++; 
			}
			return images; 
		}
		
	}
	
	/**
	 * determines the text to speak 
	 * @param imageLoc
	 * @return returns the text to speak
	 */
	public String getText(String imageLoc) {
		//if on the home screen return name of category and update this.curr to be that category
		
		 if(this.curr != null) {
			return this.curr.getText(imageLoc); 
		 }
		 else {
			 this.curr = this.maps.get(imageLoc); 
			 return this.curr.getCategory(); 
		 }
		
	}
	
	/**
	 * determines if the image represents a category or a text to speak 
	 * @param imageLoc
	 * @return returns true or false 
	 */
	public boolean isCategory(String imageLoc) {
		if(this.maps.keySet().contains(imageLoc)) {
			//then it is a category since it is in the set of keys 
			return true; 
		}
		else {
			//not a category is text
			return false; 
		}
	}
	
	
	/**
	 * resets current category back to the default category
	 */
	public void reset() {
		this.curr = null; 
	}
	
	
	/**
	 * writes the AAC mappings stored to a file
	 * @param filename
	 */
	public void writeToFile(String filename) {
		String write = "";  
		for(String catImages : this.maps.keySet()) {
			// write 
			this.curr = null; 
			 write += catImages + " " + getText(catImages) + ("\n"); 
			 System.out.println(catImages);
			for (String images : getImageLocs()) {
				write += ">" + images+ " "+ getText(images) + ("\n"); 
				System.out.println(images);
			}
			
		}
		try {
			FileWriter file = new FileWriter(new File(filename));
			file.write(write);
			this.reset(); 
			file.close(); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}