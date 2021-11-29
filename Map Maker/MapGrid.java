/**
 * Map Grid Class 
 * 
 */
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;

public class MapGrid extends GridPane
{
    //default blank image
    Image defaultImage = new Image("Tiles/KeyItemTile.png");
    
    //Constructor 
    public MapGrid(int rowNum, int colNum)
    {
        for(int row = 0; row < rowNum; row++)
        {
            for(int col = 0; col < colNum; col++)
            {
                MapGridTile mapTile = new MapGridTile(defaultImage);
                this.add(mapTile, rowNum, colNum);
            }
        }
    }
}
