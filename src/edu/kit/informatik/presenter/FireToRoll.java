package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for spreading the fire.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class FireToRoll extends Command {

    @Override
    public String getName() {
        return "fire-to-roll";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // test if game is over
        if (g.isOver()) {
            return new Result(ErrorMessages.GAME_ALREADY_OVER, Type.FAILURE);
        }
        // test if the round is over and it can be called
        if (!g.getPlayerController().waitForDice()) {
            return new Result(ErrorMessages.ROUND_NOT_OVER, Type.FAILURE);
        }
        // test if length is right
        if (input.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 2), Type.FAILURE);
        }
        // test if the argument is a number
        int dice = 0;
        try {
            dice = Integer.parseInt(input[1]);
        } catch (NumberFormatException n) {
            return new Result(ErrorMessages.NOT_A_VALID_INT, Type.FAILURE);
        }
        // test if between 1 and 6
        if (dice < 1 || dice > 6) {
            return new Result(String.format(ErrorMessages.NOT_IN_DICE_INT_RANGE, dice), Type.FAILURE);
        }
        // call the method in g that executes the algorithm to roll the fire for the value of the dice accordingly 
        // return the Result if is a Failure
        Result r = g.rollFire(dice);
        if (r.getType().equals(Type.FAILURE)) {
            return r;
        }
        // start next round (else the commands for the round cannot be called again!)
        g.getPlayerController().startNextRound();
        // return the output ("OK" or "lose")
        return r;
    }

}
