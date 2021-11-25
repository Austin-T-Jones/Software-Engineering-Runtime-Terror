//add image support
import javafx.scene.image.*;

/**
 * PaletteTile: subclass of Tile class
 */
public class PaletteTile extends Tile
{
    /**
     * Constructor for objects of class PaletteTile
     */
    public PaletteTile(Image defaultPaletteImage)
    {
        super(defaultPaletteImage);
        this.getStyleClass().add("palette-tile");
    }

    /**
     * getImage(): returns the Palettetile's currentImage
     */
    public Image getImage()
    {
        return currentImage;
    }
}
