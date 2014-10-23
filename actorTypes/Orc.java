package actorTypes;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.RectangleBuilder;
import util.InputGUI;
import actor.Actor;
import army.Army;

public class Orc extends Actor {
	/** <b> private </b> int acting as a unique identifier added to the class tag of each Orc object. */
	private static int serialNumber;
	/**Used to determine what type of Orc is created (Strong Uruk Hai, Fast Goblins, Boring regular Orcs, etc) */
	private int orcType;
	/**Determines whether a boring Orc becomes a buffed up Orc Chieftain.*/
	private int orcSubType;
	/**Determines whether an Uruk-Hai is a regular foot soldier, or a berserker. Ideally, later on in the coding process, berserkers will detonate upon contact to an enemy unit. */
	private int urukType;
	/**Modifier that buffs all an Orcs stats if true. Later in the coding process, this will only become true if an Orc kills an enemy unit. May stack. */
	private boolean bloodLust;
	/**Holds race name. */
	private String race;

	/**
	 * Default constructor randomly determines what type of Orc is created, from fast but weak goblins, to powerful Uruk-Hai. 
	 * Also randomly decides whether an Orc is in a blood frenzy and increases stats accordingly.
	 * @para
	 * armyAllegiance
	 */
	public Orc (Army armyAllegiance){
		super(armyAllegiance);
		armyAllegiance.setAllegiance("Forces of Darkness");
		++serialNumber;
		race = "Orc";
		switch (orcType){
		case 0:
			orcSubType = (int)(Math.random()*100);
			if (orcSubType < 95){
				super.addName("-Orc:" + serialNumber);
				setBloodLust((Math.random() < 0.5) ? true : false);
					if (getBloodLust() == true){
						super.addHealth(30);
						super.addStrength(30);
						super.addSpeed(30);
					}//end if
			}//end if
			else {
				super.addName("-Orc Cheiftan:" + serialNumber);
				super.addHealth(40);
				super.addStrength(40);
				super.addSpeed(40);
				setBloodLust((Math.random() < 0.5) ? true : false);
				if (getBloodLust() == true){
					super.addHealth(30);
					super.addStrength(30);
					super.addSpeed(30);
				}//end if
			}//end else
			break;
		case 1:
			urukType = (int)(Math.random()*5);
				if (urukType <= 3){
					super.addName("-Uruk-Hai:" + serialNumber);
					super.addStrength(30);
					super.addHealth(30);
					setBloodLust((Math.random() < 0.5) ? true : false);
					if (getBloodLust() == true){
						super.addHealth(30);
						super.addStrength(30);
						super.addSpeed(30);
					}//end if
				}//end if
				else {
					super.addName("-Uruk Berserker:" + serialNumber);
					super.setHealth(15);
					super.addStrength(70);
					setBloodLust((Math.random() < 0.5) ? true : false);
					if (getBloodLust() == true){
						super.addHealth(30);
						super.addStrength(30);
						super.addSpeed(30);
					}//end if
				}//end else
			break;
		case 2:
			super.addName("-Goblin:" + serialNumber);
			super.addStrength(-20);
			super.addSpeed(50);
			super.addHealth(-20);
			setBloodLust((Math.random() < 0.5) ? true : false);
			if (getBloodLust() == true){
				super.addHealth(30);
				super.addStrength(30);
				super.addSpeed(30);
			}//end if
			break;
		}//end orcType switch
	}//end default constructor
		
	/**
	 * Returns actual boolean value of bloodLust.
	 * @return
	 */
	public boolean getBloodLust(){
		return bloodLust;
	}//end getBloodLust
	
	/**
	 * Returns string representation of bloodLust.
	 * @return
	 */
	private String hasBloodLustString() {
		return bloodLust ? "On" : "Off";
	}//end HasHorseString
		
	/**
	 * Allows users to change whether bloodLust is on or off.
	 * @param bloodLust
	 */
	public void setBloodLust(boolean bloodLust) {
		this.bloodLust = bloodLust;
		if (getBloodLust() == true){
			super.addHealth(30);
			super.addStrength(30);
			super.addSpeed(30);
		}//end if
	}// end setBloodLust
	
	/**
	 * Allows users to change all stats within each Orc object. 
	 */
	@Override
	public void inputAllFields() {
		boolean statChange;
		super.inputAllFields();
		statChange = InputGUI.getBooleanGUI("Bloodlust?");
		setBloodLust(statChange);
	}//end inputAllFields
	
	/**Since there are three different races of Orcs, this assigns a different coloured rectangle to each different Orc.*/
	@Override
	 protected Node buildBattleFieldAvatar() {
		orcType = (int)(Math.random()*3);
		if (orcType == 0){
			return RectangleBuilder.create().width(15.0*(getStrength()*.02)).height(10.0*(getStrength()*.02)).fill(Color.DARKRED).stroke(Color.BLACK).strokeWidth(2.0).build();
		}
		if (orcType == 1){
			return RectangleBuilder.create().width(15.0*(getStrength()*.02)).height(10.0*(getStrength()*.02)).fill(Color.BLACK).stroke(Color.BLACK).strokeWidth(2.0).build();
		}
		else
			return RectangleBuilder.create().width(15.0*(getStrength()*.02)).height(10.0*(getStrength()*.02)).fill(Color.DARKSEAGREEN).stroke(Color.BLACK).strokeWidth(2.0).build();
	 }//end buildBattleFieldAvatar
	
	/**
	 * Allows for presentation of all values within each Orc object. 
	 */
	@Override
	public String toString() {
		return String.format("%s %s %-31s",super.toString(),"Bloodlust:", hasBloodLustString());
	}//end toString
	
	
	/**Finds nearest enemy actor, and moves towards them. */
	@Override
	public Point2D findNewLocationToMoveTo() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		if(isHealthyEnoughToMove())
			return new Point2D(((nearestOpponent.getBattleFieldAvatar().getTranslateX())), ((nearestOpponent.getBattleFieldAvatar().getTranslateY())));
		else
			return null;
	}//end findNewLocationToMoveTo
		
	/**Deals damage to nearest enemy actor. */
	@Override
	public void combat() {
		Army opposingArmy = armyAllegiance.getOpposingArmy();
		Actor nearestOpponent = opposingArmy.findNearestOpponent(this);
		
		if (this.getHealth() > 0 && nearestOpponent.getHealth() > 0){
		nearestOpponent.damageHealth(this.getStrength()/10);
		}
		this.move();
	}//end combat()
	
	/**Returns race, used to determine where Orcs spawn. (Mordor) */
	@Override
	public String getRace(){
		return race;
	}//end getRace()
	
}//end subclass Orc
