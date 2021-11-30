/**
 * Map Grid Class 
 * 
 */
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import java.util.ArrayList;

public class MapGrid extends GridPane
{
    //default blank image
    Image defaultImage = new Image("Tiles/!EraseTile.png");
    
    //array storing all MapGridTiles
    ArrayList<MapGridTile> allMapTiles = new ArrayList<MapGridTile>();
    
    //Constructor 
    public MapGrid(int rowNum, int colNum)
    {
        this.setAlignment(Pos.CENTER);
        for(int row = 0; row < rowNum; row++)
        {
            for(int col = 0; col < colNum; col++)
            {
                MapGridTile newMapTile = new MapGridTile(defaultImage);
                this.add(newMapTile, row, col);
                allMapTiles.add(newMapTile);
            }
        }
    }
    
    ArrayList<MapGridTile> getAllMapTiles() 
    {
        return allMapTiles;
    }
}
