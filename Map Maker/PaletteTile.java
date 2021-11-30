//add image support
import javafx.scene.image.*;

//for button presses
import javafx.event.ActionEvent;

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
        this.setOnAction((ActionEvent event) -> getImage());
    }

    /**
     * getImage(): returns the PaletteTile's currentImage
     */
    public Image getImage()
    {
        //System.out.println("getImage() was run.");
        return currentImage;
    }
}
