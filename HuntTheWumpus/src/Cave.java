import java.util.ArrayList;

public class Cave {
	private String caveName; 
	private int caveNum; 
	private ArrayList<Integer> adjCaves; 
	private boolean caveVisited; 
	private CaveContents caveCont; 
	
	 
	public Cave (String name, int num, ArrayList<Integer> adj){
		this.caveName = name; 
		this.caveNum = num; 
		this.adjCaves = adj; 
		this.caveVisited = false; 
		this.caveCont = CaveContents.EMPTY; 
		
		
	}
	
	public int getCaveNumDownTunnel(int tunnelNum){
		if (tunnelNum <=0 || tunnelNum > this.adjCaves.size()) {
			return -1; 
		}
		else {
			int num = this.adjCaves.get(tunnelNum-1); 
			return num; 
		}
		
	}
	
	public String getVisibleName() {
		if (this.caveVisited) {
			return this.caveName; 
		}
		return "cave name is unknown";
	}
	
	public int getCaveNumber() {
		return this.caveNum; 
	}
	
	public CaveContents getContents() { 
		return this.caveCont; 
	}
	
	public int getNumTunnels() {
		return this.adjCaves.size(); 
	}
	
	public void markAsVisited() {
		this.caveVisited = true; 
	}
	
	public void setContents(CaveContents contents) {
		this.caveCont = contents;
	}
}
