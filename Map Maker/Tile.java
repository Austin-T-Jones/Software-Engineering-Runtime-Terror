import javafx.scene.control.Button;

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
        
        this.currentImage = defaultImage;
        ImageView imageView = new ImageView(currentImage);
        //tileButton = new Button("", imageView);
        this.setGraphic(imageView);
    }
}
