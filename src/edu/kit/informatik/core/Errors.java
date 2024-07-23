package edu.kit.informatik.core;

/**
 * Implement to define how to output Error-Strings to the user.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public interface Errors {
    /**
     * Prints error-output to the User-Interface through the implemented way.
     * 
     * @param err error-String
     */
    void print(String err);
}
