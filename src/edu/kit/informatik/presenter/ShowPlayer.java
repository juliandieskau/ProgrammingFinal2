package edu.kit.informatik.presenter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.kit.informatik.model.Game;
import edu.kit.informatik.model.objects.BoardObject;
import edu.kit.informatik.model.objects.FireEngine;
import edu.kit.informatik.presenter.input.Patterns;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Command class for printing the attributes of a player to the console.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class ShowPlayer extends Command {

    @Override
    public String getName() {
        return "show-player";
    }

    @Override
    public Result execute(Game g, String[] input) {
        // test if game is over
        if (g.isOver()) {
            return new Result(ErrorMessages.GAME_ALREADY_OVER, Type.FAILURE);
        }
        // test if no other arguments wer entered
        if (input.length != 1) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, input.length, 1), Type.FAILURE);
        }
        
        String out = "";
        // Add player name and reputation points
        out += g.currentPlayer().getID().toString() + "," + String.valueOf(g.currentPlayer().reputation());
        // Take all FireEngines out of the board
        List<FireEngine> list = new ArrayList<>();
        for (int i = 0; i < g.getBoard().getRows(); i++) {
            for (int j = 0; j < g.getBoard().getColumns(); j++) {
                List<BoardObject> objects = g.getBoard().getSquareAtPosition(i, j).getObjects();
                for (BoardObject b : objects) {
                    if (b.getName().matches(Patterns.IS_FIRE_ENGINE)) {
                        char[] nameChars = b.getName().toCharArray();
                        String player = String.valueOf(nameChars[0]);
                        if (player.matches(g.currentPlayer().getID().toString())) {
                            try {
                                list.add((FireEngine) b);
                            } catch (ClassCastException c) {
                                // do nothing and continue, only need the FireEngines
                            }
                        }
                    }
                }
            }
        }
        // Add line for line the information for all the players fire engines by
        // mapping the stream of all fire engines of this player to
        // <ID>,<tank capacity>,<action points>,<i>,<j> (position),
        // then sort the resulting stream of Strings alphabetically by the name of the fire engine
        // and join them together, separated by a line-break.
        out += "\n" + list.stream()
                .map(f -> f.getName() + "," 
                    + String.valueOf(f.getRemainingWater()) + ","
                    + String.valueOf(f.getRemainingActionPoints()) + "," 
                    + f.getRowPosition() + ","
                    + f.getColumnPosition())
                .sorted().collect(Collectors.joining(System.lineSeparator()));
        return new Result(out, Type.SUCCESS);
    }

}
