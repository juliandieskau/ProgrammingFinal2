package edu.kit.informatik.model;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.model.objects.BoardObject;
import edu.kit.informatik.presenter.input.Patterns;

/**
 * Game-class that represents one square of the whole board and stores all objects that stand on it.
 * Also has the State of the Square, which can either be wet, dry, lightly burning (sparking), or
 * heavily burning (burning) and which decides, which action will be taken.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Square {
    /** State of the Square */
    private State state;
    /** List of all Objects that are placed on the Square */
    private List<BoardObject> objects;
    /** Decides if the field is a forest or not, if {@value true} it can burn or be extinguished. */
    public final boolean IS_FOREST;
    /** boolean that shall be reset at the beginning of each round to see, if the burning should happen or not  TODO!*/ 
    private boolean burnedThisRound = false;
    
    
    /**
     * Constructor for the Square which sets its state.
     * Used when there is no need to place an Object on it while init().
     * Use if is a forest and there are no Objects placed here.
     * 
     * @param s         State   initial state of the Square
     */
    public Square(State s) {
        this.state = s;
        this.IS_FOREST = true;
        this.objects = new ArrayList<>();
    }
    
    /**
     * Constructor for the Square which sets its state and an Object on top of it.
     * Used when there is a need to place an Object on it while init().
     * Places the Object in the objects-list, cause from the style of the args[] there is no need for multiple obj.
     * isForest can only be set false if there are only FireStation- or Lake-BoardObjects added!
     * 
     * @param s         State       initial state of the Square
     * @param obj       BoardObject to be placed on the square
     * @param isForest  boolean     if it can be burned or not
     */
    public Square(State s, BoardObject obj, boolean isForest) {
        this.state = s;
        this.IS_FOREST = isForest;
        this.objects = new ArrayList<>();
        this.objects.add(obj);
    }
    
    /**
     * Method that is called if the square is heavily burning and removes all fire engines that are currently on it
     */
    private void burnFireEngines() {
        // prevent this method from changing something if the square is not a forest
        if (!this.IS_FOREST) {
            return;
        }
        // check if there are any objects in the list:
        if (this.objects.isEmpty()) {
            return;
        }
        // iterate backwards through the objects List to prevent changing the positions of the objects in the list
        for (int i = this.objects.size() - 1; i >= 0; i--) {
            // check if the boardobject is a fire engine and if yes remove it from the list
            if (!this.objects.get(i).getName().matches(Patterns.IS_FIRE_ENGINE)) {
                continue;
            }
            this.objects.remove(i);
        }
    }
    
    /**
     * Getter for the current State of the Square.
     * 
     * @return State    current value of the state-attribute 
     */
    public State getState() {
        return this.state;
    }
    
    /**
     * Getter for the List of all Objects that are placed on the Square.
     * 
     * @return List<Object> all objects that are inside the objects-list
     */
    public List<BoardObject> getObjects() {
        return this.objects;
    }
    
    /**
     * Getter for the boolean which tells the program that this square has burned before in the round
     * cause it never should twice.
     * 
     * @return boolean {@value true} if has burned already
     */
    public boolean burnedThisRound() {
        return this.burnedThisRound;
    }
    
    /**
     * Places a BoardObject onto the board, adding it to the list.
     * If is not a Forest-Square return false, FireEngines should not be placed inside lakes or Buildings!
     * 
     * @param o BoardObject to be placed
     * @return {@value true}
     */
    public boolean placeObject(BoardObject o) {
        if (!this.IS_FOREST) {
            return false;
        }
        this.objects.add(o);
        return true;
    }
    
    /**
     * Tries to remove the BoardObject with the given name from the Square.
     * If is not a Forest-Square return false, the single Object on 
     * 
     * @param name   String of the BoardObject's name
     * @return boolean {@value true} if successful, {@value false} if not found or empty list.
     */
    public boolean removeObject(String name) {
        if (!this.IS_FOREST) {
            return false;
        }
        int length = this.objects.size();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (this.objects.get(i).getName().equals(name)) {
                this.objects.remove(i);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method that is called to change the state of the Square making it 
     * dry, when wet
     * sparking, when dry
     * burning, when sparking
     * Only call if
     * 
     * @return boolean {@value true} if the square-state has been changed by the method-call
     */
    public boolean burnSquare() {
        if (IS_FOREST && !burnedThisRound) {
            switch (this.state) {
            case WET:
                this.state = State.DRY;
                this.burnedThisRound = true;
                return (this.state == State.DRY);
            case DRY:
                this.state = State.SPARKING;
                this.burnedThisRound = true;
                return (this.state == State.SPARKING);
            case SPARKING:
                this.state = State.BURNING;
                this.burnedThisRound = true;
                // all fire engines shall be removed if a square is burning heavily:
                this.burnFireEngines();
                return (this.state == State.BURNING);
            default:
                // only reached when State.BURNING, then remove all FireEngines on the board just in case
                this.burnFireEngines();
                return false;
            }
        } else {
            // it has already been burned, so do not do it again
            return false;
        }
        
    }
    
    /**
     * Method that returns if a Square can be extinguished or not
     * 
     * @return {@value true} if the Square can be extinguished, {@value false} if not
     */
    public boolean canExtinguish() {
        if (this.IS_FOREST) {
            if (this.state == State.WET) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    /**
     * Method that is called to change the state of the Square opposite than the burnSquare()-method
     * => wet, when dry; dry, when sparking; sparking, when burning
     * 
     * @return {@value true} if the player shall obtain reputation points for the extinguishing
     */
    public boolean extinguish() {
        if (this.IS_FOREST) {
            if (this.state == State.DRY) {
                this.state = State.WET;
                return false;
            } else if (this.state == State.SPARKING) {
                this.state = State.WET;
            }
            else if (this.state == State.BURNING) {
                this.state = State.SPARKING;
            }
            // fire was extinguished, give reputation points
            return true;
        }
        return false;
    }
    
    /**
     * Resets the round based attributes and is called after applying the wind and burning the squares.
     * Sets the burnedThisRound to false so it can be used the next round.
     */
    public void roundReset() {
        this.burnedThisRound = false;
    }
    
    /**
     * Return the String that should represent the Square
     * So either the squares state is printed or if it isn't a forest-square print out the name of the only object
     * on the square:
     * 
     * @return the string representing the square
     */
    public String printSquare() {
        // if is not a forest-square
        if (!this.IS_FOREST) {
            return this.objects.get(0).getName();
        }
        // if it is a forest square
        if (this.state == State.WET) {
            return "w";
        } else if (this.state == State.DRY) {
            return "d";
        } else if (this.state == State.SPARKING) {
            return "+";
        } else if (this.state == State.BURNING) {
            return "*";
        } else {
            // will never be reached except someone adds another State-value
            return "";
        }
    }
    
    /**
     * Finds the String that should represent the state of this Square on the board in the console
     * 
     * @return the character as a String representing the square for show-board
     */
    public String strShowBoard() {
        if (!this.IS_FOREST) {
            return "x";
        } else if (this.state == State.SPARKING) {
            return "+";
        } else if (this.state == State.BURNING) {
            return "*";
        }
        return "x";
    }
    
    /**
     * States for each square to assign and to check what action to use on them.
     * 
     * @author Julian Dieskau
     * @version 1.0
     */
    public enum State {
        /** When the square is wet */
        WET,
        /** When the square is dry */
        DRY,
        /** When the square is lightly burning */
        SPARKING,
        /** When the square is burning heavily */
        BURNING
    };
}
