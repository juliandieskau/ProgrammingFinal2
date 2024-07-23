package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.input.Parser;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for extinguishing fire.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Extinguish extends Command {

    @Override
    public String getName() {
        return "extinguish";
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
        // check amount of arguments
        if (input.length != 2) {
            return new Result(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, Type.FAILURE);
        }
        // check if the given argument was entered correctly
        Result r = Parser.isValidExtinguish(g, input[1]);
        if (r.getType().equals(Type.FAILURE)) {
            return r;
        }
        // get the values from the now proven correct input:
        String[] split = input[1].split(",", 3);
        
        //extinguish the fire
        Result finalResult = g.extinguish(split[0], Integer.valueOf(split[1]), Integer.valueOf(split[2]));
        
        return finalResult;
    }

}
