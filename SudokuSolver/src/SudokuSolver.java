import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuSolver {
	private int [][] grid; 
	public static final int GRID_SIZE = 9; 

	public SudokuSolver(String filename) {
		try {
			Scanner infile = new Scanner(new File(filename));
			this.grid = new int [GRID_SIZE][GRID_SIZE];
			for(int row = 0; row<GRID_SIZE; row++) {
				String line = infile.nextLine(); 
				for (int col = 0; col< GRID_SIZE; col++) {
					char val = line.charAt(col); 
					if (val == '-') {
						this.grid[row][col] = 0; 
					}
					else {
						this.grid[row][col] = Integer.parseInt(val+""); 
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean solve(int row, int col) {
		if (row == GRID_SIZE) {
			return true; 
		}
		else if (col == GRID_SIZE) {
			return solve(row+1, col); 
		}
		else if(this.grid[row][col] != 0) {
			return solve(row, col+1); 
		}
		for (int i = 1; i<=9; i++) {
			if(canPlace(i, row, col)) {
				this.grid[row][col] = i; 
				if(solve(row, col+1)) {
					return true; 
				}
				this.grid[row][col] = 0; 
			}
		}
		return false; 
	}
	
	private boolean canPlace(int val, int row, int col) {
		return checkRow(val, row) && checkCol(val, col) && checkSquare(val, row, col); 
	}
	
	private boolean checkRow(int val, int row) {
		for(int col = 0; col<GRID_SIZE; col++) {
			if(this.grid[row][col] == val) {
				return false ; 
			}
		}
		return true; 
	}
	
	private boolean checkCol(int val, int col) {
		for(int row = 0; row<GRID_SIZE; row++) {
			if(this.grid[row][col] == val) {
				return false ; 
			}
		}
		return true ; 
	}
	
	private boolean checkSquare(int val, int row, int col) {
		int squareSize =(int) Math.sqrt(GRID_SIZE); 
		int baseRow= squareSize * (row/squareSize); 
		int baseCol = squareSize * (col/squareSize);
		for (int r = baseRow; r < baseRow+squareSize; r++) {
			for (int c = baseCol; c<baseCol + squareSize; c++) {
				if (this.grid[r][c] == val) {
					return false; 
				}
			}
		}
		return true; 
	}
	
	public String toString() {
		String s = ""; 
		for(int row = 0; row<GRID_SIZE; row++) {
			for (int col = 0; col< GRID_SIZE; col++) {
				s += this.grid[row][col]; 
			}
			s+= "\n";
		}
		return s; 
	
	}
	
}
