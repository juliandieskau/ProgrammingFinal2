package edu.kit.informatik.presenter.input;

import java.util.List;

import edu.kit.informatik.model.Square;

/**
 * Class that holds the parsed command line arguments and the case of failure in the ArgumentType,
 * if needed, so the program exits when given incorrectly. Also holds a message in case of failure
 * so that it can be ouput as an error message to the console.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Arguments {
    
    private List<Square> squares;
    private String message;
    private ArgumentType type;
    
    /**
     * Constructor that gives the instance of this class the List of Squares as well as if the 
     * arguments were correct or not. Also sets the type to incorrect if the objects are empty or null.
     * 
     * @param objects   List<Square>        Squares that should be initialized and have been parsed with their object
     * @param message   String              message in case of type == INCORRECT
     * @param type      ArgumentType        Successfulness of the parsing-operation
     */
    public Arguments(List<Square> objects, String message, ArgumentType type) {
        this.squares = objects; 
        this.type = type;
        this.message = message;
    }
    
    /**
     * Getter for the list of Squares that are set on the board.
     * @apiNote Only use when getType().equals(ArgumentType.CORRECT).
     * 
     * @return List<BoardObject>    list of all Squares that are carried within the object
     */
    public List<Square> getList() {
        return this.squares;
    }
    
    /**
     * Getter for the message of the instance.
     * 
     * @return message to be carried on with the argument
     */
    public String getMessage() {
        return this.message;
    }
    
    /**
     * Getter for the argument-type of the instance
     * 
     * @return ArgumentType <i>CORRECT</i> or <i>INCORRECT</i>
     */
    public ArgumentType getType() {
        return this.type;
    }
    
    /**
     * Enum that is used to represent if the parsing operation of the command line arguments has been successful
     * or if the arguments were given incorrectly.
     * 
     * @author Julian Dieskau
     * @version 1.0
     */
    public enum ArgumentType {
        /** When the command line arguments are of correct form and the program can start: */
        CORRECT,
        /** When the command line arguments have been written incorrectly and the program should ecit: */
        INCORRECT
    }
}
