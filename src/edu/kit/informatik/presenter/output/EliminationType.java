package edu.kit.informatik.presenter.output;

/**
 * Enum that defines different types for results a eliminate-players-method-call can have
 * to examine which output shall be printed.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public enum EliminationType {
    /** If it was the last player, return that the game is lost: */
    LOSE,
    /** If the first was eliminated: */
    FIRST,
    /** If no player was eliminated: */
    NONE,
    /** If a player was eliminated, that is not the first player: */
    SECONDARY
}