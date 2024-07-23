package edu.kit.informatik.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.kit.informatik.Application;
import edu.kit.informatik.core.Errors;
import edu.kit.informatik.core.Input;
import edu.kit.informatik.core.Output;
import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.BuyFireEngine;
import edu.kit.informatik.presenter.Command;
import edu.kit.informatik.presenter.Extinguish;
import edu.kit.informatik.presenter.FireToRoll;
import edu.kit.informatik.presenter.Move;
import edu.kit.informatik.presenter.Quit;
import edu.kit.informatik.presenter.Refill;
import edu.kit.informatik.presenter.Reset;
import edu.kit.informatik.presenter.ShowBoard;
import edu.kit.informatik.presenter.ShowField;
import edu.kit.informatik.presenter.ShowPlayer;
import edu.kit.informatik.presenter.Turn;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Class that represents the Input-Output-System and checks if an Input matches a command,
 * executes the command and prints the message or error-message to the Terminal.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class IO {

    private Game game;
    private final List<Command> commands;
    
    /**
     * Constructor of the InputOutput class, registers the commands
     * 
     * @param game  Game object, that processes the input and has all the logic for the game itself
     */
    public IO(Game game) {
        this.game = game;
        // Register all commands here:
        this.commands = new ArrayList<>();
        this.commands.addAll(List.of(new Quit(), new Reset(), new Turn(), new BuyFireEngine(), new Extinguish(), 
                new ShowBoard(), new ShowField(), new ShowPlayer(), new Move(), new Refill(), new FireToRoll()));
    }

    /**
     * Private class that returns an Optional of the command.
     * Searches through the registered command-objects for one with the given name and returns it.
     * Optional is empty if no command was found.
     * 
     * @param name of the command to search for
     * @return Optional of the command
     */
    private Optional<Command> getCommand(String name) {
        for (Command c : this.commands) {
            if (name.equals(c.getName())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    /**
     * Needs to be called in a loop, calls the command from the Input
     * and calls the game-object to process that data.
     * 
     * @param in    Input   Terminal input from the next line, reads a String
     * @param out   Output  Terminal output as String
     * @param err   Errors  Terminal output as String but writes "Error, " in front
     * @param g     Game    Game-object to call the game logic with the commands on
     */
    public void next(Input in, Output out, Errors err, Game g) {
        this.game = g;
        String read = in.read();
        String[] input = read.split(" ", -1);
        
        Result r = getCommand(input[0])
                .map(cmd -> cmd.execute(this.game, input))
                .orElse(new Result(ErrorMessages.COMMAND_NOT_FOUND, Type.FAILURE));
        
        // check the type of the command and print the message or exit
        if (r.getType() == Type.SUCCESS) {
            out.print(r.getMessage());
        } else if (r.getType() == Type.FAILURE) {
            err.print(r.getMessage());
        } else if (r.getType() == Type.EXIT) {
            Application.exit();
        }
    }
}