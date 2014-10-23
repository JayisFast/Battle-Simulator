package simulator;

import java.io.*;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 * Entry point to program execution for the JavaFX enabled version of the <i>Hobbit Battlefield Simulator</i>.
 * Adds the scenegraph to the Stage (a.k.a. window).
 * The scenegraph will have a VBox to organize the menu at the top and the battleFieldDisplaySurface below.
 * The battleFieldDisplaySurface will have an ImageView object in the background and the simulator on top (and the simulator is setup as a StackPane).
 */
public class FXLauncher extends Application {
  private Simulator simulator;
  private Scene mainScene;
  private VBox sceneGraphRoot;

  /**
   * Called before the window is shown. The method is used to construct all visual elements before revealing the window.
   * @param primaryStage This Stage object has been built for us by the Application layer of the program (something which is standard in JavaFX).
   */
  private void init(Stage primaryStage) {
    final double SCENE_WIDTH = 1000.0; final double SCENE_HEIGHT = 594.0;
    sceneGraphRoot = new VBox();
    mainScene = new Scene(sceneGraphRoot, SCENE_WIDTH, SCENE_HEIGHT);
    simulator = new Simulator(primaryStage);  

    Group battleFieldDisplaySurface = new Group(buildBackground(), simulator);
    sceneGraphRoot.getChildren().addAll(buildMenus(), battleFieldDisplaySurface);

    primaryStage.setScene(mainScene);
  } // end init()

