package edu.kit.informatik.presenter;

import edu.kit.informatik.Application;
import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Messages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for resetting the game object.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Reset extends Command {

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public Result execute(Game g, String[] input) {
        if (input.length != 1) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 1), Type.FAILURE);
        }
        Application.reset();
        return new Result(Messages.GAME_OBJECT_RESET, Type.SUCCESS);
    }

}
