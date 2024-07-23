package edu.kit.informatik.presenter.output;

import edu.kit.informatik.model.Player;

/**
 * Class that provides a simple type to use to pass on messages in form as strings as well as
 * if an action was successful or not and therefore allows the receiving function to know,
 * how to process that message. TODO javadoc wrong
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class NextReturn {

    private Player player;
    private Next type;
    
    /**
     * Constructor that is used to pass the Player and if Successful to this instance.
     * 
     * @param player    Player  Player of the object to be passed
     * @param t         Type    SUCCESS, FAILURE, EXIT as "successfulness" of the operation
     */
    public NextReturn(Player player, Next t) {
        this.player = player;
        this.type = t;
    }
    
    /**
     * Getter for the Player of the object.
     * 
     * @return the Player inside the object
     */
    public Player getPlayer() {
        return this.player;
    }
    
    /**
     * Getter for the Type of the Result object.
     * 
     * @return SUCCESS if successful operation, FAILURE if not and 
     *          EXIT if the program should close afterwards
     */
    public Next getType() {
        return this.type;
    }
    
    /**
     * Nested enum that provides three values for the Results so called successfulness of the Result object
     * it is used inside.
     * 
     * @author Julian Dieskau
     * @version 1.0
     */
    public enum Next {
        /** If after the next-call the game should go on as always. */
        PROCEED,
        /** If after the call of the next player the round is over and all variables shall be reset. */
        RESET,
    }
}
