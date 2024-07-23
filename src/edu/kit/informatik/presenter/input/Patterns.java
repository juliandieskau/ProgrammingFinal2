package edu.kit.informatik.presenter.input;

/**
 * Class to provide patterns to compare input Strings with for proof of correctness or vice versa.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Patterns {
    // for command line arguments:
    /** Pattern for fire engines, so A0, A1, A2,... until D0, D1, D2, ... */
    public static final String IS_FIRE_ENGINE = "[A-D][0-9]+";
    /** Pattern for forest squares, dry, wet, lightly burning or heavily */
    public static final String IS_FOREST = "[d|w|\\+|\\*]";
    /** Pattern for lakes that are needed to refill fire engines with water */
    public static final String IS_LAKE = "L";
    /** String of a valid Player or FireStation named after the Player */
    public static final String IS_PLAYER = "[A-D]";
    /** Player A String, also symbolizes the fire station of this player */
    public static final String IS_PLAYER_A = "A";
    /** Player B String, also symbolizes the fire station of this player */
    public static final String IS_PLAYER_B = "B";
    /** Player C String, also symbolizes the fire station of this player */
    public static final String IS_PLAYER_C = "C";
    /** Player D String, also symbolizes the fire station of this player */
    public static final String IS_PLAYER_D = "D";
}
