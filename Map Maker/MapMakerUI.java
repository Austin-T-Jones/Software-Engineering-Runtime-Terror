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
        //mainStage.getIcons().add(new Image("icons/color_wheel.png"));
        
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
        MenuItem openFile = new MenuItem("Open Map...");
        MenuItem saveFile = new MenuItem("Save Map...");
        //TODO: add more help menus regarding different parts of the program
        MenuItem aboutProgram = new MenuItem("About this program...");
        
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
        
        //tabs for Tile Palette
        TabPane tilePaletteTabs = new TabPane();
        tilePaletteTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab paletteA = new Tab("A");
        CreatePalette(paletteA);
        Tab paletteB = new Tab("B");
        Tab paletteC = new Tab("C");
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
        MapGrid mapGrid = new MapGrid(10, 4);
        root.setCenter(mapGrid);
        //get list of all MapGridTiles in MapGrid and listen for their events
        for(MapGridTile mapTile : mapGrid.getAllMapTiles())
        {
            mapTile.setOnAction((ActionEvent event) -> mapTile.setImage(activeTileImage));
        }
        
        
        
        //TODO: get user input for map size
        
        
        
        mainStage.show();
    }
    
    void CreatePalette(Tab currentTab)
    {
        //create a GridPane that will store all the PaletteTiles
        GridPane paletteTileBox = new GridPane();
        
        //create array of Images pulled from Tiles folder
        String folderName = "Tiles";
        File imageDirectory = new File(folderName);
        File images[] = imageDirectory.listFiles();
        
        //starting coordinates for tiles in GridPane
        int x = 0; 
        int y = 0;
        
        //for loop that fills the VBox with PaletteTiles for each image
        for(int i = 0; i < images.length; i++)
        {
            String currentImageName = images[i].getName();
            System.out.println(currentImageName);
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
        currentTab.setContent(paletteTileBox);
    }
    
    void SetActiveTileImage(Image newImage)
    {
        this.activeTileImage = newImage;
    }
}
