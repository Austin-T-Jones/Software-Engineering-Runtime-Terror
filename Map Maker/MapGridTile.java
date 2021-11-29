//add image support
import javafx.scene.image.*;

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
    }
}
