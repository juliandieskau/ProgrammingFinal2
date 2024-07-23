package edu.kit.informatik.model;

/**
 * Gives specific types for the checkIfSorrounding() method to check different options.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public enum SurroundingParam {
    /** If it should check also diagonals */
    DIAGONAL,
    /** If it should only check in cardinal directions */
    DIRECT,
    /** If it should check in a straight line in all directions */
    LINE
}
