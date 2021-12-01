import javafx.application.Application;
import javafx.stage.*;         // Stage, FileChooser
import javafx.scene.*;         // Scene
import javafx.scene.layout.*;  // VBox, HBox, GridPane, BorderPane, ...
import javafx.scene.input.*;   // KeyEvent, MouseEvent
import javafx.event.ActionEvent;
import javafx.geometry.*;      // Insets, Pos
import javafx.scene.control.*; // Label, TextField, Button, Alert, ...
import java.io.*;              // File, PrintWriter, ...
import java.util.*;            // List, Scanner, Optional, ...
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.ScrollPane;

//new imports to work with images
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

//add image support
import javafx.scene.image.*;

//when Tiles are selected
import javafx.event.ActionEvent;

/**
 * User interface for the Map Maker program
 */
public class MapMakerUI extends Application
{
    //variables
    Image activeTileImage = new Image("Tiles/!EraseTile.png");

    public static void main(String[] args) 
    {
        try
        {
            launch(args);
        }
        catch (Exception error)
        {
            error.printStackTrace();
        }
        finally
        {
            System.exit(0);
        }
    }

    public void start(Stage mainStage) 
    {
        mainStage.setTitle("Map Maker");
        mainStage.setMaximized(true);

        //add application icon
        mainStage.getIcons().add(new Image("icons/color_wheel.png"));

        BorderPane root = new BorderPane();

        Scene mainScene = new Scene(root, 800, 800);
        mainStage.setScene(mainScene);

        //load the stylesheet file
        mainScene.getStylesheets().add("stylesheet.css");

        //add MenuBar at the top of the screen
        MenuBar bar = new MenuBar();
        root.setTop(bar);
        Menu fileMenu = new Menu("File");
        Menu helpMenu = new Menu("Help");

        //create MenuItems
        MenuItem newFile = new MenuItem("New Map...");
        newFile.setGraphic(new ImageView(new Image("icons/page.png")));
        MenuItem openFile = new MenuItem("Open Map...");
        openFile.setGraphic(new ImageView(new Image("icons/folder.png")));
        MenuItem saveFile = new MenuItem("Save Map...");
        saveFile.setGraphic(new ImageView(new Image("icons/disk.png")));
        //TODO: add more help menus regarding different parts of the program
        MenuItem aboutProgram = new MenuItem("About this program...");
        aboutProgram.setGraphic(new ImageView(new Image("icons/information.png")));

        //add MenuItems to Menus
        fileMenu.getItems().addAll(newFile, openFile, saveFile);
        helpMenu.getItems().add(aboutProgram);
        bar.getMenus().addAll(fileMenu, helpMenu);

        //add Tile Palette and Layers windows to left side of screen
        VBox vbox = new VBox();
        //have these windows take up 15% of the screen size
        vbox.prefWidthProperty().bind(mainStage.widthProperty().multiply(0.15));
        root.setLeft(vbox);
        Label tilePaletteLabel = new Label("Tile Palettes"); //TODO: improve labels
        Label layerLabel = new Label("Layers");

        //sets mouse to display current selected tile
        Image cursorImage = new Image("Tiles/rpgTile000.png");
        ImageCursor customCursor = new ImageCursor(cursorImage, 50, 50);
        mainScene.setCursor(customCursor);

        //aboutProgram funcitonality
        aboutProgram.setOnAction(
            (ActionEvent event) ->
            {
                Alert infoAlert = new Alert(AlertType.INFORMATION);
                infoAlert.setTitle("About");
                infoAlert.setHeaderText("Have questions?");
                infoAlert.setContentText("Select a tile on the left side and click anywhere on the canvas to make your map!");
                infoAlert.setGraphic(new ImageView(new Image("icons/help.png")));
                Stage alertStage = (Stage)infoAlert.getDialogPane().getScene().getWindow();
                alertStage.getIcons().add(new Image("icons/information.png"));
                infoAlert.showAndWait();
            }
        );

        //newFile funcitonality
        // TODO: needs to be improved so the grid will properly read user input
        newFile.setOnAction(
            (ActionEvent event) ->
            {
                MapGrid mapGrid = new MapGrid(10, 10);
                root.setCenter(mapGrid);
            }
        );

        //saveFile funcitonality
        DirectoryChooser dirChooser = new DirectoryChooser();
        saveFile.setOnAction(
            (ActionEvent e) ->
            {
                FileChooser chooser = new FileChooser();
                ExtensionFilter filter = new ExtensionFilter("Image Files" , ".png");
                chooser.getExtensionFilters().add(filter);
                File imageFile = chooser.showSaveDialog(mainStage);
                if (imageFile == null)
                {
                    Alert infoAlert = new Alert(AlertType.WARNING);
                    infoAlert.setTitle("Warning");
                    infoAlert.setHeaderText(null);
                    infoAlert.setContentText("No directory selected");
                    infoAlert.setGraphic(new ImageView(new Image("icons/note.png")));
                    Stage alertStage = (Stage)infoAlert.getDialogPane().getScene().getWindow();
                    alertStage.getIcons().add(new Image("icons/information.png"));
                    infoAlert.showAndWait();
                }
                try
                {
                    Image image = root.snapshot(null, null);
                    BufferedImage buffImage = SwingFXUtils.fromFXImage(image,null);
                    ImageIO.write(buffImage, "png", imageFile);
                }
                catch (Exception error)
                {
                    error.printStackTrace();
                }
            }
        );

        //tabs for Tile Palette
        TabPane tilePaletteTabs = new TabPane();
        tilePaletteTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab paletteA = new Tab("A");
        CreatePalette(paletteA, "Nature");
        Tab paletteB = new Tab("B");
        CreatePalette(paletteB, "Indoors");
        Tab paletteC = new Tab("C");
        CreatePalette(paletteC, "Decorative");
        Tab paletteD = new Tab("D");
        tilePaletteTabs.getTabs().addAll(paletteA, paletteB, paletteC, paletteD);

        //buttons for Layers
        VBox layerButtons = new VBox();
        Button groundTab = new Button("Ground");
        Button wallsTab = new Button("Walls");
        Button decorTab = new Button("Decor");
        Button gmTab = new Button("GM");
        layerButtons.getChildren().addAll(groundTab, wallsTab, decorTab, gmTab);

        vbox.getChildren().addAll(tilePaletteLabel, tilePaletteTabs, layerLabel, layerButtons);

        //display MapGrid
        MapGrid mapGrid = new MapGrid(10, 10);
        root.setCenter(mapGrid);
        //get list of all MapGridTiles in MapGrid and listen for their events
        for(MapGridTile mapTile : mapGrid.getAllMapTiles())
        {
            mapTile.setOnAction((ActionEvent event) -> mapTile.setImage(activeTileImage));
        }

        //TODO: get user input for map size

        
        mainStage.show();
    }

