package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for quitting the whole application.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Quit extends Command {

    @Override
    public String getName() {
        return "quit";
    }

    @Override
    public Result execute(Game g, String[] input) {
        if (input.length != 1) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 1), Type.FAILURE);
        }
        return new Result("", Type.EXIT);
    }

}
