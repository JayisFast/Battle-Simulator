package actorTypes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.PolygonBuilder;
import util.InputGUI;
import actor.Actor;
import army.Army;

public class Elf extends Actor {
	/** <b> private </b> int acting as a unique identifier added to the class tag of each Elf object. */
	private static int serialNumber;
	/**Used to determine each elf's rank */
	private int elfType;
	/**Used to determine whether an elf has plate armour or not. If true, health is increased. */
	private boolean hasArmour;
	/**Used to determine whether an elf has a Gondolin Sword. If true, strength is increased. */
	private boolean hasSword;
	/**Holds race name.*/
	private String race;
	
	/**
	 * Default constructor. Randomly assigns an elf type to each elf created, from a weak regular elf, to a powerful elven lord.
	 * Also randomly determines if an elf has armour or weaponry, and increases stats according.  
	 * @param armyAllegiance
	 */
	public Elf (Army armyAllegiance) {
		super(armyAllegiance);
		armyAllegiance.setAllegiance("Forces of Light");
		++serialNumber;
		race = "Elf";
		switch (elfType){
		case 0:
			super.addName("-Elf:" + serialNumber);
			setArmour((Math.random() < 0.5) ? true : false);
			setSword((Math.random() < 0.3) ? true : false);
			if (getArmour() == true);
				super.addHealth(50);
			if (getSword() == true )
				super.addStrength(50);
			break;
		case 1:
			super.addName("-Elven Warrior" + serialNumber);
			super.addHealth(40);
			super.addSpeed(40);
			super.addStrength(40);
			if (getArmour() == true);
				super.addHealth(50);
			if (getSword() == true )
				super.addStrength(50);
			break;
		case 2:
			super.addName("-Elven Lord" + serialNumber);
			super.addHealth(80);
			super.addSpeed(80);
			super.addStrength(80);
			if (getArmour() == true);
				super.addHealth(50);
			if (getSword() == true )
				super.addStrength(50);
			break;
		}//end switch elfType
	}//end default constructor
	
	/**
	 * Returns string representation of boolean hasArmour
	 * @return
	 */
	private String hasArmourString() {
		return hasArmour ? "Has Armour" : "No Armour";
	}//end HasArmourString
	
	/**
	 * returns string representation of boolean hasSword
	 * @return
	 */
	private String hasSwordString() {
		return hasSword ? "Has Sword" : "No Sword";
	}//end HasSwordString
	
	/**
	 * Returns actual value of boolean hasArmour
	 * @return
	 */
	private boolean getArmour() {
		return hasArmour;
	}//end getArmour
	
	/**
	 * Allows users to change whether elf has armour or not. 
	 * @param hasArmour
	 */
	private void setArmour(boolean hasArmour){
		this.hasArmour = hasArmour;
		if (hasArmour == true);
			super.addHealth(50);
	}//end setArmour
	
	/**
	 * Returns boolean value of hasSword
	 * @return
	 */
	private boolean getSword(){
		return hasSword;
	}//end getSword
	
	/**
	 * Allows user to change whether the elf has a sword or not. 
	 * @param hasSword
	 */
	private void setSword(boolean hasSword){
		this.hasSword = hasSword;
		if (this.hasSword = true)
			super.addStrength(50);
			
	}//end setSword
	
	/**
	 * Allows users to change all stats in relation to an elf. 
	 */
	@Override
	public void inputAllFields() {
		boolean statChange;
		super.inputAllFields();
		statChange = InputGUI.getBooleanGUI("Armour?");
		setArmour(statChange);
		statChange = InputGUI.getBooleanGUI("Sword?");
		setSword(statChange);
	}//end inputAllFields
	
	/**Since there are various; ranks of Elves, this will return one of three different coloured elf Polygons.*/
	@Override
	 protected Node buildBattleFieldAvatar() {
		elfType = (int)(Math.random()*3);
		if (elfType == 0){
			 return PolygonBuilder.create().points(20.0, 10.0,  0.0, 17.0,  6.0, 10.0,   0.0, 3.0).fill(Color.FORESTGREEN).stroke(Color.BLACK).strokeWidth(2.0).build();
		}
		if (elfType == 1){
			 return PolygonBuilder.create().points(20.0*(getStrength()*.03),10.0*(getStrength()*.03),  0.0, 17.0*(getStrength()*.03),  6.0*(getStrength()*.03), 10.0*(getStrength()*.03),   0.0, 3.0*(getStrength()*.03)).fill(Color.SLATEGREY).stroke(Color.BLACK).strokeWidth(2.0).build();
		}
		else
			 return PolygonBuilder.create().points(20.0*(getStrength()*.03),10.0*(getStrength()*.03),  0.0, 17.0*(getStrength()*.03),  6.0*(getStrength()*.03), 10.0*(getStrength()*.03),   0.0, 3.0*(getStrength()*.03)).fill(Color.GOLD).stroke(Color.BLACK).strokeWidth(2.0).build();
	
	 }//end buildBattleFieldAvatar
	
	/**
	 * Allows the display of all values within an elf object. 
	 */
	@Override
	public String toString() {
		return String.format("%-31s %-20s %s",  super.toString(), hasArmourString(), hasSwordString());
	}//end toString

	/**Moves the Elf towards the nearest enemy actor. */
	@Override
	public Point2D findNewLocationToMoveTo() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		if(isHealthyEnoughToMove()){
			return new Point2D(((nearestOpponent.getBattleFieldAvatar().getTranslateX())-20), ((nearestOpponent.getBattleFieldAvatar().getTranslateY()))-20);
		}
		else
			return null;
	}//end findNewLocationToMoveTo
	
	/**Attacks the nearest opponent, if both parties have more than 0 health. */
	@Override
	public void combat() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		
		if (this.getHealth() > 0 && nearestOpponent.getHealth() > 0){
		nearestOpponent.damageHealth(this.getStrength()/10);
		}//end if
	}//end combat
	
	/**Returns "Elf". Used to determine where this unit spawns. (Mirkwood) */
	@Override
	public String getRace(){
		return race;
	}
}//end subclass Elf 