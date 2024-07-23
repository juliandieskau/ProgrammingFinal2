package edu.kit.informatik.presenter;

import java.util.List;
import java.util.stream.Collectors;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.model.Square;
import edu.kit.informatik.model.objects.BoardObject;
import edu.kit.informatik.presenter.input.Parser;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for printing a single field to the console.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class ShowField extends Command {

    @Override
    public String getName() {
        return "show-field";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // usable even if game is already over
        // check if the amount of arguments entered is correct
        if (input.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 2), Type.FAILURE);
        }
        // initialization of the given position
        Result r = Parser.isValidPosition(g, input[1]);
        if (r.getType().equals(Type.FAILURE)) {
            return r;
        }
        String[] s = r.getMessage().split(",", 2);
        int i = Integer.valueOf(s[0]);
        int j = Integer.valueOf(s[1]);
        
        // Put together the output-String
        Square square = g.getBoard().getSquareAtPosition(i, j);
        // msg = message (not Monosodium glutamate)
        String msg = "";
        List<BoardObject> list = square.getObjects();
        // If is a fire station or lake, print that out
        if (!square.IS_FOREST) {
            msg = list.get(0).getName();
        } else {
            // If is a forest, print state and all objects on it
            msg = square.printSquare();
            // Add the names of the BoardObjects on the square in alphabetical order to the msg that will be output,
            // by mapping the BoardObjects on this square/ field to the respective String by calling the 
            // getName-method of this instance and sorting them afterwards. 
            // Then those Strings will be collected and joined together, 
            // adding them to the msg-String, separated by a comma.
            String names = list.stream().map(BoardObject::getName)
                    .sorted().collect(Collectors.joining(","));
            if (!names.isEmpty()) {
                msg += "," + names;
            }
        }
        return new Result(msg, Type.SUCCESS);
    }

}
