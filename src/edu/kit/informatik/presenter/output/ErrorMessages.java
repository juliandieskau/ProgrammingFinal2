package edu.kit.informatik.presenter.output;

/**
 * Class that provides constant Strings to define how specific error messages shall be passed to the output system.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class ErrorMessages {
    /** When the command was not found: */
    public static final String COMMAND_NOT_FOUND
        = "the given input does not match an existing command, please enter a valid command!";
    /** When the given Input had the wrong amount of arguments: */
    public static final String INVALID_AMOUNT_OF_ARGUMENTS
        = "the given input has a wrong amount of arguments: %s given but %s expected";
    /** When the input does not match the Pattern for a valid position */
    public static final String INVALID_POSITION_REPRESENTATION
        = "the given input is not a valid representation of a position. The position should be \"<i>,<j>\".";
    /** When the given number for the rows is out of bounds: */
    public static final String ROW_OUT_OF_BOUNDS
        = "the given row-position %d is out of bounds. Expected to be > 0 and < %d";
    /** When the given number for the columns is out of bounds: */
    public static final String COLUMN_OUT_OF_BOUNDS
        = "the given column-position %d is out of bounds. Expected to be > 0 and < %d";
    /** When a player tries to access a fire engine that is not his: */
    public static final String INVALID_ENGINE_ACCESS
        = "hey you troll, you cannot extinguish fire with fire engines that are not yours!";
    /** When a player tries to access a fire engine that is not existing 
     * or has an invalid name (pretty much the same): */
    public static final String FIRE_ENGINE_NONEXISTENT
        = "there is no fire engine with the given identifier %s. You can use \"show-player\" to list your"
                + " remaining fire engines!";
    /** When a player tries to access a fire engine but the id does not match a valid fire engine name: */
    public static final String FIRE_ENGINE_ID_INVALID
        = "the given ID is not a valid ID for a fire engine!";
    /** When a player tries to extinguish fire with an empty fire engine: */
    public static final String FIRE_ENGINE_EMPTY
        = "the water tank of your fire engine is empty, you first need to refill it before trying to extinguish fire!";
    /** When a player tries an action with a fire engine that has no action points remaining: */
    public static final String NO_ACTION_POINTS
        = "your fire engine has no action points remaining.";
    /** When a player tries to extinguish fire that is not around the fire engine: */
    public static final String NOT_NEXT_TO_FIRE_ENGINE
        = "the square you are trying to extinguish is not next to the square your fire engine is on.";
    /** When a player tries to extinguish a square that is already wet: */
    public static final String SQUARE_IS_ALREADY_WET
        = "you cannot extinguish a square that is already wet!";
    /** When a player tries to extinguish a square with a fire engine that already 
     * has done this in the current round: */
    public static final String EXTINGUISHED_THIS_ROUND
        = "you cannot extinguish a square with a fire engine that has already done this in the current round.";
    /** If the game is already over: */
    public static final String GAME_ALREADY_OVER
        = "the game is already over, only use this command after a restart!";
    /** When the current player has not enough reputation points to buy a fire engine: */
    public static final String BALANCE_TOO_SMALL
        = "you do not have enough reputation points to buy a fire engine!";
    /** When the player tries to place a new fire engine at a position that is not around his fire station */
    public static final String NOT_AROUND_FIRE_STATION
        = "you cannot put a fire engine there, try to place it right next to your fire station";
    /** When the player tries to place a new fire engine on a Square that is on fire: */
    public static final String SQUARE_ON_FIRE
        = "you cannot place a fire engine on a Square that is on fire!";
    /** When the player tries to place a new fire engine on a Square that is not a forest-square: */
    public static final String SQUARE_NO_FOREST
        = "you can place fire engines only on squares, that are forest-squares!";
    /** When a player tries to move his fire engine on a diagonal: */
    public static final String MOVE_DIAGONAL
        = "you can only move your fire engine for a maximum of two squares and for the steps not on diagonals!";
    /** When a player tries to move his fire engine for more than the allowed max amount of squares: */
    public static final String MOVE_TOO_FAR
        = "you cannot move your fire engine for more than %d squares in one move!";
    /** When a player tries to move his fire engine over a square that is not a forest: */
    public static final String MOVE_NOT_FOREST
        = "you cannot move your fire engine over a square that is not a forest!";
    /** When a player tries to move his fire engine on or over a square that is heavily burning: */
    public static final String MOVE_IS_BURNING
        = "you cannot move your fire engine on or over squares that are heavily burning!";
    /** If the engine is not able to move: */
    public static final String MOVE_UNABLE
        = "not able to move the fire engine.";
    /** If the player tries to move his fire engine to a square that is lightly burning: */
    public static final String MOVE_LIGHTLY_BURNING
        = "you cannot move your fire engine onto a square that is lightly burning!";
    /** If the player tries to move his fire engine but he has already performed another action prior: */
    public static final String ALREADY_DONE_ACTION
        = "you cannot move your fire engine after you have already performed an action on it in the round.";
    /** If the called fire engine isn't near a lake or fire station so it cannot refilll: */
    public static final String NO_NEAR_REFILL
        = "you can only refill your fire engine if it is next to a fire station or a lake.";
    /** If the action points are empty or tank is full you cannot refill the tank: */
    public static final String CANNOT_REFILL
        = "cannot refill the fire engine, check if the tank is full or no action points remaining before continuing!";
    
    /** If the round is over and the last "turn" command of the round was entered there is the need to limit the type
     * of commands that can be executed, if the wrong one was called give out this: */
    public static final String WAIT_FOR_DICE
        = "between rounds - when waiting for \"fire-to-roll\" - you can only use the commands \"reset\", \"quit\","
                + "\"show-field\" and \"show-board\". All other shall only be used after rolling the fire.";
    /** If the fire-to-roll command is used when not all players had their turn: */
    public static final String ROUND_NOT_OVER
        = "you can only call this command when all players had their turn and \"turn\" was called.";
    /** When a String was entered that cannot be parsed to an integer: */
    public static final String NOT_A_VALID_INT
        = "the argument you have given is not valid and cannot be parsed to an integer!";
    /** When the argument for fire-to-roll is not in the valid range of integers: */
    public static final String NOT_IN_DICE_INT_RANGE
        = "1 <= %s <= 6 is not true! Please enter a valid dice result in that range.";
    
    
}