    void CreatePalette(Tab currentTab, String folderName)
    {
        //create a GridPane that will store all the PaletteTiles
        ScrollPane scroll = new ScrollPane();
        GridPane paletteTileBox = new GridPane();

        //create array of Images pulled from Tiles folder
        File imageDirectory = new File(folderName);
        File images[] = imageDirectory.listFiles();

        //**sort() method to sort images based on file name**

        //starting coordinates for tiles in GridPane
        int x = 0; 
        int y = 0;

        //for loop that fills the VBox with PaletteTiles for each image
        for(int i = 0; i < images.length; i++)
        {
            String currentImageName = images[i].getName();
            Image newImage = new Image(folderName + "/" + currentImageName);
            PaletteTile newTile = new PaletteTile(newImage);

            //listen for when a PaletteTile is selected
            newTile.setOnAction((ActionEvent event) -> SetActiveTileImage(newTile.getImage()));

            paletteTileBox.add(newTile, x, y); //TODO: make PaletteTiles smaller
            x++;

            //find coordinates for new tile
            if (x > 3)
            {
                x = 0;
                y++;
            }
        }

        //set currentTab's content to be the GridPane of PaletteTiles
        scroll.setContent(paletteTileBox);
        currentTab.setContent(scroll);
    }

    void SetActiveTileImage(Image newImage)
    {
        this.activeTileImage = newImage;
    }
}
