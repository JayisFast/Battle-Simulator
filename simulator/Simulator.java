package simulator;

import java.util.ArrayList;

import util.InputGUI;
import javafx.scene.Scene;
import javafx.scene.control.LabelBuilder;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;
import army.*;
/**
 * Creates a StackPane surface onto which the Army objects will be drawn.
 */
public class Simulator extends StackPane {
  private Army forcesOfLight;
  private Army forcesOfDarkness;
  private Stage stageListControllerWindow;
  private Stage stageTableControllerWindow;
  private Stage primaryStage;
  /**Boolean variable check to fix the bug in which you can infinitely "start" the program, 
   * and have all the units infinitely accelerate. Only issue is that once you populate while
   * program is running, you have to suspend, then start.*/
  private boolean runCheck;

  /**
   * Constructor. Sets stage, creates light and dark army, sets them opposing each other.
   * Builds table and lists.
   * @param primaryStage
   */
  public Simulator(Stage primaryStage) {
  	this.primaryStage = primaryStage;
    forcesOfLight = new Army("Forces of Light", Color.GREEN);
    forcesOfDarkness = new Army("Forces of Darkness", Color.RED);
    forcesOfLight.setOpposingArmy(forcesOfDarkness);
    forcesOfDarkness.setOpposingArmy(forcesOfLight);
    buildListViewWindow();
    buildTableViewWindow();
    } // end constructor
  
  /**
   * Adds a handfull of actors onto the field.
   */
  public void populate() {
    forcesOfLight.populate(ActorFactory.Type.HOBBIT, 5, getChildren());
    forcesOfLight.populate(ActorFactory.Type.ELF, 3, getChildren());
    forcesOfLight.populate(ActorFactory.Type.WIZARD, 2, getChildren());
    forcesOfDarkness.populate(ActorFactory.Type.ORC, 8, getChildren());
  } // end populate()
  	
  
  public void displayLightArmyToConsole()	{ forcesOfLight.display(); }	 
  public void displayDarkArmyToConsole()	{ forcesOfDarkness.display(); }		
  
   
  public void editDarkArmy()	{
  	if (forcesOfDarkness.getSize() == 0) {
  		System.out.println("No Actor objects in Army");
  		return;
  	}
  	forcesOfDarkness.edit(InputGUI.getInt("Index to Edit", 0, forcesOfDarkness.getSize()-1)); 
  }//end editDarkArmy
  
  public void editLightArmy() {	
  	if (forcesOfLight.getSize() == 0) {
  		System.out.println("No Actor objects in Army");
  		return;
  	}
  	forcesOfLight.edit(InputGUI.getInt("Index to Edit", 0, forcesOfLight.getSize()-1)); 
  }//end editLightArmy
  
  private final void buildListViewWindow() { // final because of its use in the constructor
  	final double SCENE_WIDTH = 900.0;
  	final double SCENE_HEIGHT = 400.0;
    VBox vBoxLightArmy = VBoxBuilder.create().spacing(5.0).prefWidth(SCENE_WIDTH/2.0).children(LabelBuilder.create().text(forcesOfLight.getAllegiance())   .textAlignment(TextAlignment.CENTER).build(), forcesOfLight.getListViewOfActors())   .build();
    VBox vBoxDarkArmy  = VBoxBuilder.create().spacing(5.0).prefWidth(SCENE_WIDTH/2.0).children(LabelBuilder.create().text(forcesOfDarkness.getAllegiance()).textAlignment(TextAlignment.CENTER).build(), forcesOfDarkness.getListViewOfActors()).build();
    HBox hBoxSceneGraphRoot = HBoxBuilder.create().spacing(5.0).children(vBoxLightArmy, vBoxDarkArmy).build();
    stageListControllerWindow = StageBuilder.create().style(StageStyle.UTILITY).resizable(false).scene(new Scene(hBoxSceneGraphRoot, SCENE_WIDTH, SCENE_HEIGHT)).build();
    stageListControllerWindow.initOwner(primaryStage); // establishes control relationship between two separate windows
  } // end buildListViewWindow()

	public void openListWindow()  { stageListControllerWindow.show(); }//end openListWindow()
	public void closeListWindow() { stageListControllerWindow.hide();	}//end closeListWindow()
	
	private final void buildTableViewWindow() { // final because of its use in the constructor
		    VBox vBoxLightArmy = new VBox(5.0);
		    vBoxLightArmy.getChildren().addAll(
		        LabelBuilder.create().text(forcesOfLight.getAllegiance()).textAlignment(TextAlignment.CENTER).build(),
		        forcesOfLight.getTableViewOfActors());
		    VBox vBoxDarkArmy = new VBox(5.0);
		    vBoxDarkArmy.getChildren().addAll(
		        LabelBuilder.create().text(forcesOfDarkness.getAllegiance()).textAlignment(TextAlignment.CENTER).build(),
		        forcesOfDarkness.getTableViewOfActors());
		    HBox hBoxSceneGraphRoot = new HBox(5.0);

		    hBoxSceneGraphRoot.getChildren().addAll(vBoxLightArmy, vBoxDarkArmy);

		    if (stageTableControllerWindow != null) {
		      stageTableControllerWindow.close();
		      stageTableControllerWindow.setScene(null);
		    }
		    stageTableControllerWindow = new Stage(StageStyle.UTILITY);
		    stageTableControllerWindow.initOwner(primaryStage);
		    stageTableControllerWindow.setScene(new Scene(hBoxSceneGraphRoot));
		  } // end buildTableViewWindow()

	public void run() {
		if (runCheck == false){
		forcesOfDarkness.startMotion();
		forcesOfLight.startMotion();
		runCheck = true;
		}
	}//end run
	

	public void suspend() {
		if (runCheck == true){
		forcesOfDarkness.suspend();
		forcesOfLight.suspend();
		runCheck = false;
		}
	}//end suspend

	public void closeTableWindow() {
		stageTableControllerWindow.hide();
	}//end closeTableWindow

	public void openTableWindow() {
		stageTableControllerWindow.show();
	}//end openTableWindow

} // end class Simulator