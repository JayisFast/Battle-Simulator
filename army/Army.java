package army;


import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.effect.Effect;
import javafx.scene.paint.Color;
import actor.Actor;

/** Allows quick creation of large Arraylists of soldiers of various types and factions. */
public class Army {	
	/**Holds name of faction. */
	private String factionName;
	/**Used to get a list of all actors in both armies. */
	private ListView<Actor> listView;
	/**ArrayList holding all Actors created.  */
	private ObservableList<Actor> collectionActors = FXCollections.observableArrayList(new ArrayList <Actor>());
	/**Used to create a table of all actors in both armies. */
	private TableView<Actor> tableView;
	/**SUPPOSED to be used to colour the glow for each army. Doesn't work for some reason. */
	private Color color;
//	private Effect effect = DropShadowBuilder.create().color(color).build();
//	public Effect getDropShadow() {return effect;}
	/**Used to establish enemy armies to each army. */
	private Army opposingArmy;

	/**Constructor. Sets up table and list. 
	 * @param factionName the name of the faction
	 * @param Color parameter supposed to dela with army glow. Rex never got back to us on that.*/
	public Army (String factionName, Color color) {
	this.color = color;
	this.factionName =  factionName;
	listView = new ListView<Actor>();
	listView.setItems(collectionActors);
	tableView = Actor.createTable();
	tableView.setItems(collectionActors);
	}//end constructor
	
	/**
	 * Allows quick population of the collectionActors array list. Uses each actors Race to spawn them randomly around a certian area, as opposed to 
	 * random actor confetti.
	 * @param type  Accepts any type specified in Actor Factory. 
	 * @param numToAdd  Accepts any int, creates as many soldiers as specified. 
	 * @param listChildNodes
	 */
	public void populate (ActorFactory.Type type, int numToAdd, ObservableList<Node> listChildNodes) {
		for (int i = 0; i< numToAdd; ++i){
			Actor actor = type.create(this);
			collectionActors.add(actor);
			if (actor.getRace() == "Elf"){
				actor.setRandomLocationAroundPoint(new Point2D(600, 150), 2.0);
			}//end if
			if (actor.getRace() == "Hobbit"){
				actor.setRandomLocationAroundPoint(new Point2D(700, 300), 2.0);
			}//end if
			if (actor.getRace() == "Wizard"){
				actor.setRandomLocationAroundPoint(new Point2D(400, 250), 2.0);
			}//end if
			if (actor.getAllegiance() == "Forces of Darkness"){
				actor.setRandomLocationAroundPoint(new Point2D(700, 350), 2.0);
			}//end if
			listChildNodes.add(actor.getBattleFieldAvatar());
		}//end for loop
	}//end populate
	
	/**
	 *  Allows access to inputAllFields.
	 * @param choice  allows user to decide which soldier they wish to change. 
	 * @return String
	 */
	public void edit(int choice) {
		collectionActors.get(choice).inputAllFields();
	}//end edit
	
	/**
	 * Getter for factionName string.  
	 * @return
	 */
	public String getAllegiance(){
		return factionName;
	}//end factionName getter
	
	/**
	 * Setter for factionName string.
	 * @param factionName
	 */
	public void setAllegiance(String factionName){
		this.factionName = factionName;
	}//end factionName setter
	
	/**
	 * Prints all data related to an army.
	 */
	public void display() {
		for (int i = 0; i < collectionActors.size(); i++)
			System.out.println(collectionActors.get(i).toString());
	}//end display
	
	/**
	 * Returns an int of the size of the array of Actors. 
	 * @return
	 */
	public int getSize(){
		return collectionActors.size();
	}//end getSize
	
	/**
	 * Returns a listView of the army. 
	 * @return
	 */
	public Node getListViewOfActors() {
		return listView;
	}//end getListViewOfActors
	
	/**
	 * Returns a tableView of the army. 
	 * @return
	 */
	public Node getTableViewOfActors() {
		return tableView;
	}//end getTableViewOfActors
	
	/**Triggers all actors in the army to move. */
	public void startMotion() {
		for (Actor actor : collectionActors)
			actor.move();	
	}//end startMotion
	
	/**Suspends all movement by the army. */
	public void suspend() {
		for (Actor actor : collectionActors)
			actor.suspend();	
	}//end suspend
	
	/**
	 * Gets the opposing army, allowing actors to move towards the opposing army, and deal damage to the other army. 
	 * @return
	 */
	public Army getOpposingArmy() {
		return opposingArmy;
	}//end getOpposingArmy
	
	/**
	 * Sets opposingArmy.
	 * @param opposingArmy
	 */
	public void setOpposingArmy(Army opposingArmy) {
		this.opposingArmy = opposingArmy;
	}//end setOpposing army.

	/**
	 * 
	 * @param actorToMove
	 * @return
	 */
	public Actor findNearestOpponent(Actor actorToMove) {
		Point2D actorToMoveLocation = new Point2D(actorToMove.getBattleFieldAvatar().getTranslateX(), actorToMove.getBattleFieldAvatar().getTranslateY());
		Actor nearestOpponent = null;
		double closestDistance = Double.MAX_VALUE;
		for ( Actor opposingActor : collectionActors){
			double currentDistance = actorToMoveLocation.distance(opposingActor.getBattleFieldAvatar().getTranslateX(), opposingActor.getBattleFieldAvatar().getTranslateY());
			if (currentDistance < closestDistance){
				closestDistance = currentDistance;
				nearestOpponent = opposingActor;
			}
		}
		return nearestOpponent;
	}//end findNearestOpponent
}//end class Army

