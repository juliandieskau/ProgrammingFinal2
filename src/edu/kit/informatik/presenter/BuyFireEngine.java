package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.input.Parser;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for buying fire engines.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class BuyFireEngine extends Command {

    @Override
    public String getName() {
        return "buy-fire-engine";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // test if game is over
        if (g.isOver()) {
            return new Result(ErrorMessages.GAME_ALREADY_OVER, Type.FAILURE);
        }
        // test if it is between rounds
        if (g.getPlayerController().waitForDice()) {
            return new Result(ErrorMessages.WAIT_FOR_DICE, Type.FAILURE);
        }
        // test argument amount
        if (input.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 2) , Type.FAILURE);
        }
        Result r = Parser.isValidPosition(g, input[1]);
        if (r.getType().equals(Type.FAILURE)) {
            return r;
        }
        // is a valid position, the position gets transmitted as a String in the message of r
        // in the form "i,j"
        String[] split = r.getMessage().split(",", 2);
        return g.buyFireEngine(Integer.valueOf(split[0]), Integer.valueOf(split[1]));
    }
}
