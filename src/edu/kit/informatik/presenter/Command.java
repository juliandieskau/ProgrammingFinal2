package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.Result;

/**
 * Class that is implemented by different command-classes to give an "interface" for them to work in a list.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public abstract class Command {

    /**
     * Gives the name of the command, needs to be implemented by extending classes.
     * 
     * @return String name of the command
     */
    public abstract String getName();

    /**
     * Executes what the command should do, needs to be implemented by extending classes.
     * 
     * @param g     Game-object
     * @param input 
     * @return Result of the command that is shall be printed to the console.
     */
    public abstract Result execute(Game g, String[] input);
}
