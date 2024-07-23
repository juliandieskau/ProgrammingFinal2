package edu.kit.informatik.model.objects;

/**
 * BoardObject that represents a FireStation standing on the Square and has no further use.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class FireStation extends BoardObject {
    
    /**
     * Constructor that passes the name of the FireStation (shall be the name of the player possessing it) 
     * to the name-attribute in the super-class.
     * 
     * @param name
     */
    public FireStation(String name) {
        super(name);
    }
}
