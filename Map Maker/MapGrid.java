/**
 * Map Grid Class 
 * 
 */
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.geometry.Pos;

public class MapGrid extends GridPane
{
    //default blank image
    Image defaultImage = new Image("Tiles/_EraseTile.png");
    
    //Constructor 
    public MapGrid(int rowNum, int colNum)
    {
        this.setAlignment(Pos.CENTER);
        for(int row = 0; row < rowNum; row++)
        {
            for(int col = 0; col < colNum; col++)
            {
                this.add(new MapGridTile(defaultImage), row, col);
            }
        }
    }
}
