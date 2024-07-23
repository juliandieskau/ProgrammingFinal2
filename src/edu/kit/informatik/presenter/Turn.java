package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for giving the turn to the next player.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Turn extends Command {

    @Override
    public String getName() {
        return "turn";
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
        // check if no arguments were given except the command
        if (input.length != 1) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 1), Type.FAILURE);
        }
        // execute command
        g.next();
        return new Result(g.currentPlayer().toString(), Type.SUCCESS);
    }

}
