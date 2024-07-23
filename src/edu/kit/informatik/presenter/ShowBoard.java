package edu.kit.informatik.presenter;

import edu.kit.informatik.model.Board;
import edu.kit.informatik.model.Game;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for printing the game's board to the console.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class ShowBoard extends Command {

    @Override
    public String getName() {
        return "show-board";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // also usable when game is already over
        // check if no other arguments were given
        if (input.length != 1) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 1), Type.FAILURE);
        }
        // process the output from the board object
        Board b = g.getBoard();
        String result = "";
        for (int i = 0; i < b.getRows(); i++) {
            for (int j = 0; j < b.getColumns(); j++) {
                //result += b.getSquareAtPosition(i, j).printSquare();
                result += b.getSquareAtPosition(i, j).strShowBoard();
                if (j < b.getColumns() - 1) {
                    result += ",";
                }
            }
            if (i < b.getRows() - 1) {
                result += "\n";
            }
        }
        return new Result(result, Type.SUCCESS);
    }

}
