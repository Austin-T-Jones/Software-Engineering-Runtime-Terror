//add image support
import javafx.scene.image.*;

/**
 * Tile class; superclass for MapGridTile and PaletteTile classes
 */
public class Tile
{
    Image currentImage;
    
    /**
     * Constructor for objects of class Tile
     */
    public Tile(Image defaultImage)
    {
        this.currentImage = defaultImage;
    }
}
