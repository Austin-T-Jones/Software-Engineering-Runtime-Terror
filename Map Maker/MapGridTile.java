//add image support
import javafx.scene.image.*;

//for button presses
//import javafx.event.ActionEvent;

/**
 * MapGridTile: subclass of Tile class
 */
public class MapGridTile extends Tile
{
    /**
     * Constructor for objects of class PaletteTile
     */
    public MapGridTile(Image defaultMapGridImage)
    {
        super(defaultMapGridImage);
    }

    /**
     * setImage(): changes the MapGridTile's currentImage
     */
    public void setImage(Image newImage)
    {
        currentImage = newImage;
        ImageView imageView = new ImageView(currentImage);
        this.setGraphic(imageView);
    }
}
