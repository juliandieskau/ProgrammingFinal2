package edu.kit.informatik.model.objects;

/**
 * Class that represents a Lake used for refilling FireEngines that is located on a Square of the board.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Lake extends BoardObject {
    /** Representation of the Lake for Input and Output. */
    private static final String NAME = "L";
    
    /**
     * Constructor that passes the constant name of a lake to the name attribute in the BoardObject.
     */
    public Lake() {
        super(NAME);
    }
}
