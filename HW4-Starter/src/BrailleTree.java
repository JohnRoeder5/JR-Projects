import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * This class stores braille characters in a binary tree. 
 * @author Catie Baker
 *
 */
public class BrailleTree { 
	private BrailleNode root; 
 
	/**
	 * Creates the Braille tree from the alphabet in the provided file.
	 * The file should be formated, one braille character per line, with
	 * first the braille encoding as a series of six bits representing the
	 * six dots in a top to bottom, left to right order. Then after the encoding
	 * there will be a space followed by the text that braille character
	 * represents. In addition, it adds the all 0 encoding (000000 for six dot braille)
	 * as a space.
	 * @param filename the name of the file that stores the encoding mapping
	 */
	public BrailleTree(String filename) { 
		this.root = null ; 
		try {
			Scanner infile= new Scanner(new File(filename));
			while (infile.hasNextLine()) {
				String binary = infile.next(); 
				String text = infile.nextLine().trim(); 
				this.add(binary, text); 
			} 	 
			this.add("000000", " "); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Adds the braille character with the provided binary 
	 * encoding to the tree with the provided text. If there is
	 * already something with that encoding in the tree, it replaces
	 * that text. 
	 * @param binary the braille encoding of the character
	 * @param text the text that the character represents
	 */
	public void add(String binary, String text) {
		this.root = this.add(binary, text, this.root); 
	}
	
	
	/**
	 * Adds the braille character with the provided binary 
	 * encoding to the tree with the provided text. If there is
	 * already something with that encoding in the tree, it replaces
	 * that text. 
	 * @param binary the braille character that the text is associated with
	 * @param text the text to add to the tree
	 * @param curr the subtree to add the text to
	 * @return the node that is the root of the subtree
	 */
	private BrailleNode add(String binary, String text, BrailleNode curr) {
		int len = binary.length(); 
		if(curr == null) {
			curr = new BrailleNode("");
		}  
		if(!binary.equals("")){
			if (binary.charAt(0) == '0') {
				curr.setLeft(this.add(binary.substring(1), text, curr.getLeft()));
			}        
			else {
				curr.setRight(this.add(binary.substring(1), text, curr.getRight()));
			}
		}
		else {
			curr.setText(text);
		}
		return curr;
	}


	/**
	 * Gets the text associated with the provided braille character
	 * @param binary the binary encoding of the braille character
	 * @return the text associated with the braille character or
	 * the empty string if there is no such encoding in the tree
	 */
	public String getText(String binary) {
		return this.getText(binary, this.root); 
	}
	
	/**
	 * Gets the text associated with the provided braille character
	 * @param binary the binary encoding of the braille character
	 * @return the text associated with the braille character or the
	 * empty string if there is no such encoding in the tree
	 */
	private String getText(String binary, BrailleNode curr) {  
		String sub = ""; 
		if (curr == null) {
            return "";
        }
        else if (binary.equals("")) {
            return curr.getText();
        }
		
        else if (binary.charAt(0) == '0') {
        	 
        	
        	return this.getText(binary.substring(1), curr.getLeft());
        }
		
        else {

        	return this.getText(binary.substring(1), curr.getRight());
        }
		
	}
	
	/**
	 * Finds and returns the braille encoding for the provided text
	 * @param text the text to find the braille encoding of
	 * @return the binary braille encoding that has that text or 
	 * the empty string if that text is not in the tree.
	 */
	public String getBraille(String text) {
		String path = ""; 
		return this.getBraille(text, this.root, path); 
	
	}
	
	/**
	 * Finds and returns the braille encoding for the provided text
	 * @param text the text to find the braille encoding of
	 * @param curr the current node that you are checking
	 * @param path the binary encoding the represents the path 
	 * to the current node 
	 * @return the binary braille encoding that has that text or 
	 * the empty string if that text is not in the tree.
	 */
	private String getBraille(String text, BrailleNode curr, String path) {
		if (curr == null) {
            return "";
        }
        else if (text.equals(curr.getText())) {
            return path;
        }
		String leftResult =  this.getBraille(text, curr.getLeft(), ""); 
        String rightResult = this.getBraille(text, curr.getRight(), "");
          if(leftResult.charAt(0) == '0') {
        	  System.out.println(path);
        	  return path+"0"; 
          }
          else if(rightResult.charAt(0)== '1') {
        	 
        	  return path +"1"; 
          }
          return path; 
		
	}
	
	/**
	 * Given a file written in braille, it translates it to the
	 * text provided in the tree and writes it to a new file, outfile.
	 * @param infile the braille file to translate
	 * @param outfile the file to write the translation to
	 */
	public void translateFile(String infile, String outfile) {
		try { 
			String sub ;  
			String encoding; 
			FileWriter file = new FileWriter(new File(outfile));
			Scanner scan = new Scanner(new File(infile));
			while(scan.hasNextLine()) {
				String word = ""; 
				encoding = scan.nextLine(); 
				for (int i = 0; i< encoding.length(); i+=6) {
					sub = encoding.substring(i, i+6); 
					word = word + getText(sub);
				}
				file.write(word + "\n");
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
	}

	
	/**
	 * Class representing a node in a BrailleTree. Nodes without
	 * associated text (e.g. inner nodes) should store the empty string
	 * @author Catie Baker
	 */
	private class BrailleNode {
		private String text;
		private BrailleNode left;
		private BrailleNode right;
		
				
		/**
		 * Creates a BrailleNode with the provided text value
		 * @param data the text to store in the node
		 */
		public BrailleNode(String data) {
			this.text = data;
			this.right = null;
			this.left = null;
		}

		/**
		 * Gets the text stored in the node
		 * @return the text stored in the node
		 */
		public String getText() {
			return text;
		}

		/**
		 * Sets the text stored in the node
		 * @param data the text to store in the node
		 */
		public void setText(String data) {
			this.text = data;
		}

		/**
		 * Gets the left child of the node
		 * @return the left child of the node
		 */
		public BrailleNode getLeft() {
			return left;
		}
		
		/**
		 * Sets the left child of the node
		 */
		public void setLeft(BrailleNode left) {
			this.left = left;
		}

		/**
		 * Gets the right child of the node
		 * @return the right child of the node
		 */
		public BrailleNode getRight() {
			return right;
		}

		/**
		 * Sets the right child of the node
		 */
		public void setRight(BrailleNode right) {
			this.right = right;
		}	
	}
}
