import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Battleship {
	private char grid[][];  
	private ArrayList<Integer> boatLens;
	private ArrayList<Integer> rowVals; 
	private ArrayList<Integer> colVals;
	private int gridSize;  
	
	
	/**
	 * initializing the grid. fills all the row/col indexes that have spaces in them with spaces as a placeholder
	 * for values that are unknown since only known values are B's and ~'s. Essentially creates the starting puzzle 
	 * with all known and unknown values. 
	 * Places B's in any row/col index that has one. Also collects the counts for the different sizes of boat segments
	 * stores them in a list with the 0th index being 4segment count, 1st being the 3 segment count and so on...
	 * Stores row and column values in separate lists 
	 * 
	 * @param filename - file with the starting battleship puzzle
	 */
	public Battleship(String filename) {
		try {
			Scanner scan = new Scanner (new File(filename));
			this.gridSize = scan.nextInt();
			this.grid = new char[this.gridSize][this.gridSize]; 
			this.boatLens = new ArrayList<Integer>(); 
			scan.nextLine(); 
			for (int i=0; i<4; i++) {
				String boatVal = scan.nextLine(); 
				int toAdd = Integer.parseInt(boatVal); 
				this.boatLens.add(toAdd);  
			}
			//System.out.println(this.boatLens);

			//add column values to a list 
			this.rowVals = new ArrayList<Integer>();
			this.colVals = new ArrayList<Integer>();
			for(int colVals = 0; colVals< this.gridSize; colVals++) {
				int colV = scan.nextInt(); 
				this.colVals.add(colV); 
			}
			scan.nextLine(); 
			//here create the layout for the grid by adding the known values. also grab rowVals. 
			for (int row =0; row < this.gridSize; row++) {
				String rowline = scan.nextLine(); 
				int colLine = Integer.parseInt(rowline.charAt(0) + ""); 
				//System.out.println(colLine);
				this.rowVals.add(colLine); 
				for (int col =0; col < this.gridSize; col++) {
					char value = rowline.charAt(col+1); 
					this.grid[row][col] = value;  
				}
			}
			//System.out.println(this.rowVals); 
			//System.out.println(this.colVals);
			//System.out.println(this);
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * helper method for the solve method. starts at the top left corner of the grid with 0,0
	 * @return -returns a boolean that tells whether or not the puzzle can be solved. 
	 */
	public boolean solve() {
		return this.solve(0,0);  
	}
	
	/**
	 * is used to solve the battleship puzzle through backtracking. 
	 * takes params the given row and column
	 * @param row - the row given
	 * @param col - the column given 
	 * @return - returns a boolean as to whether or not a problem can be solved 
	 */
	public boolean solve(int row, int col) {
		if(row == this.gridSize) {
			return true;
		}
		else if (col == this.gridSize) {
			return solve(row+1, 0); 
		}
		else if(this.grid[row][col] != ' ') {
			return solve(row, col+1); 
		}
			if(canPlace('B', row, col)) {
				this.grid[row][col] = 'B';
				System.out.println(this);
				if(solve(row, col+1)) {
					return true; 
				}
				this.grid[row][col] = ' '; 
			}
			if(canPlace('~', row, col)) {
				this.grid[row][col] = '~'; 
				System.out.println(this);
				if(solve(row, col+1)) {
					return true; 
				}
				this.grid[row][col] = ' '; 
			}
		return false; 
	}
	
	/**
	 * Contraints: 
	 * amount of B's in each row/col must be = to the rowvals, colvals given. 
	 * no B's can be adjacent to each other. 
	 * available spaces cannot be less than required B's for rows or columns
	 * need to be correct amounts of the given boat sizes
	 * uses checkRow, checkCol, checkSurroundings and findValidBoats
	 * @param val - the value to be placed
	 * @param row - the given row 
	 * @param col - the given column
	 * @return - returns true or false as to whether or not the value can be placed. 
	 */
	public boolean canPlace (char val, int row, int col) {
		//checkRow/Col should see if the value associated with each row/col
		//is valid for that row.
		boolean isdone = false; 
		if(row == this.gridSize-1  && col == this.gridSize-1) {
			isdone = true; 
		}
		//System.out.println(row +" "  +col + val);
		//System.out.println(checkRow(val, row));
		//System.out.println(checkCol(val, col));
		//System.out.println(checkSurroundings(val, row, col));
		//System.out.println(findValidBoats(isdone));
		return checkRow(val, row) && checkCol(val, col) && checkSurroundings(val, row, col) && findValidBoats(isdone);
	}
	
	
	/**
	 *  checks the rows and counts the amount of B's and spaces in each row. find whether or not a value can be placed in the cell 
	 *  by checking for the water constraint and by checking if a B can be placed 
	 * @param val - the value to be placed 
	 * @param row - the given row 
	 * @return - returns true or false as to if a value can be placed there 
	 */
	public boolean checkRow(char val, int row) {
		int rowLimit = this.rowVals.get(row); 
		int rowBCount = 0; 
		int rspaceCount = 0; 
		int cspaceCount = 0; 
		// B count plus space count needs to be > = r or c val 
		for(int col = 0; col < this.gridSize; col++ ) {
			if(this.grid[row][col] == 'B') {
				rowBCount++; 
			}
			if(this.grid[row][col] == ' ') {
				rspaceCount++; 
			}
		}
		//need row b count to be less than the row limit and
		//there need to be extra spaces in the row that can hold B's 
		//in order for one to be placed
		if(val =='B') {
			if(rowBCount < rowLimit) {
				return true; 
			}
		}
		if(val == '~') {
			//if its not >= then the Bcount is less than the required amount
			//so we need to see if there are enough spaces remaining for the B 
			//to be placed in
			if(rowBCount + rspaceCount > rowLimit) {
				return true; 
			}
		}
		return false;
		
	}

	/**
	 * checks the columns and counts the amount of B's and spaces in each column. finds out of the water can be placed or not
	 * as well as if a b can be placed or not. 
	 * @param val - the val to be placed 
	 * @param col - the column
	 * @return - returns true or false as to if a value can be placed there
	 */
	public boolean checkCol(char val, int col) {
		int colLimit = this.colVals.get(col); 
		int colBCount = 0; 
		int cspaceCount = 0;
		for (int row = 0; row< this.gridSize; row++) {
			if(this.grid[row][col] == 'B') {
				colBCount ++; 
			}
			if (this.grid[row][col] == ' ') {
				cspaceCount++; 
			}
		}
		//need row b count to be less than the row limit and
		//there need to be extra spaces in the row that can hold B's 
		//in order for one to be placed
		if(val =='B') {
			if(colBCount < colLimit) {
				return true; 
			}
		}
		if(val == '~') {
			//if its not >= then the Bcount is less than the required amount
			//so we need to see if there are enough spaces remaining for the B 
			//to be placed in
			if(colBCount + cspaceCount > colLimit) {
				return true; 
			}
			
		}
		return false; 
	}
	
	
	/**
	 * method to check for sizes of boats. need to do and if that will allow for the correct amount of
	 * boats w a certain length. lowercase b will be the placeholder where a B could go. will be restored
	 * in a later method. could this method be void
	 * @param row - given row 
	 * @param col - given col 
	 * @return - returns the size of consecuitive b's 
	 */
	public int boatSize(int row, int col) {
		int boatsize = 0; 
		int rCount = 0; 
		int cCount = 0; 
		if( row < 0 || col < 0 ||row >= this.grid.length || col >= this.grid.length || 
				this.grid[row][col] != 'B') {
			return 0; 
		}
		else {
			this.grid[row][col] = 'b'; 
				boatsize = 1+ this.boatSize(row-1, col) +
					this.boatSize(row, col-1) +
					this.boatSize(row, col+1) +
					this.boatSize(row+1, col); 
			return boatsize;
		} 
	}
	
	
	
	/**
	 * finds the connected b's and replaces them with B's. 
	 * also counts the segmented boats to make sure that the correct number of each segment value is present
	 * checks to see if there are any boats that are too large in the grid. 
	 * @param - isdone - boolean that will be either true or false depending onwhether or not the grid is done filling itself out
	 */
	public boolean findValidBoats(boolean isdone) {
		int oneSegment = 0 ;
		int twoSegment = 0; 
		int threeSegment = 0; 
		int fourSegment = 0; 
		int bigBoat =0; 
		int fourWanted = this.boatLens.get(0); 
		for (int row = 0; row< this.gridSize; row++) {
			for (int col = 0; col< this.gridSize; col++) {
				if(this.grid[row][col] == 'B') {
					int boatVal = this.boatSize(row, col); 
					if (boatVal ==0) {
						//do nothing
						boatVal+=0; 
					}
					else if (boatVal == 1) {
						oneSegment ++; 
					}
					else if(boatVal ==2) {
						twoSegment++; 
					}
					else if (boatVal == 3) {
						threeSegment++;
					}
					else if(boatVal ==4){
						fourSegment++; 
					}
					else if(boatVal >4) {
						bigBoat++; 
					}
				}
			}
		}
		//if not true then the series of boats is incorrect. 
		for (int row = 0; row < this.grid.length; row++) {
			for (int col = 0; col < this.grid.length; col++) {
				if(this.grid[row][col] == 'b') {
					this.grid[row][col] = 'B'; 
				}
			}
		}
		if(bigBoat>0) {
			return false; 
		}
		if(isdone == true) {
			if(oneSegment != this.boatLens.get(3) || 
					twoSegment != this.boatLens.get(2) || 
					threeSegment != this.boatLens.get(1) || 
					fourSegment != this.boatLens.get(0)) {
				return false; 
			}
		}
		
		else {
			// boat difference is boats wanted - boats have. 
			//works to cut off sooner
			if(fourSegment > this.boatLens.get(0)) {
				return false ; 
			}
			else if (threeSegment > this.boatLens.get(1)  + (this.boatLens.get(0) - fourSegment)) {
				return false; 
			}
			else if (twoSegment > this.boatLens.get(2)  + (this.boatLens.get(1)-threeSegment) + (this.boatLens.get(0) - fourSegment)) {
				return false; 
			}
			else if (oneSegment > this.boatLens.get(3) 
					+ (this.boatLens.get(2)- twoSegment)
					+ ((this.boatLens.get(1) - threeSegment)*2)
					+ ((this.boatLens.get(0) - fourSegment)*2)) {
				return false; 
			}
		}
		return true; 
	}
	
	/**
	 * if the row value or col value for the given cell is 0 then returns false, 
	 * if the cell has any Boats placed on adjacent corners then returns false. 
	 * calls method is valid to check if the row or col is valid to have a rows or cols up or down.
	 * (checks if the row is off the grid that is trying to be checked for adjacents. )
	 * @param val - the value at the give cell
	 * @param row - row for the cell
	 * @param col - column for the cell
	 * @return - returns either true or false based on the constraints 
	 */
	public boolean checkSurroundings(char val, int row, int col) {
		//row or col val == 0. checks if B's are even allowed in that row/col
		if(val =='B') {
			if( (this.rowVals.get(row) == 0 || this.colVals.get(col) == 0) ) {
				return false; 
			}
			//checks if B adjacent at the corners of the current [row][col]
			//if the cell we are checking is on the first row then there is no row-1
			//if the cell is on the last column there is no col+1
			//if the cell is on the first column there is no col-1
			//if the cell is on the last row then there is no row+1
			else if (isValid(row-1, col-1) && this.grid[row-1][col-1] == 'B'|| 
					isValid(row-1, col+1) && this.grid[row-1][col+1] == 'B'||
					isValid(row+1, col-1) && this.grid[row+1][col-1] == 'B'|| 
					isValid(row+1, col+1) && this.grid[row+1][col+1] == 'B') { 
				return false; 
			}
		}
		return true; 
	}
	
	/**
	 * checks to see if the row or column could possibly have an adjacent boat for checksurroundings. 
	 * checks if the column or row could possibly exist. 
	 * if row is -1 or larger than grid 
	 * if col is -1 or larger than grid 
	 * @param row - the given row 
	 * @param col - the given column
	 * @return
	 */
	public boolean isValid(int row, int col) {
		if (row >= this.gridSize || row <0 || col >=this.gridSize || col <0) {
			return false; 
		}
		else {
			return true; 
		}
	}
	
	/**
	 * to string writes the finished grid with the solved battleship puzzle.
	 */
	public String toString() {
		String output = "";
		String r= " "; 
		int cl = 0; 
		//for row output
		for(int i = 0; i<this.rowVals.size(); i++) {
			r += this.colVals.get(i); 
		}
		output+= " " + r + "\n"; 
		output+= "  ----------" + "\n"; 
		//for grid output
		for(int row = 0; row<this.gridSize; row++) {
			//for col output
			cl = this.rowVals.get(row); 
			output += cl + "|"; 
			for (int col = 0; col< this.gridSize; col++) {
				output += this.grid[row][col] ; 
			}
			output+= "|" + "\n";
		}
		output += "  ----------"; 
		return output; 
	}
}
