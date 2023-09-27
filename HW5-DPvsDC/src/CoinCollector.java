import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//ADD COMMENT HERE
public class CoinCollector {
private char grid[][]; 
private int colSize; 
private int rowSize; 
	
	//The grid above will be represented by a text file that has the number 
	//of rows in the grid on the first line, the number of columns on the second line, then one line per row with each column represented as a 
	//character, space for empty, a number for coin(s) and x for wall.
	public CoinCollector(String filename) {
		try {
			Scanner scan = new Scanner(new File(filename));
			//get row and col sizes
			this.rowSize = scan.nextInt(); 
			this.colSize = scan.nextInt(); 
			System.out.println(rowSize + " , " + colSize);
			
			//initialize the grid. fill in the mappings from the file
			this.grid = new char[rowSize][colSize];
			
			//grab rows and fill in grid position by position by col
			char val; 
			scan.nextLine(); 
			for (int row = 0; row< this.rowSize; row++) {
				String line = scan.nextLine();
				for (int col = 0; col< this.colSize; col++) {
					val = line.charAt(col); 
					if(val ==' ') {
						val = '0'; 
					}
					this.grid[row][col] = val; 
				}
			}
			System.out.println(this);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
	/**
	 *  Determines the maximum number of coins a robot can pick up on the way to their target location
	 *  using a top down approach.
	 *    @param startRow the initial row the robot starts in
	 *    @param startCol the initial column the robot starts in
	 *    @return the max number of coins that can be picked up on the way to the target destination, -1 
	 *    if it is not possible to reach the target destination from the starting point.
	 */
	public int findMaxCoinsTopDown( int startRow, int startCol)   {
		//Base Case: numCoins(row, col) = -1 if row, col is a wall or not in the grid (aka an invalid starting spot)
		//Base Case: numCoins(last row, last col) = num coins if there are coin(s) there, 0 otherwise
		//Recursive Case: if the starting location is valid,
		//numCoins(row,col) = max value of (numCoins(row+1,col) and numCoins(row, col+1)) 
		//then add num coins if there are coin(s) at row, col; if the starting location is not valid 
		//(aka it is not possible to reach the target destination from this spot), return -1  
		int coinCount =0; 
		if(startRow >= this.rowSize ||startCol >= this.colSize) {
			return -1;  
		}
		else if(this.grid[startRow][startCol] =='x') {
			return -1; 
		}
		//if the starting point is the last cell in the grid. count any coins in that cell  
		else if(startRow == this.rowSize-1 && startCol == this.colSize -1) {
			coinCount += Integer.parseInt(""+this.grid[startRow][startCol]); 
			return coinCount; 
		}
		//deals with x's 
		else {
			int goldDown =  findMaxCoinsTopDown(startRow+1 , startCol); 
			int goldRight = findMaxCoinsTopDown(startRow , startCol+1); 
			if(goldDown ==-1 && goldRight==-1) {
				return -1 ; 
			}

			//attempt to show visited cells  in grid
			//this.grid[startRow][startCol] = 'v'; 
			return coinCount = Integer.parseInt("" + this.grid[startRow][startCol]) + Math.max(goldDown, goldRight);
		}
	}
		
		
	
	
	/**
	 * used to viualize the grid made to make sure its created correctly. 
	 */
	public String toString() {
		String output = "";
		String r= " "; 
		int cl = 0; 
		//for row output
		
		output+= " " + r + "\n"; 
		output+= "  ----------" + "\n"; 
		//for grid output
		for(int row = 0; row<this.rowSize; row++) {
			//for col output 
			output += cl + "|"; 
			for (int col = 0; col< this.colSize; col++) {
				output += this.grid[row][col] ; 
			}
			output+= "|" + "\n";
		}
		output += "  ----------"; 
		return output; 
	}
	
	
	/**
	 *  Determines the maximum number of coins a robot can pick up on the way to their target location
	 *  using a top down approach.
	 *    @param startRow the initial row the robot starts in
	 *    @param startCol the initial column the robot starts in
	 *    @return the max number of coins that can be picked up on the way to the target destination, -1 
	 *    if it is not possible to reach the target destination from the starting point.
	 */
	public int findMaxCoinsBottomUp(int startRow, int startCol)   {
		//NOTE: while it may be more efficient to create the solution grid as a field
		//like we did with caching, part of the goal of this homework is to compare
		//the running times; so re-create the solution grid from scratch each time
		//in this method
		//initialize a table for the values. fill with 0's
		
		int coinCount = 0; 
		int[][] dyTable = new int[this.rowSize][this.colSize]; 
		for(int row =0; row< this.rowSize; row++) {
			for(int col = 0; col<this.colSize; col++) {
				dyTable[row][col] = 0; 
			}
		}

		//starting cases
		if(startRow >= this.rowSize ||startCol >= this.colSize) {
			System.out.println("start position invalid");
			return -1;  
		}
		else if(this.grid[startRow][startCol] =='x') {
			System.out.println("start position invalid there is an 'x' stationed here");
			return -1; 
		}
		else if(startRow<0 || startRow<0) {
			System.out.println("start position invalid");
			return -1; 
		}
		//else position is valid. 
		else {
			//start of for loops
			for(int c = this.colSize-1; c>= startCol; c--) {
				for(int r= this.rowSize-1; r>=startRow; r--) { 
					//if the starting point is the last cell in the grid. count any coins in that cell  
					//does this need to be r and c ???
					if(r == this.rowSize-1 && c == this.colSize -1) { 
						//this would be the max value since we are in the last cell 
						dyTable[r][c] = Integer.parseInt(""+this.grid[r][c]); 
					}
					//if position is an x 
					else if(this.grid[r][c]=='x') {
						dyTable[r][c] = -1; 
					}
					//else 
					else {
						int coinDown;
						if(r+1 < this.rowSize) {
							coinDown = dyTable[r+1][c]; 
						}  
						else {
							coinDown = -1 ; 
						}
						int coinRight;
						if(c+1 < this.colSize) {
							coinRight = dyTable[r][c+1];
						}
						else {
							coinRight = -1; 
						}
						//we are boxed in by x's
						if(coinDown == -1 && coinRight == -1) {
							dyTable[r][c] = -1 ;  
						}
						dyTable[r][c] = Integer.parseInt(""+this.grid[r][c])+ Math.max(coinRight, coinDown); 
						 
					}
				}
			}
			//return place that we are starting from. should have the max coins that we can find in that spot. 
			return dyTable[startRow][startCol];

		}
	}
}
