package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

public class Refill extends Command {

    @Override
    public String getName() {
        return "refill";
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
        // test input arguments
        if (input.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 2), Type.FAILURE);
        }
        // test if id is valid and test if from current player is inside the game object
        // get the result of game.refillFireEngine
        return g.refillFireEngine(input[1]);
    }
}
