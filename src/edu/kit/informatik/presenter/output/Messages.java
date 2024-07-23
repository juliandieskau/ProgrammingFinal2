package edu.kit.informatik.presenter.output;

/**
 * This class provides constant Strings that are used to define how specific output is passed to the Terminal.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Messages {
    /** When the game was successfully reset and can be played again: */
    public static final String GAME_OBJECT_RESET = "OK";
    /** When a player was eliminated that was not the first of the round or if none was: */
    public static final String FIRE_ROLLED_NORMALLY = "OK";
    /** When the last Player was eliminated and the game is over: */
    public static final String GAME_OVER = "lose";
    /** When the last fire was put out and the game ist over: */
    public static final String PLAYERS_WON = "win";
}
