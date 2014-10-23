 package actorTypes;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.CircleBuilder;
import util.InputGUI;
import util.SingletonRandom;
import actor.Actor;
import army.Army;

/**
 * Subclass of class Actor, allows creation of Hobbit Actors, who share all the features of Actor, but 
 * with an additional stealth stat. 
 * <p> <p> <b> Inherits from:</b> Actor
 * @author Jayson Fast
 * 
 */
public class Hobbit extends Actor {
	
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the minimum allowed stealth. <p> Set at: {@value #MIN_STEALTH} */
	public final static double MIN_STEALTH = 0;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the maximum allowed stealth. <p> Set at: {@value #MAX_STEALTH} */
	public final static double MAX_STEALTH = 100;
	/** <b> private </b> double holding the stealth value assigned to each Hobbit. */
	private double stealth = 0;
	/** <b> private </b> int acting as a unique identifier added to the class tag of each Hobbit object. */
	private static int serialNumber;
	/**Holds race name. */
	private String race;
	
	/**
	 * Default constructor that creates random value for stealth. <p> <p> Also adds a (Hobbit) tag to the name String for each 
	 * Hobbit created.  
	 */
	public Hobbit(Army armyAllegiance) {
		super(armyAllegiance);
		++serialNumber;
		super.addName("-Hobbit:" + serialNumber);
		setStealth(SingletonRandom.instance.getNormalDistribution(MIN_STEALTH,
				MAX_STEALTH, SPREAD));
		race = "Hobbit";

		
	}//end default constructor
	
	/**Getter that returns stealth value of a Hobbit object.
	 * @return  <i>double</i>  stealth
	 */
	public double getStealth() {
		return stealth;
	}// end getHealth
	
	/**Accepts double and sets it as the Hobbit's stealth.
	 * 
	 * @param stealth
	 */
	public void setStealth(double stealth) {
		if (stealth < MIN_STEALTH)
			stealth = MIN_STEALTH;// if the stealth value the user enters is less than MIN, sets to zero.
		else if (stealth > MAX_STEALTH)
			stealth = MAX_STEALTH;
		this.stealth = stealth;// else if the stealth value the user enters is greater than MAX, sets to MAX.
	}// end setStealth
	
	/**
	 * Override method, allows user to change all stats associated with each Hobbit, including it's unique 
	 * stealth stat. 
	 */
	@Override
	public void inputAllFields() {
		double statChange;
		super.inputAllFields();
		statChange = InputGUI.getDouble("Enter a new strealth between 0-100:",
				MIN_STEALTH, MAX_STEALTH);
		setStealth(statChange);
	}// end inputAllFields()
	
	/**Returns a green circle avatar for each Hobbit. */
	@Override
	 protected Node buildBattleFieldAvatar() {
	 return CircleBuilder.create().radius(10.0).stroke(Color.BLACK).strokeWidth(2.0).fill(Color.GREENYELLOW).build();
	 }//end buildBattleFieldAvatar
	
	/**
	 * Override method, returns all information of an Hobbit object as a String. 
	 * @return <i>String</i> 
	 */
	@Override
	public String toString() {
		return String.format("%s %s %-40.2f", super.toString(),"Stealth:", getStealth());
	}// end toString()
	
	/**Finds the nearest enemy actor, then divides those X and Y coordinates by half. If the enemy Orc is at (500, 500), the Hobbit will move to (250, 250)
	 * thus simulating "running away." */
	@Override
	public Point2D findNewLocationToMoveTo() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		if(isHealthyEnoughToMove())
			return new Point2D(((nearestOpponent.getBattleFieldAvatar().getTranslateX())/2), ((nearestOpponent.getBattleFieldAvatar().getTranslateY())/2));
		else
			return null;
	}//end findNewLocationToMoveTo
	
	/**Intentionally left blank. Hobbits run and don't fight.*/
	@Override
	public void combat() {
		this.addHealth(10);
		this.move();
	}//end combat()
	
	/**Returns "Hobbit", used to determine where this unit spawns. */
	@Override
	public String getRace(){
		return race;
	}//end getRace
	
}// end class Hobbit
