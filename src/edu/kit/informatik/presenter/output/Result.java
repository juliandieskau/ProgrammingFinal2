package edu.kit.informatik.presenter.output;

/**
 * Class that provides a simple type to use to pass on messages in form as strings as well as
 * if an action was successful or not and therefore allows the receiving function to know,
 * how to process that message.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Result {

    private String message;
    private Type type;
    
    /**
     * Constructor that is used to pass the message and successfulness of an operation in the type,
     * only point where that can be edited. All attributes are read only!
     * 
     * @param message   String  content of the object, message to be passed to be further processed
     * @param t         Type    SUCCESS, FAILURE, EXIT as "successfulness" of the operation
     */
    public Result(String message, Type t) {
        this.message = message;
        this.type = t;
    }
    
    /**
     * Getter for the message of the object
     * 
     * @return this.message as String
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Getter for the Type of the Result object.
     * 
     * @return SUCCESS if successful operation, FAILURE if not and 
     *          EXIT if the program should close afterwards
     */
    public Type getType() {
        return this.type;
    }
    
    /**
     * Nested enum that provides three values for the Results so called successfulness of the Result object
     * it is used inside.
     * 
     * @author Julian Dieskau
     * @version 1.0
     */
    public enum Type {
        /** The operation (mostly the execution of a command) was successful. */
        SUCCESS,
        /** The operation caused an error that is returned in the message. */
        FAILURE,
        /** The operation closes the program and the message shall not be used! */
        EXIT
    }
}
