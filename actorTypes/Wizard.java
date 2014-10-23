package actorTypes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.PolygonBuilder;
import util.InputGUI;
import actor.Actor;
import army.Army;

/**
 * Subclass of class Actor, allows creation of Wizard Actors, who share all the features of Actor, but 
 * with additional hasStaff and hasHorse boolean conditions.  
 * <p> <p> <b> Inherits from:</b> Actor
 * @author Jayson Fast
 * 
 */
public class Wizard extends Actor {
	/** <b>private</b> boolean determining whether Wizard has a staff or not. */
	private boolean hasStaff;
	/** <b>private</b> boolean determining whether Wizard has a horse or not. */
	private boolean hasHorse;
	/**<b> private </b> int acting as a unique identifier added to the class tag of each Wizard object. */
	private static int serialNumber;
	/**Holds race name.  */
	private String race;
	
	/**
	 * Default constructor that randomly determines hasHorse and hasStaff booleans for each Wizard. 
	 * <p> <p> Also adds a (Wizard) tag to the name String for each Wizard created.  
	 */
	public Wizard(Army armyAllegiance) {
		super(armyAllegiance);
		++serialNumber;
		race = "Wizard";
		super.addName("-Wizard:" + serialNumber);
		setStaff((Math.random() < 0.95)? true : false);// class specification.method() less than 95%.
		if (getStaff() == true)
			super.addStrength(80);
		setHorse((Math.random() < 0.1) ? true : false);
		if (getHorse() == true)
			super.addSpeed(80);
	}//end default constructor
	
	/** <b>private</b> String that returns positive or negative dialogue depending on whether or not 
	 * a Wizard object has a staff or not. 
	 * @returns String
	 */
	private String hasStaffString() {
		return hasStaff ? "Has Staff" : "Does not have Staff"; // Conditional operator.
	}//end hasStaff
	
	/** <b>private</b> String that returns positive or negative dialogue depending on whether or not 
	 * a Wizard object has a horse or not. 
	 * @returns String
	 */
	private String hasHorseString() {
		return hasHorse ? "Has Horse" : "Does not have Horse";
	}//end HasHorseString
	
	/**Getter that returns staff value of an Wizard object.
	 * @return  <i>boolean</i>  hasStaff
	 */
	public boolean getStaff() {
		return hasStaff;
	}//end getStaff
	
	/**Accepts boolean and uses it to set Wizard's staff status. 
	 * 
	 * @param hasStaff
	 */
	public void setStaff(boolean hasStaff) {
		this.hasStaff = hasStaff;
		if (hasStaff == true)
			super.addStrength(80);
	}// end setStaff
	
	/**Getter that returns horse value of an Wizard object.
	 * @return  <i>boolean</i>  hasHorse
	 */
	public boolean getHorse() {
		return hasHorse;
	}//end getHorse
	
	/**Accepts boolean and uses it to set Wizard's horse status. 
	 * 
	 * @param hasHorse
	 */
	public void setHorse(boolean hasHorse) {
		this.hasHorse = hasHorse;
		if (hasHorse == true)
			super.addSpeed(80);
	}//end setHorse
	
	/**
	 * Override method, allows user to change all stats associated with each Wizard, including it's unique 
	 * hasStaff and hasHorse booleans.
	 */
	@Override
	public void inputAllFields() {
		boolean statChange;
		super.inputAllFields();
		statChange = InputGUI.getBooleanGUI("Does Wizard have a Staff?");
		setStaff(statChange);
		statChange = InputGUI.getBooleanGUI("Does Wizard have a Horse?");
		setHorse(statChange);
	}//end inputAllFields
	
	/**Returns a "Wizard Hat" polygon. */
	@Override
	 protected Node buildBattleFieldAvatar() {
	 return PolygonBuilder.create().points(8.0*(getStrength()*.02), 15.0*(getStrength()*.02),  0.0,20.0*(getStrength()*.02),  20.0*(getStrength()*.02),20.0*(getStrength()*.02),   15.0*(getStrength()*.02), 15.0*(getStrength()*.02), 11.0*(getStrength()*.02), 0.0 ).fill(Color.DEEPSKYBLUE).stroke(Color.BLACK).strokeWidth(2.0).build();
	 }//end buildBattleFieldAvatar
	
	/**
	 * Override method, returns all information of an Wizard object as a String. 
	 * @return <i>String</i> 
	 */
	@Override
	public String toString() {
		return String.format("%s %-31s %-31s", super.toString(),
				hasStaffString(), hasHorseString());
	}// end toString
	
	/**Finds the location of the nearest enemy, and moves towards them. */
	@Override
	public Point2D findNewLocationToMoveTo() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		if(isHealthyEnoughToMove())
			return new Point2D(((nearestOpponent.getBattleFieldAvatar().getTranslateX())), ((nearestOpponent.getBattleFieldAvatar().getTranslateY())));
		else
			return null;
	}//end findNewLocationToMoveTo
	
	/**Deals damage to the nearest enemy actor. */
	@Override
	public void combat() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		
		if (this.getHealth() > 0 && nearestOpponent.getHealth() > 0){
			nearestOpponent.damageHealth(this.getStrength()/10);
		}
		this.move();
	}//end combat()
	
	/**Returns "Wizard", used to determine where this unit spawns. */
	@Override
	public String getRace(){
		return race;
	}//end getRace()
	
}// end class Wizard