package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.input.Parser;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for moving fire engines.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Move extends Command {

    @Override
    public String getName() {
        return "move";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // test if game is over
        if (g.isOver()) {
            return new Result(ErrorMessages.GAME_ALREADY_OVER, Type.FAILURE);
        }
        // check if it is between rounds
        if (g.getPlayerController().waitForDice()) {
            return new Result(ErrorMessages.WAIT_FOR_DICE, Type.FAILURE);
        }
        // first check input and parse it to usable variables
        if (input.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 2), Type.FAILURE);
        }
        Result r = Parser.isValidExtinguish(g, input[1]);
        if (r.getType().equals(Type.FAILURE)) {
            return r;
        }
        //input is valid now parse to variables:
        String[] split = input[1].split(",", 3);
        int i = 0;
        int j = 0;
        try {
            i = Integer.parseInt(split[1]);
            j = Integer.parseInt(split[2]);
        } catch (NumberFormatException n) {
            return new Result(ErrorMessages.MOVE_UNABLE, Type.FAILURE);
        }
        // calls the move method on the game object which returns the Result of the operation
        return g.moveFireEngine(split[0], i, j);
    }

}
