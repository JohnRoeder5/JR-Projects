import java.util.Scanner;

/**
 * DO NOT MODIFY THIS CLASS 
 * Provides a terminal for playing Hunt the Wumpus
 */
public class WumpusTerminal {
    public static void main(String[] args) throws java.io.FileNotFoundException {
        CaveMaze maze = new CaveMaze("caves.txt");

        System.out.println("HUNT THE WUMPUS:  Your mission is to explore the maze of caves");
        System.out.println("and capture all of the wumpi (without getting yourself mauled).");
        System.out.println("To move to an adjacent cave, enter 'M' and the tunnel number (e.g. M 2).");
        System.out.println("To toss a stun grenade into a cave, enter 'T' and the tunnel number (e.g. T 2).");

        Scanner input = new Scanner(System.in);
        while (maze.stillAble() && maze.stillWumpi()) {
            System.out.println("\n"+maze.showLocation());

            try {
                String action = input.next();
                if (action.toLowerCase().charAt(0) == 'q') {
                    System.out.println("You can't win if you don't play.");
                    break;
                }
                if (action.toLowerCase().charAt(0) == 't') {
                    System.out.println(maze.toss(input.nextInt()));
                } else if (action.toLowerCase().charAt(0) == 'm') {
                    System.out.println(maze.move(input.nextInt()));
                } else {
                    System.out.println("Unrecognized command -- please try again.");
                }
            }
            catch (java.util.InputMismatchException e) {
                System.out.println("Unrecognized command -- please try again.");
            }
        }
        System.out.println("\nGAME OVER");
    }
}