  /**
   * creates the menus that will be on the top of the scene.
   * @return reference to a newly constructed MenuBar object
   */
  private MenuBar buildMenus() {
    MenuBar menuBar = new MenuBar(); // container for ALL menu components, the following code builds the components and adds to the MenuBar object.

    //**********************************************************************************************************
    // START BUILD RUN MENU
    // Involves creating MenuItem objects: specify 1) Name 2) Action when selected
    MenuItem runPopulateMenuItem = MenuItemBuilder.create().text("_Populate").mnemonicParsing(true).accelerator(KeyCombination.keyCombination("Ctrl+p")).build();
    MenuItem runStartMenuItem = MenuItemBuilder.create().text("_Start").mnemonicParsing(true).accelerator(KeyCombination.keyCombination("Ctrl+s")).build();
    MenuItem runSuspendMenuItem = MenuItemBuilder.create().text("S_uspend").mnemonicParsing(true).accelerator(KeyCombination.keyCombination("Ctrl+u")).build();

    runPopulateMenuItem.setOnAction(new EventHandler<ActionEvent>() { @Override public void handle(ActionEvent t) { simulator.populate(); }});
    runStartMenuItem.setOnAction(new EventHandler<ActionEvent>()		{ @Override public void handle(ActionEvent t) { simulator.run(); }});
    runSuspendMenuItem.setOnAction(new EventHandler<ActionEvent>()	{ @Override public void handle(ActionEvent t) { simulator.suspend(); }});
    Menu runMenu = MenuBuilder.create().text("_Run").mnemonicParsing(true).items(
    		runPopulateMenuItem, 
    		runStartMenuItem, 
    		runSuspendMenuItem)
    		.build();
    // END BUILD RUN MENU
    //**********************************************************************************************************

    
    //**********************************************************************************************************
    // START BUILD PROPERTIES MENU
    // Involves creating MenuItem objects: specify 1) Name 2) Action when selected
    MenuItem propertiesDisplayLightArmyMenuItem = MenuItemBuilder.create().text("Display Light Army (Console)").build();
    propertiesDisplayLightArmyMenuItem.setOnAction(new EventHandler<ActionEvent>()	{ @Override public void handle(ActionEvent arg0) { simulator.displayLightArmyToConsole(); }});
    
    MenuItem propertiesEditLightArmyMenuItem = MenuItemBuilder.create().text("Edit Light Army").build();
    propertiesEditLightArmyMenuItem.setOnAction(new EventHandler<ActionEvent>()			{ @Override public void handle(ActionEvent arg0) { simulator.editLightArmy(); }});
    
    MenuItem propertiesDisplayDarkArmyMenuItem = MenuItemBuilder.create().text("Display Dark Army (Console)").build();
    propertiesDisplayDarkArmyMenuItem.setOnAction(new EventHandler<ActionEvent>()		{ @Override public void handle(ActionEvent arg0) { simulator.displayDarkArmyToConsole(); }});
    
    MenuItem propertiesEditDarkArmyMenuItem = MenuItemBuilder.create().text("Edit Dark Army").build();
    propertiesEditDarkArmyMenuItem.setOnAction(new EventHandler<ActionEvent>()			{ @Override public void handle(ActionEvent arg0) { simulator.editDarkArmy(); }});

    MenuItem propertiesOpenArmyListsMenuItem = MenuItemBuilder.create().text("Show Army _Lists").mnemonicParsing(true).accelerator(KeyCombination.keyCombination("Ctrl+l")).build();
    propertiesOpenArmyListsMenuItem.setOnAction(new EventHandler<ActionEvent>()			{ @Override public void handle(ActionEvent arg0) { simulator.openListWindow(); }});
    
    MenuItem propertiesCloseArmyListsMenuItem = MenuItemBuilder.create().text("Close Army Lists").build();
    propertiesCloseArmyListsMenuItem.setOnAction(new EventHandler<ActionEvent>()		{ @Override public void handle(ActionEvent arg0) { simulator.closeListWindow(); }});
    
    MenuItem propertiesOpenArmyTablesMenuItem = MenuItemBuilder.create().text("Show Army _Tables").mnemonicParsing(true).accelerator(KeyCombination.keyCombination("Ctrl+t")).build();
    propertiesOpenArmyTablesMenuItem.setOnAction(new EventHandler<ActionEvent>()			{ @Override public void handle(ActionEvent arg0) { simulator.openTableWindow(); }});
    
    MenuItem propertiesCloseArmyTablesMenuItem = MenuItemBuilder.create().text("Close Army Tables").build();
    propertiesCloseArmyTablesMenuItem.setOnAction(new EventHandler<ActionEvent>()		{ @Override public void handle(ActionEvent arg0) { simulator.closeTableWindow(); }});
    
    Menu propertiesMenu = MenuBuilder.create().text("Pr_operties").mnemonicParsing(true).items(
    		propertiesDisplayLightArmyMenuItem, 
    		propertiesEditLightArmyMenuItem, 
    		propertiesDisplayDarkArmyMenuItem, 
    		propertiesEditDarkArmyMenuItem, 
    		propertiesOpenArmyListsMenuItem, 
    		propertiesCloseArmyListsMenuItem,
    		propertiesOpenArmyTablesMenuItem,
    		propertiesCloseArmyTablesMenuItem)
    		.build();
    // END BUILD PROPERTIES MENU
    //**********************************************************************************************************

    menuBar.getMenus().addAll(runMenu, propertiesMenu);
    return menuBar;
  } // end buildMenus()

  /**
   * Loads an image file from disk, then formats it for display in a JavaFX scenegraph
   * @return newly created ImageView object
   */
	private ImageView buildBackground() {
    Image imageBackground = null;
    final String filename = "MiddleEarth-3.jpg";
		try {
			imageBackground = new Image(new FileInputStream(filename));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
			System.err.println("Cannot find " + filename);
			System.exit(0);
		}

    ImageView backgroundImageView = ImageViewBuilder.create().image(imageBackground).preserveRatio(true).build();
    backgroundImageView.fitWidthProperty().bind(mainScene.widthProperty());// resize automatically, based on the Simulator sizing
    return backgroundImageView;
 	} // end buildBackground()

	/**
	 * Effective entry point to a JavaFX program.
	 */
 @Override
  public void start(Stage primaryStage) throws Exception {
    init(primaryStage); // construct the elements of the JavaFX application.
    primaryStage.show();// delay showing the window until all elements have been constructed.
  } // end start()

  public static void main(String[] args) { launch(args); }
} // end class FXLauncher