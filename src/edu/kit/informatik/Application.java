package edu.kit.informatik;

import edu.kit.informatik.core.Errors;
import edu.kit.informatik.core.Input;
import edu.kit.informatik.core.Output;
import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.input.Arguments;
import edu.kit.informatik.presenter.input.Arguments.ArgumentType;
import edu.kit.informatik.presenter.input.Parser;
import edu.kit.informatik.view.IO;

/**
 * Main Class that deals as a link to all sub-classes.
 * Initiates Game- and IO-objects which control how the application behaves.
 * Also implements an exit() method that brings the whole program to stop (if other classes aren't stuck in a loop).
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public final class Application {
    /** {@value true} if exit() is called, controlls the main loop of the program. */
    private static boolean applicationShouldClose = false;
    /** Current Game-object */
    private static Game game;
    /** Handles all commands */
    private static IO io;
    /** What was parsed from the command line */
    private static String[] arguments;
    /** Number of rows of the Board of the game */
    private static int m;
    /** Number of columns of the Board of the game */
    private static int n;
    
    private static final Input IN = new Input() {
        @Override
        public String read() {
            return Terminal.readLine();
        }
    };
    
    private static final Output OUT = new Output() {
        @Override
        public void print(String out) {
            Terminal.printLine(out);
        }
    };
    
    private static final Errors ERR = new Errors() {
        @Override
        public void print(String err) {
            Terminal.printError(err);
        }
    };
    
    /**
     * Constructor that is prohibited to call, throws an illegal-access-error.
     */
    private Application() {
        throw new IllegalAccessError("Do not instantiate this class!");
    }
    
    /**
     * Main-method that is automatically called each time the application is started, calls
     * the init() method, which initiates everything, the program needs.
     * 
     * @param args command line arguments in form of a String-Array
     */
    public static void main(String[] args) {
        init(args);
    }
    
    /**
     * initiates the application, instantiates a Game-object and an IO-object with the Game-object.
     * Also calls the Parser to parse the command line arguments to a usable format or stops the application
     * if the arguments are incorrectly entered.
     * 
     * @param args String[] command line arguments
     */
    private static void init(String[] args) {
        game = new Game();
        io = new IO(game);
        
        // Parse the command line arguments and exit if incorrect
        arguments = args;
        Arguments arg = Parser.parseArgs(args);
        if (arg.getType() == ArgumentType.CORRECT) {
            game.init(m, n, arg.getList());
            run();
        } else {
            ERR.print(arg.getMessage());
            exit();
        }
    }
    
    /**
     * Main-loop that calls the Input-Output-System to process the next input and print the output to the console.
     * Runs as long as applicationShouldClose is false.
     * 
     * Calls the initiated io-object and the command in that for the next IN value
     */
    private static void run() {
        while (!applicationShouldClose) {
            io.next(IN, OUT, ERR, game);
        }
    }
    
    /**
     * Is to be called to set the amount of rows and columns the board has for all games that happen in that run of the
     * program and should only be called once at the start of the program by parsing command line arguments.
     * 
     * @param rows      Number of rows of the board
     * @param columns   Number of columns of the board
     */
    public static void setBoardConstants(int rows, int columns) {
        m = rows;
        n = columns;
    }
    
    
    /**
     * Sets the game and io-attributes to new Game and IO objects and initiates the game-object
     * with the values that were parsed in init() at the start of the program.
     * 
     * The run() method then has different static parameters to call for the next command and the garbage collector 
     * removes the now not anymore used Game and IO objects.
     */
    public static void reset() {
        game = new Game();
        
        // Parse the command line arguments and exit if incorrect
        Arguments arg = Parser.parseArgs(arguments);
        if (arg.getType() == ArgumentType.CORRECT) {
            game.init(m, n, arg.getList());
        }
    }
    
    /**
     * Forces the program to stop by stopping the main-loop of the program.
     * Everything that is still called by the current iteration will still finish but no new
     * iteration will be executed.
     */
    public static void exit() {
        applicationShouldClose = true;
    }
}
