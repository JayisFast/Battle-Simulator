package actor;
import javafx.animation.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import army.Army;
import util.*;

/**
 * <b> public </b> <i> abstract </i> class used to create and deal with commonalities shared by all actor subclasses (Hobbit, Wizard, Elf, Orc).
 * @author Jayson Fast
 */
public abstract class Actor {

	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the minimum allowed speed. <p> Set at: {@value #MIN_SPEED} */
	public final static double MIN_SPEED = 0;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the maximum allowed speed. <p> Set at: {@value #MAX_SPEED} */
	public final static double MAX_SPEED = 100;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the minimum allowed health. <p> Set at: {@value #MIN_HEALTH} */
	public final static double MIN_HEALTH = 0;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the maximum allowed health. <p> Set at: {@value #MAX_HEALTH} */
	public final static double MAX_HEALTH = 100;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the minimum allowed strength. <p> Set at: {@value #MIN_STRENGTH} */
	public final static double MIN_STRENGTH = 0;
	/** <b> public </b> <i>final</i> <i>static</i> double variable that holds the maximum allowed strength. <p> Set at: {@value #MAX_STRENGTH} */
	public final static double MAX_STRENGTH = 100;
	/** <b> public </b>  <i>final</i> <i>static</i> double variable that holds the normal distribution spread. <p> Set at: {@value #SPREAD} */
	public final static double SPREAD = 3.0;// normal distribution spread
	public final static double THRESHOLD_OF_ADEQUATE_HEALTH = 0.1;
	/** <b> private </b> <i> static </i> int holds unique identifier that increases by one with each call to Actor() constructor.*/
	private static int actorSerialNumber;// number identifier assigned to each individual actors.
	
	/** <b> private </b> String holding the name assigned to each Actor. */
	private SimpleStringProperty name = new SimpleStringProperty();
	/** <b> private </b> double holding the health value assigned to each Actor. */
	private SimpleDoubleProperty health = new SimpleDoubleProperty();
	/** <b> private </b> double holding the speed value assigned to each Actor. */
	private SimpleDoubleProperty speed = new SimpleDoubleProperty();
	/** <b> private </b> double holding the strength value assigned to each Actor. */
	private SimpleDoubleProperty strength = new SimpleDoubleProperty();
	
	
	/**Used to manage army glow. Which doesn't work.*/
	protected Army armyAllegiance;
	/**Used to hold unique shapes for each actor subtype.*/
	private Node battlefieldAvatar;
	/**Used to create translate and rotate transitions*/
	private Transition transition;
	/**Gets unique shapes for each actor subtype.*/
	public Node getBattleFieldAvatar() { return battlefieldAvatar; }
	protected abstract Node buildBattleFieldAvatar();
	/**Used to display tooltip.*/
	private Tooltip tooltip = new Tooltip();
	

	/**
	 * Default constructor that creates random values for health, speed, and strength. <p> <p> Also increases actorSerialNumber 
	 * by one each time an Actor is created, then uses that value to create the name String to unique identify each Actor. 
	 */
	public Actor(Army armyAllegiance) {
		this.armyAllegiance = armyAllegiance;
		++actorSerialNumber;
		setHealth(SingletonRandom.instance.getNormalDistribution(MIN_HEALTH,
				MAX_HEALTH, SPREAD));
		setSpeed(SingletonRandom.instance.getNormalDistribution(MIN_SPEED,
				MAX_SPEED, SPREAD));
		setStrength(SingletonRandom.instance.getNormalDistribution(MIN_STRENGTH, MAX_STRENGTH, SPREAD));
		setName(String.format("%s%d", "Actor", actorSerialNumber));
		battlefieldAvatar = buildBattleFieldAvatar();
		adjustAvatarBasedOnActorAttributes(); // 
		Tooltip.install(battlefieldAvatar, tooltip); 
	}// end default constructor

	/**Getter that returns health value of an Actor object.
	 * @return  <i>double</i>  health
	 */
	public double getHealth() {
		return health.get();
	}// end gethealth getter/accessor
	
	/**Accepts double and sets it as the Actor's health.
	 * 
	 * @param health
	 */
	public void setHealth(double health) {
		if (health < MIN_HEALTH)
			health = MIN_HEALTH;// if the health value the user enters is less than MIN, sets to zero.
		else if (health > (MAX_HEALTH*3))
			health = MAX_HEALTH;
		this.health.set(health);// else if the health value the user enters is greater than MAX, sets to MAX.
	}// end setHealth setter/mutator
	
	/**
	 * Allows subclasses to add to health, based on what type they are. For example, a Goblin is faster, but weak. An Uruk is strong, etc. 
	 * Also allows certain powerful subclasses to EXCEED bounds of MAX. 
	 * @param health
	 */
	public void addHealth (double health) {
		setHealth(getHealth() + health);
		if (getHealth() <= MIN_HEALTH)
			setHealth(1);
	}//end addHealth
	
	/**Similar to addHealth, but without restrictions. (health can go below zero if an opposing actor does enough damage. )*/
	public void damageHealth (double damage){
		setHealth(getHealth() - damage);
	}//end damageHealth
	
	/**Getter that returns speed value of an Actor object.
	 * @return  <i>double</i>  speed
	 */
	public double getSpeed() {
		return speed.get();
	}// end getSpeed getter/accessor
	
	/**Accepts double and sets it as the Actor's speed.
	 * 
	 * @param speed
	 */
	public void setSpeed(double speed) {
		if (speed < MIN_SPEED)
			speed = MIN_SPEED;
		else if (speed > (MAX_SPEED*3))
			speed = MAX_SPEED;
		this.speed.set(speed);
	}// end setSpeed setter/mutator
	
	/**
	 * Allows subclasses to add to speed, based on what type they are. For example, a Goblin is faster, but weak. An Uruk is strong, etc. 
	 * Also allows certain powerful subclasses to EXCEED bounds of MAX. 
	 * @param speed
	 */
	public void addSpeed (double speed) {
		setSpeed(getSpeed() + speed);
		if (getSpeed() <= MIN_SPEED)
			setSpeed(1);
	}//end addSpeed
	
	/**Getter that returns strength value of an Actor object.
	 * @return  <i>double</i>  strength
	 */
	public double getStrength() {
		return strength.get();
	}// end getStrength getter/accessor
	
	/**Accepts double and sets it as the Actor's strength.
	 * 
	 * @param strength
	 */
	public void setStrength(double strength) {
		if (strength < MIN_STRENGTH)
			strength = MIN_STRENGTH;
		else if (strength > (MAX_STRENGTH*3))
			strength = MAX_STRENGTH;
		this.strength.set(strength);
	}// end setStrength setter/mutator
	
	/**
	 * Allows subclasses to add to health, based on what type they are. For example, a Goblin is faster, but weak. An Uruk is strong, etc. 
	 * Also allows certain powerful subclasses to EXCEED bounds of MAX. 
	 * @param strength
	 */
	public void addStrength (double strength) {
		setStrength(getStrength() + strength);
		if (getStrength() <= MIN_STRENGTH)
			setStrength(1);
	}//end addStrength
	
	/**Getter that returns name string of an Actor object.
	 * @return  <i>String</i> name 
	 */
	public String getName() {
		return name.get();
	}// end getName getter/accessor
	
	/**Accepts String and sets it as the Actor's name.
	 * @param name
	 */
	public void setName(String name) {
		this.name.set(name);
	}// end setName setter/mutator
	
	/**Accepts String and adds to end of Actor's current name. 
	 * @param name
	 */
	public void addName(String name){//Created to give the ability to add Hobbit/Wizard identifiers to the name String, in both Hobbit() and Wizard () constructors. 
		setName(getName() + name);
	}//end addName
	
	public String getAllegiance(){
		return armyAllegiance.getAllegiance();
	}//end getAllegiance
	
	public void setRandomLocationAroundPoint(Point2D ptCenterOfDistribution, double spread) {
		final double range = 100.0/2.0;
		battlefieldAvatar.setTranslateX(SingletonRandom.instance.getNormalDistribution(ptCenterOfDistribution.getX()-range, ptCenterOfDistribution.getX()+range, spread));
		battlefieldAvatar.setTranslateY(SingletonRandom.instance.getNormalDistribution(ptCenterOfDistribution.getY()-range, ptCenterOfDistribution.getY()+range, spread));
	}//end setRandomLocationAroundPoint

	/**
	 * Allows user to change all values associated with a certain Actor object. 
	 */
	public void inputAllFields() {
		String nameChange;// string to hold any changes the user makes to Actor names. Then passed to the setter.
		double statChange;// double use to hold any stat changes made to the Actor stats. Then passed to the correct setter.
			nameChange = InputGUI.getString("Enter a new name for this actor:");
			setName(nameChange);
			statChange = InputGUI.getDouble("Enter a new health between 0-100:", MIN_HEALTH, MAX_HEALTH);
			setHealth(statChange);
			statChange = InputGUI.getDouble("Enter a new speed between 0-100:", MIN_SPEED, MAX_SPEED);
			setSpeed(statChange);
			statChange = InputGUI.getDouble("Enter a new strength between 0-100:", MIN_STRENGTH, MAX_STRENGTH);
			setStrength(statChange);
	}// end inputALlFields

	/**
	 * Returns all information of an Actor object as a String. 
	 * @return <i>String</i> 
	 */
	public String toString() {
		return String.format("%-40s %-40.2f %-40.2f %-40.2f", getName(),
				getHealth(), getSpeed(), getStrength());
	}// end toString
	
	
	/**Creates a table that displays all actors and data.*/
	public static TableView<Actor> createTable() {
		TableView<Actor> table = new TableView<Actor>();
		final double PREF_WIDTH_DOUBLE = 50.0;
		table.setPrefWidth(PREF_WIDTH_DOUBLE * 8.0);
		table.setEditable(true);
		TableColumn<Actor, String> nameCol = new TableColumn<>("Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Actor, String>(
				"name"));
		nameCol.setPrefWidth(PREF_WIDTH_DOUBLE * 2.0);
		TableColumn<Actor, Double> strengthCol = new TableColumn<>("Strength");
		strengthCol
				.setCellValueFactory(new PropertyValueFactory<Actor, Double>(
						"strength"));
		strengthCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn<Actor, Double> speedCol = new TableColumn<>("Speed");
		speedCol.setCellValueFactory(new PropertyValueFactory<Actor, Double>(
				"speed"));
		speedCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn<Actor, Double> healthCol = new TableColumn<>("Health");
		healthCol.setCellValueFactory(new PropertyValueFactory<Actor, Double>(
				"health"));
		healthCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		TableColumn locationXCol = new TableColumn("X");
		locationXCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		locationXCol
				.setCellValueFactory(new Callback<CellDataFeatures<Actor, Double>, ObservableDoubleValue>() {
					public ObservableDoubleValue call(
							CellDataFeatures<Actor, Double> actor) {
						return actor.getValue().battlefieldAvatar
								.translateXProperty();
					}
				});
		TableColumn locationYCol = new TableColumn("Y");
		locationYCol.setPrefWidth(PREF_WIDTH_DOUBLE);
		locationYCol
				.setCellValueFactory(new Callback<CellDataFeatures<Actor, Double>, ObservableDoubleValue>() {
					public ObservableDoubleValue call(
							CellDataFeatures<Actor, Double> actor) {
						return actor.getValue().battlefieldAvatar
								.translateYProperty();
					}
				});
		// END CODE TEST: Following code DOES WORK, but it is NOT generic.

		table.getColumns().addAll(nameCol, strengthCol, speedCol, healthCol,
				locationXCol, locationYCol);
		nameCol.setCellFactory(TextFieldTableCell.<Actor> forTableColumn());
		nameCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, String>>() {
			@Override
			public void handle(CellEditEvent<Actor, String> t) {
				Actor a = (t.getTableView().getItems().get(t.getTablePosition()
						.getRow()));
				a.setName(t.getNewValue());
				a.adjustAvatarBasedOnActorAttributes();
			}
		}); // end setOnEditCommit()

		strengthCol.setCellFactory(TextFieldTableCell
				.<Actor, Double> forTableColumn(new DoubleStringConverter()));
		strengthCol
				.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>() {
					@Override
					public void handle(CellEditEvent<Actor, Double> t) {
						Actor a = (t.getTableView().getItems().get(t
								.getTablePosition().getRow()));
						try {
							a.setStrength(t.getNewValue());
							a.adjustAvatarBasedOnActorAttributes();
						} catch (IllegalArgumentException iae) {
							// change to property was rejected, so old value
							// remains unchanged, but the TableView says
							// otherwise
							Double d = t.getOldValue(); // No change to view,
														// but it does retrieve
														// the previous value.
							System.out.println(d);
						}
					}
				}); // end setOnEditCommit()

		speedCol.setCellFactory(TextFieldTableCell
				.<Actor, Double> forTableColumn(new DoubleStringConverter()));
		speedCol.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>() {
			@Override
			public void handle(CellEditEvent<Actor, Double> t) {
				Actor a = (t.getTableView().getItems().get(t.getTablePosition()
						.getRow()));
				a.setSpeed(t.getNewValue());
				a.adjustAvatarBasedOnActorAttributes();
			}
		}); // end setOnEditCommit()

		healthCol.setCellFactory(TextFieldTableCell
				.<Actor, Double> forTableColumn(new DoubleStringConverter()));
		healthCol
				.setOnEditCommit(new EventHandler<CellEditEvent<Actor, Double>>() {
					@Override
					public void handle(CellEditEvent<Actor, Double> t) {
						Actor a = (t.getTableView().getItems().get(t
								.getTablePosition().getRow()));
						a.setHealth(t.getNewValue());
						a.adjustAvatarBasedOnActorAttributes();
					}
				}); // end setOnEditCommit()
		return table;
	} // end createTable()
	
	
	/**Sets the scale of each actor depending on how strong/healthy they are. */
	private void adjustAvatarBasedOnActorAttributes() { 
//		battlefieldAvatar.setEffect(armyAllegiance.getDropShadow()); DOES NOT WORK
		battlefieldAvatar.setScaleX((getStrength() + getHealth()) / 200.0);
		battlefieldAvatar.setScaleY((getStrength() + getHealth()) / 200.0);
		tooltip.setText(toString());
		
  }//end adjustAvatarBasedOnActorAttributes
	
	/**Creates a translate transition to the nearest enemy actor, then triggers combat()
	 If the actor is too unhealthy to move, creates a rotate transition.*/
	public void move() {
		/**
		 * Call once to begin motion; each call to move() invokes one short-term
		 * animation; an "onFinished" CALLBACK is set to call move() again;
		 * looks recursive BUT IT IS NOT (I call it chained).
		 */
		adjustAvatarBasedOnActorAttributes();
		Point2D ptNewLocation = findNewLocationToMoveTo(); // COULD RETURN NULL
															// if not moving
		if (ptNewLocation == null) { // don't move . . . should be in a rotate
										// mode
			transition = RotateTransitionBuilder
					.create()
					// look around then invoke move() when finished looking
					.node(battlefieldAvatar)
					.fromAngle(0.0)
					.toAngle(360.0)
					.cycleCount(2)
					// if the Actor isn't yet healthy enough to move, then this
					// version of move() (a RotateTransition) will be reasserted
					// again and again.
					.autoReverse(true)
					.duration(Duration.seconds(300.0 / getSpeed()))
					.onFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							move();
						}
					}) // Looks recursive, but not really . . . it is chained,
						// where the termination of a transition calls move() to
						// spawn another.
					.build();
			transition.play();
		} else { // found a new location . . . move towards
			transition = TranslateTransitionBuilder.create()
					.node(battlefieldAvatar)
					.delay(Duration.seconds(200.0 / getHealth()))
					.duration(Duration.seconds(300.0 / getSpeed()))
					.toX(ptNewLocation.getX())
					.toY(ptNewLocation.getY())
					.onFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent arg0) {
							combat();
							move();
						}
					}) // Looks recursive, but not really . . . it is chained,
						// where the termination of a transition calls move() to
						// spawn another.
					.build();
			transition.play();
		}
	}//end move
	
	/**Grabs the race of each actor. Used to determine where each race spawns in populate() Dealt with in each subclass.  */
	public abstract String getRace();
	
	/**Handles damage and health. Since certain actors behave differently, this is handled by each subclass.*/
	public abstract void combat();
	
	/**Decides if an actor is healthy enough to move.*/
	public boolean isHealthyEnoughToMove() { return (getHealth() > MAX_HEALTH * THRESHOLD_OF_ADEQUATE_HEALTH) ? true : false; }
	
	/**Finds nearest enemy actor, and moves towards them. Since certain actors run away, this is handled by the subclasses. */
	public abstract Point2D findNewLocationToMoveTo(); 
	
	/**Stops the current simulation. */
	public void suspend() {
		if (transition != null) {
			transition.stop();
			transition = null;
		}//end suspend
	}//end suspend	
}// end class Actor