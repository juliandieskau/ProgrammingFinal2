package edu.kit.informatik.model.objects;

/**
 * Object class for objects that are placed on a square of the game's board.
 * Always tell the BoardObject after constructing it where it is at!
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public abstract class BoardObject {
    
    private String name;
    private int rowPosition;
    private int columnPosition;
    
    /**
     * Constructor that is inherited from all sub-classes that give every board object a name that works like an id.
     * <br>Also sets the position on the board for the object to {0, 0}.<br>
     * If you want to use the position for certain uses of the sub-class, you need to call:
     * <blockquote><pre>
     * changePosition(int i, int j)</pre>
     * </blockquote>
     * 
     * @param name that the board object shall have
     */
    public BoardObject(String name) {
        this.name = name;
        this.rowPosition = 0;
        this.columnPosition = 0;
    }
    
    /**
     * Getter for the name-attribute of the board object.
     * 
     * @return name of the object, sometimes referred to as "id" or "identifier"
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets the position of the board object to the specified row = i and column = j.
     * 
     * @param i new position for the row attribute
     * @param j new position for the column attribute
     */
    public void changePosition(int i, int j) {
        this.rowPosition = i;
        this.columnPosition = j;
    }
    
    /**
     * Getter for the row position of the object
     * 
     * @return row position of the board object on the board
     */
    public int getRowPosition() {
        return this.rowPosition;
    }
    
    /**
     * Getter for the column position of the object
     * 
     * @return column position of the board object on the board
     */
    public int getColumnPosition() {
        return this.columnPosition;
    }
}
