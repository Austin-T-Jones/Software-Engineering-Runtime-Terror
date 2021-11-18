
/**
 * Map Grid Class 
 * 
 */
import java.awt.*;
import javax.swing.*;

public class MapGrid
{
    JFrame f;
    
    //Constructor contains a Jframe 
    public void MapGrid()
    {
        f = new JFrame();
    }
    
    //set mapsize takes only one integer as a parameter and sets n as the number of columns and rows
    //setSize - Resizes this component so that it has width w and height h. 
    //The width and height values are automatically enlarged if either is less than the minimum size as specified by previous call to setMinimumSize. 
    public void setMapSize(int n)
    {
        f.setLayout(new GridLayout(n,n));
        f.setSize(n*100, n*100);
        f.setVisible(true);
    }
    
    //to add into the frame Jframe has a simple add() method so to add a tile it would be something like f.add(tile)
}
