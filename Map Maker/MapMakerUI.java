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
import javafx.collections.FXCollections;
import javax.swing.event.*;
import javafx.util.Pair;
import javafx.scene.control.ButtonBar.ButtonData;
//new imports to work with images
import javafx.scene.image.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javafx.embed.swing.SwingFXUtils;

//add image support
import javafx.scene.image.*;

//when Tiles are selected
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
/**
 * User interface for the Map Maker program
 */
public class MapMakerUI extends Application
{
    //variables
    Image activeTileImage = new Image("Tiles/!EraseTile.png");
    MapGrid groundGrid;
    MapGrid wallGrid;
    MapGrid decorGrid;
    private int numWidth, numHeight;
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

        //dialog box for custom map size
        Dialog<Pair<String, String>> mapDialog = new Dialog<>();
        mapDialog.setTitle("MapMaker");
        mapDialog.setHeaderText(" Set Custom Map Size : ");
        mapDialog.setGraphic(new ImageView(new Image("icons/grid.png")));
        Stage gridStage = (Stage)mapDialog.getDialogPane().getScene().getWindow();
        gridStage.getIcons().add(new Image("icons/help.png"));
        
        //creating buttontype and adding to mapdialog 
        ButtonType mapSizeBtnType = new ButtonType("Create Map", ButtonData.OK_DONE);
        mapDialog.getDialogPane().getButtonTypes().addAll(mapSizeBtnType);
        
        //creating gridpane for mapdialog
        GridPane mapSizeGrid = new GridPane();
        mapSizeGrid.setHgap(10);
        mapSizeGrid.setVgap(10);
        mapSizeGrid.setPadding(new Insets(20,150,10,10));
        
        
        //creating textfields for mapdialog and adding to gridpane 
        TextField mapWidth = new TextField();
        mapWidth.setPromptText("Width"); 

        TextField mapHeight = new TextField();
        mapHeight.setPromptText("Height");
        
        //ensure that both TextFields only acccept digits as input
        mapWidth.setOnKeyTyped(e -> 
        {
            char input = e.getCharacter().charAt(0);
            if (Character.isDigit(input) != true) {
                e.consume();
            }
        });
        mapHeight.setOnKeyTyped(e -> 
        {
            char input = e.getCharacter().charAt(0);
            if (Character.isDigit(input) != true) {
                e.consume();
            }
        });

        mapSizeGrid.add(new Label("Enter Width : "), 0, 0);
        mapSizeGrid.add(mapWidth, 1, 0);
        mapSizeGrid.add(new Label("Enter Height : "), 0, 1);
        mapSizeGrid.add(mapHeight, 1, 1);
        
        //getDialogPane().setContent sets the content of the GridPane to the Mapsize Dialog Box 
        mapDialog.getDialogPane().setContent(mapSizeGrid);

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
        Tab paletteA = new Tab("Nature");
        CreatePalette(paletteA, "Nature");
        Tab paletteB = new Tab("Indoors");
        CreatePalette(paletteB, "Indoors");
        Tab paletteC = new Tab("Decorative");
        CreatePalette(paletteC, "Decorative");
        Tab paletteD = new Tab("Misc.");
        CreatePalette(paletteD, "Decorative");
        tilePaletteTabs.getTabs().addAll(paletteA, paletteB, paletteC, paletteD);

        //buttons for Layers
        VBox layerButtons = new VBox();
        Button groundButton = new Button("Ground Layer");
        Button wallsButton = new Button("Wall Layer");
        Button decorButton = new Button("Decor Layer");
        layerButtons.getChildren().addAll(groundButton, wallsButton, decorButton);

        vbox.getChildren().addAll(tilePaletteLabel, tilePaletteTabs, layerLabel, layerButtons);

        //get user input for map size & create & display MapGrid layers 
        //when user clicks create map, take data from textfields and create a map with input
        StackPane stackPane = new StackPane();

        mapDialog.showAndWait();

        //retrieve data from textfields
        String textWidth = mapWidth.getText();
        String textHeight = mapHeight.getText();
        if(textWidth.isEmpty() || textHeight.isEmpty())
        {
            numWidth = 0;
            numHeight = 0;
        }
        else
        {
            //read only integers from textfields and store in integer 
            numWidth = Integer.parseInt(textWidth);
            numHeight = Integer.parseInt(textHeight);
        }
        
        
        //checks for min and max range and keeps prompting until legal integers are inputted 
        while(numWidth < 1 || numWidth > 32 || numHeight < 1 || numWidth > 32)
        {
            mapDialog.setHeaderText("Only enter numbers between 1-15");
            mapDialog.showAndWait();
            textWidth = mapWidth.getText();
            textHeight = mapHeight.getText();
            if(textWidth.isEmpty() || textHeight.isEmpty())
            {
                numWidth = 0;
                numHeight = 0;
            }
            else
            {
                //read only integers from textfields and store in integer 
                numWidth = Integer.parseInt(textWidth);
                numHeight = Integer.parseInt(textHeight);
            }
        };

        //initialize mapgrids using data from the textfields
        groundGrid = new MapGrid(numWidth, numHeight);
        wallGrid = new MapGrid(numWidth, numHeight);
        decorGrid = new MapGrid(numWidth,numHeight);

        //create StackPane for layers
        stackPane.getChildren().addAll(groundGrid, wallGrid, decorGrid);
        root.setCenter(stackPane);

        //ground layer is active by default
        wallGrid.setMouseTransparent(true);
        decorGrid.setMouseTransparent(true);

        //layer button events; change active layer and disable interaction with other
        //layers when relevant button is pressed
        groundButton.setOnAction((ActionEvent event) -> 
            {
                groundGrid.setMouseTransparent(false);
                wallGrid.setMouseTransparent(true);
                decorGrid.setMouseTransparent(true);
            });
        wallsButton.setOnAction((ActionEvent event) -> 
            {
                groundGrid.setMouseTransparent(true);
                wallGrid.setMouseTransparent(false);
                decorGrid.setMouseTransparent(true);
            });            
        decorButton.setOnAction((ActionEvent event) -> 
            {
                groundGrid.setMouseTransparent(true);
                wallGrid.setMouseTransparent(true);
                decorGrid.setMouseTransparent(false);
            });            

        //get list of all MapGridTiles in MapGrid and listen for their events
        for(MapGridTile mapTile : groundGrid.getAllMapTiles())
        {
            mapTile.setOnAction((ActionEvent event) -> mapTile.setImage(activeTileImage));
        }
        for(MapGridTile mapTile : wallGrid.getAllMapTiles())
        {
            mapTile.setOnAction((ActionEvent event) -> mapTile.setImage(activeTileImage));
        }       
        for(MapGridTile mapTile : decorGrid.getAllMapTiles())
        {
            mapTile.setOnAction((ActionEvent event) -> mapTile.setImage(activeTileImage));
        }        

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

            }
        );

        //saveFile functionality
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
                    return;
                }
                try
                {
                    Image image = stackPane.snapshot(null, null);
                    BufferedImage buffImage = SwingFXUtils.fromFXImage(image,null);
                    ImageIO.write(buffImage, "png", imageFile);
                }
                catch (Exception error)
                {
                    error.printStackTrace();
                }
            }
        );

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

        //sort images based on file name by ASCENDING ORDER
        Arrays.sort(images);

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
