import java.awt.Font;

/**
 * Driver class for the keyboard-based virtual dulcimer.
 * 
 * @author John Roeder
 * @version 9-28-22
 * creates display with corresponding keys and notes
 */
public class DulcimerDriver {
   public static void main(String[] args) {
        String bassKeys = "a   s   d   f   g   h   j   k   l   ;   '";    
        String dashes = "--- --- --- --- --- --- --- --- --- --- ---";
        String bassNotes = "G-  A   B   C   D   E   F   G   A+ A#+  C+";
        
        String treble1keys = "1    2    3    4    5    6    7    8    9    0    -    ="; 
        String trebDash = "---  ---  ---  ---  ---  ---  ---  ---  ---  ---  ---";
        String treble1Notes = "D++  C++  B++  A++  G+  F#+  E+  D+  C#+  B+  A+  G#"; 
        
        String treble2keys = "q   w   e   r   t   y   u   i   o   p   [   ]"; 
        String treb2Dash = "---  ---  ---  ---  ---  ---  ---  ---  ---  ---  ---"; 
        String treble2Notes = "G+  F#+  E+  D+  C+  B+  A+  G  F#  E  D  C#"; 
         
        StdDraw.setFont(new Font("Monospaced", Font.PLAIN, 12));
        StdDraw.textLeft(0.00, 1.00, "DULCIMER KEY MAPPINGS");
        StdDraw.textLeft(0.00, 0.90, "        keys:  " + bassKeys);
        StdDraw.textLeft(0.00, 0.87, "BASS           " + dashes);
        StdDraw.textLeft(0.00, 0.84, "       notes:  " + bassNotes);
        
        StdDraw.textLeft(0.00, 0.78, "        keys:  " + treble1keys);
        StdDraw.textLeft(0.00, 0.75, "TREBLE 1       " + trebDash);
        StdDraw.textLeft(0.00, 0.72, "       notes:  " + treble1Notes);
        
        StdDraw.textLeft(0.00, 0.66, "        keys:  " + treble2keys);
        StdDraw.textLeft(0.00, 0.63, "TREBLE 2       " + trebDash);
        StdDraw.textLeft(0.00, 0.60, "       notes:  " + treble2Notes);
        
        String keys = (bassKeys+ " "+ treble1keys + " "+ treble2keys).replace(" ","");  
        
        Dulcimer dulc = new Dulcimer(bassNotes + " "+ treble1Notes + " "+ treble2Notes);
        while (true) { 
            if (StdDraw.hasNextKeyTyped()) {
                int typed = keys.indexOf(StdDraw.nextKeyTyped());
                dulc.hammer(typed);
            }
            dulc.play();
        }
    }    
}
