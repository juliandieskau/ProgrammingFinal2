package edu.kit.informatik.presenter.input;

/**
 * Class that holds String constants that are used for error-output when the given command line arguments are incorrect.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class ArgumentMessages {
    
    /** When there were no command line arguments given: */
    public static final String NO_ARGUMENTS_GIVEN
        = "no command line arguments have been given, expected 1 seperated by commas.";
    /** When there were to many arguments given: */
    public static final String TO_MANY_ARGUMENTS
        = "%d command line arguments given, expected only 1. Please make sure to only separate with \",\"!";
    /** When the given arguments in arg[0] don't match the expected number */
    public static final String WRONG_ARG_AMOUNT
        = "wrong comma-separated-arguments amount! Expected %s, given %d";
    /** When the first two arguments are incorrect */
    public static final String WRONG_FIRST_TWO_ARGS
        = "the first two arguments need to be integers greater than 4. "
                + "They represent the number of rows and columns of the board.";
    /** When the rows amount is an even number */
    public static final String EVEN_ROWS_AMOUNT
        = "%d is even; the board has to have an uneven amount of rows!";
    /** When the columns amount is an even number */
    public static final String EVEN_COLUMNS_AMOUNT
        = "%d is even; the board has to have an uneven amount of columns!";
    /** When the rows amount is smaller than 5 */
    public static final String ROWS_SMALLER_FIVE
        = "given %d rows, need to be at least 5!";
    /** When the columns amount is smaller than 5 */
    public static final String COLUMNS_SMALLER_FIVE
        = "given %d columns, need to be at least 5!";
    /** When the given argument does not match the expected pattern */
    public static final String ARGUMENT_WRONG
        = "the given argument \"%s\" does not match the pattern %s";
    /** When the arguments did not contain a "\\+" */
    public static final String NO_SPARKING_SQUARE
        = "the given arguments did not contain a lightly burning board-square, has to at least contain one!";
    /** When the arguments did not contain a "\\*" */
    public static final String NO_BURNING_SQUARE
        = "the given arguments did not contain a heavily burning board-square, has to at least contain one!";
}
