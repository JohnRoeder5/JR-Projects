import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/**
 * Incomplete class that models a maze of caves for the "Hunt the Wumpus" game.
 *   @author Catie Baker
 */
public class CaveMaze {
  private Cave currentCave;
  private ArrayList<Cave> caves;
  private int wumpies;
  private int bombs;   
  private boolean wumpiesAlive; 
  private boolean stillAlive;
   
  
  /**
   * Constructs a CaveMaze from the data found in a file.
   *   @param filename the name of the cave data file
   */
  public CaveMaze(String filename) throws java.io.FileNotFoundException {
      Scanner infile = new Scanner(new File(filename));
      
      int numCaves = infile.nextInt();
      this.caves = new ArrayList<Cave>();
      for (int i = 0; i < numCaves; i++) {
          this.caves.add(null);
        }
      
      for (int i = 0; i < numCaves; i++) {
          int num = infile.nextInt(); 
          int numAdj = infile.nextInt(); 
          ArrayList<Integer> adj = new ArrayList<Integer>();
          for (int a = 0; a < numAdj; a++) {
              adj.add(infile.nextInt());
          }
          String name = infile.nextLine().trim();          
          this.caves.set(num, new Cave(name, num, adj));
      }
      //gets current cave youre in and sets to visited 
      this.currentCave = this.caves.get(0);
      this.currentCave.markAsVisited();
      // gets 1 to 3 wumpi and places them in caves using the helpercave method
      Die wumpyDie = new Die (3); 
      this.wumpies = wumpyDie.roll(); 
      for (int i = 0 ; i<= this.wumpies; i++) {
    	  this.helperCaveFinder(CaveContents.WUMPUS); 
      }
      //sets the number of bombs 
      this.bombs = 3 * this.wumpies; 
      //sets the pit and the bat swarm in a random empty cave. 
      this.helperCaveFinder(CaveContents.BATS);
      this.helperCaveFinder(CaveContents.PIT);
      //sets you being alive to true
      this.stillAlive = true; 
      //sets the wumpi being alive to true 
      this.wumpiesAlive = true; 
  }
  
  /**
   * finds the caves that have nothing in them. used to place wumpi, bats, and pit
   * @param contents takes the contents of the cave as the parameter to 
   */
  public void helperCaveFinder (CaveContents contents) {
	  Die cave = new Die(20); 
	  int totCave = cave.roll()-1; 
	  Cave whatCave = this.caves.get(totCave);
	  while (!(whatCave.getContents().equals(CaveContents.EMPTY))) {
		  totCave = cave.roll()-1; 
		  whatCave = this.caves.get(totCave); 
	  }
	  whatCave.setContents(contents); 
  }
  
  
  
  /**
   * Moves the player from the current cave along the specified tunnel, marking the new cave as visited.
   *   @param tunnel the number of the tunnel to be traversed (1-3)
   */
  public String move(int tunnel) {
      if (tunnel < 1 || tunnel > this.currentCave.getNumTunnels()) {
          return "There is no tunnel number " + tunnel;
      }
      int caveNum = this.currentCave.getCaveNumDownTunnel(tunnel);
      this.currentCave = this.caves.get(caveNum);
      this.currentCave.markAsVisited();
      Die die = new Die(20); 
	  int dieroll = die.roll();
      if (this.currentCave.getContents().equals(CaveContents.WUMPUS)) {
    	  this.currentCave= this.caves.get(dieroll); 
    	  System.out.println("You are moving down tunnel number "+tunnel+"...");
    	  this.stillAlive = false; 
    	  return "You have been mauled to death by a wumpus."; 
      }
      if (this.currentCave.getContents().equals(CaveContents.PIT)) {
    	  this.stillAlive= false; 
    	  System.out.println("moving down tunnel number "+tunnel+"...");
    	  return "you have fallen in a pit"; 
      }
      if (this.currentCave.getContents().equals(CaveContents.BATS)) {
    	  System.out.println("moving down tunnel number "+tunnel+"...");
    	  return "A swarm of bats carried you away!"; 
      }
      return "Moving down tunnel " + tunnel + "...";
  }
  
  /**
   * Attempts to toss a stun grenade into the specified tunnel, but currently no grenades.
   *   @param tunnel the number of the tunnel to be bombed. 
   */
  public String toss(int tunnel) {
	  while (this.bombs > 0 && (this.stillAble() == true)) {
		  int caveWant = this.currentCave.getCaveNumDownTunnel(tunnel); 
		  if(this.caves.get(caveWant).getContents().equals(CaveContents.WUMPUS)) {
			  this.caves.get(caveWant).setContents(CaveContents.EMPTY);
			  this.bombs -= 1; 
			  this.wumpies -= 1; 
			  if (this.wumpies <= 0) {
				  this.wumpiesAlive = false; 
				  System.out.println("You've captured all the wumpi!");
			  }
			  return "The wumpus was captured"; 
		  }
		  else {
			  this.bombs -= 1; 
			  return "you missed! the wumpus was not there.";
		  }
	  }
	  this.stillAlive = false; 
	  return "You have no stun grenades to throw! You lose!";
  }  
  
  /**
   * Displays the current cave name and the names of adjacent caves. Caves that have not yet been 
   * visited are displayed as "unknown".  
   */
  public String showLocation() {
    String message = " You are currently in " + this.currentCave.getVisibleName();
    boolean wumps = false; 
    for (int i = 1; i<= this.currentCave.getNumTunnels(); i++) {
		  Cave getCave = this.caves.get(currentCave.getCaveNumDownTunnel(i)); 
		  if (getCave.getContents().equals(CaveContents.BATS)) {
			  message += " You hear the flapping of wings close by"; 
		  }
		  if (getCave.getContents().equals(CaveContents.PIT)) {
			  message += " you feel a draft coming from one of the tunnels"; 
		  }
		  if (getCave.getContents().equals(CaveContents.WUMPUS) && !wumps) {
			  message += " You smell an awful stench nearby"; 
			  wumps = true; 
		  }
		  
		  
    }
	for (int i = 1; i <= this.currentCave.getNumTunnels(); i++) {
	    int caveNum = this.currentCave.getCaveNumDownTunnel(i);
	    Cave adjCave = this.caves.get(caveNum);
	    message += "\n    (" + i + ") " + adjCave.getVisibleName();
	}
	return message;
  }
  
  /**
   * Reports whether the player is still able (healthy and mobile).
   *   @return true Used to test if the player is still alive. If not then game over 
   */
  public boolean stillAble() {
      return this.stillAlive;
  }

  /**
   * Reports whether there are any wumpi remaining.
   *   @return true. used to check if all the wumpuses are alive.  
   */
  public boolean stillWumpi() {
      return this.wumpiesAlive;
  }
}