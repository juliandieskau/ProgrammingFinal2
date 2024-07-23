package edu.kit.informatik.model.objects;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.model.Square;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * BoardObject that can move around on the board and extinguish fires with water in its water tank. <br>
 * Always tell the FireEngine where it is on the board by calling 
 * <blockquote><pre>
 * BoardObject.changePosition()</pre>
 * </blockquote>
 * after constructing it.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class FireEngine extends BoardObject {
    // Attributes
    /** Maximum amounts of water-charges to extinguish fire with */
    private static final int TANK_CAPACITY = 3;
    /** Maximum number of actionPoints */ 
    private static final int MAX_ACTION_POINTS = 3;
    /** Amount of water in the tank when it is seemed as empty */
    private static final int TANK_EMPTY = 0;
    /** Amount of action points that disallow any further actions */
    private static final int NO_ACTIONS_LEFT = 0;
    
    /** Current water level in the tank */
    private int waterLevel;
    /** Action-points: Number of actions that can be performed in the current round */
    private int actionPoints;
    /** If this fire engine has already done an action so it cannot move anymore */
    private boolean doneAction = false;
    /** List of all squares, the engine extinguished in the current round */
    private List<Square> extinguished = new ArrayList<>();
    
    /**
     * Constructor that sets the name in the BoardObject attribute. <br>
     * Also initializes the amount of water in the tank of the fire engine as well as the amount of actions
     * it can perform in this round
     * 
     * @param name  String that is like an id for the BoardObject
     */
    public FireEngine(String name) {
        super(name);
        this.waterLevel = TANK_CAPACITY;
        this.actionPoints = MAX_ACTION_POINTS;
    }
    
    /**
     * Getter for the current amount of action points of this FireEngine
     * 
     * @return {@value #actionPoints} amount of actions left in the round
     */
    public int getRemainingActionPoints() {
        return this.actionPoints;
    }
    
    /**
     * Getter for the current amount of water in the tank of this FireEngine
     * 
     * @return {@value #waterLevel} the current water level in the tank
     */
    public int getRemainingWater() {
        return this.waterLevel;
    }
    
    /**
     * Returns if a given square has already been extinguished by <b>this</b> fire engine in the current round 
     * cause it is not allowed twice. You need more than one fire engine to extinguish a heavily burning fire
     * within a single round.
     *
     * @param s square to be tested
     * @return <i>true</i> if square has been extinguished already, 
     *      <i>false</i> if the fire engine can still extinguish it
     */
    public boolean extinguishedThisRound(Square s) {
        if (this.extinguished.contains(s)) {
            return true;
        }
        return false;
    }
    
    /**
     * Uses this FireEngine to extinguish nearby fire.
     * Called from the game which does avoke that, only used here to adjust the water level in the tank.
     * 
     * @param s     Square that is extinguished to store it in the list
     * @return <i>false</i> if the tank is empty or no action points remaining, else <i>true</i>
     */
    public Result extinguishFire(Square s) {
        if (this.waterLevel == TANK_EMPTY) {
            return new Result(ErrorMessages.FIRE_ENGINE_EMPTY, Type.FAILURE);
        } else if (this.actionPoints == NO_ACTIONS_LEFT) {
            return new Result(ErrorMessages.NO_ACTION_POINTS, Type.FAILURE);
        }
        this.waterLevel--;
        this.actionPoints--;
        this.extinguished.add(s);
        this.doneAction = true;
        return new Result("", Type.SUCCESS);
    }
    
    /**
     * Refills the water tank of the FireEngine, but only if not already full.
     * 
     * @return {@value true} if successful, {@value false} if tank is full or action points empty
     */
    public boolean refillTank() {
        if (this.waterLevel == TANK_CAPACITY || this.actionPoints == 0) {
            return false;
        }
        this.waterLevel = TANK_CAPACITY;
        this.actionPoints--;
        this.doneAction = true;
        return true;
    }
    
    /**
     * Resets the action Points after a round ended.
     */
    public void roundReset() {
        this.actionPoints = MAX_ACTION_POINTS;
        this.extinguished.clear();
        this.doneAction = false;
    }
    
    /**
     * Move the FireEngine to another Square.
     * If an action was performed you cannot move anymore, so only allow to move if no action was performed prior.
     * 
     * @return {@value true} if was able to move {@value false} if has done action beforehand
     */
    public boolean move() {
        if (this.doneAction) {
            return false;
        }
        this.actionPoints--;
        return true;
    }
}
