import javafx.scene.control.Button;
import javafx.geometry.Insets;

//add image support
import javafx.scene.image.*;
/**
 * Tile class; superclass for MapGridTile and PaletteTile classes
 */
public class Tile extends Button
{
    Button tileButton;
    Image currentImage;
    
    /**
     * Constructor for objects of class Tile
     */
    public Tile(Image defaultImage)
    {
        super();
        
        this.getStyleClass().add("palette-tile");
        this.setPadding(Insets.EMPTY);
        this.currentImage = defaultImage;
        ImageView imageView = new ImageView(currentImage);
        this.setGraphic(imageView);
    }
}
