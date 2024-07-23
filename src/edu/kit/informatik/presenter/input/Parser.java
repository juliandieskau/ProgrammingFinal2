package edu.kit.informatik.presenter.input;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.Application;
import edu.kit.informatik.model.Game;
import edu.kit.informatik.model.Square;
import edu.kit.informatik.model.Square.State;
import edu.kit.informatik.model.objects.FireEngine;
import edu.kit.informatik.model.objects.FireStation;
import edu.kit.informatik.model.objects.Lake;
import edu.kit.informatik.presenter.input.Arguments.ArgumentType;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Class that stands as a intermediary instance between the IO and the Logic classes and parses Input-Strings
 * to Game-Objects and Game-Objects to Output-Strings but also performs needed checks and returns a 
 * ResultType or ArgumentType.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public final class Parser {

    private static int lightly = 0;
    private static int heavily = 0;
    
    /**
     * Utility class, therefore not wanted.
     */
    private Parser() {
        throw new IllegalAccessError("Do not try to instantiate this class!");
    }
    
    private static Arguments testArgs(String[] args) {
        if (args == null || args.length == 0) {
            return new Arguments(null, ArgumentMessages.NO_ARGUMENTS_GIVEN, ArgumentType.INCORRECT);
        } else if (args.length > 1) {
            return new Arguments(null, String.format(ArgumentMessages.TO_MANY_ARGUMENTS, args.length), 
                    ArgumentType.INCORRECT);
        }
        
        // number of arguments is correct <=> args.length == 1
        String arg = args[0];
        // split arg at every comma, at least 5x5 board, plus m and n as arguments => 27 or more args
        String[] split = arg.split(",", -1);
        if (split.length < 2) {
            return new Arguments(null, String.format(ArgumentMessages.WRONG_ARG_AMOUNT, "at least 2", split.length),
                    ArgumentType.INCORRECT);
        }
        
        // try to catch the number of rows m and number of columns n from the first two arguments
        int m; 
        int n;
        try {
            m = Integer.parseInt(split[0]);
            n = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            return new Arguments(null, ArgumentMessages.WRONG_FIRST_TWO_ARGS, ArgumentType.INCORRECT);
        }
        
        // check if m or n are greater than 4
        if (m < 5) {
            return new Arguments(null, String.format(ArgumentMessages.ROWS_SMALLER_FIVE, m), ArgumentType.INCORRECT);
        } else if (n < 5) {
            return new Arguments(null, String.format(ArgumentMessages.COLUMNS_SMALLER_FIVE, m), 
                    ArgumentType.INCORRECT);
        }
        
        // check if m and n are uneven numbers
        if (m % 2 != 1) {
            return new Arguments(null, String.format(ArgumentMessages.EVEN_ROWS_AMOUNT, m), ArgumentType.INCORRECT);
        } else if (n % 2 != 1) {
            return new Arguments(null, String.format(ArgumentMessages.EVEN_COLUMNS_AMOUNT, n), ArgumentType.INCORRECT);
        }
        
        // try to parse every other String from the String[] split into the List<BoardObject>
        if (split.length != m * n + 2) {
            return new Arguments(null, String.format(ArgumentMessages.WRONG_ARG_AMOUNT, m * n + 2, split.length), 
                    ArgumentType.INCORRECT);
        }
        return new Arguments(null, "was", ArgumentType.CORRECT);
    }
    
    private static Arguments testRest(String next, int i, int m, int n, List<Square> list) {
        if (i == 2 + ((n + 1) / 2) - 1 // Top middle
                || i == 2 + m * n - ((n + 1) / 2) // Bottom middle
                || i == 2 + ((m - 1) / 2) * n // Left middle
                || i == 2 + ((m + 1) / 2) * n - 1) { // Right middle
            if (next.matches(Patterns.IS_LAKE)) {
                list.add(new Square(State.DRY, new Lake(), false));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, Patterns.IS_LAKE),
                        ArgumentType.INCORRECT);
            }
        // All other squares have to be forest squares without board-objects on them
        } else {
            if (next.matches(Patterns.IS_FOREST)) {
                switch (next) {
                    case "d":
                        list.add(new Square(State.DRY));
                        break;
                    case "w":
                        list.add(new Square(State.WET));
                        break;
                    case "+":
                        list.add(new Square(State.SPARKING));
                        lightly += 1;
                        break;
                    case "*":
                        list.add(new Square(State.BURNING));
                        heavily += 1;
                        break;
                    default:
                        // if the tests beforehand are correct this should never be reached, but to avoid mistakes:
                        return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                                Patterns.IS_FOREST), ArgumentType.INCORRECT);
                } 
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                        Patterns.IS_FOREST), ArgumentType.INCORRECT);
            }
        }
        return new Arguments(null, String.valueOf(lightly + heavily), ArgumentType.CORRECT);
    }
    
    private static Arguments firstTest(String next, int i, int m, int n, List<Square> list) {
        // Test the squares for the Players -> Add FireStations
        if (i == 2) {
            // board[0][0]: Always Player A
            if (next.matches(Patterns.IS_PLAYER_A)) {
                list.add(new Square(State.DRY, new FireStation(Patterns.IS_PLAYER_A), false));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                        Patterns.IS_PLAYER_A), ArgumentType.INCORRECT);
            }
        } else if (i == 2 + n - 1) {
            // board[0][n-1]: Always Player D
            if (next.matches(Patterns.IS_PLAYER_D)) {
                list.add(new Square(State.DRY, new FireStation(Patterns.IS_PLAYER_D), false));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                        Patterns.IS_PLAYER_D), ArgumentType.INCORRECT);
            }
        } else if (i == 2 + m * n - 1) {
            // board[m-1][n-1]: Always Player B
            if (next.matches(Patterns.IS_PLAYER_B)) {
                list.add(new Square(State.DRY, new FireStation(Patterns.IS_PLAYER_B), false));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                        Patterns.IS_PLAYER_B), ArgumentType.INCORRECT);
            }
        } else if (i == 2 + (m - 1) * n) {
            // board[m-1][0]: Always Player C
            if (next.matches(Patterns.IS_PLAYER_C)) {
                list.add(new Square(State.DRY, new FireStation(Patterns.IS_PLAYER_C), false));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, 
                        Patterns.IS_PLAYER_C), ArgumentType.INCORRECT);
            }
        // The first fire engine for each player needs to be positioned on the diagonal square
        //      next to each of their FireStations.
        // add a dry square and the fire engine on top of that square
        } else if (i == 2 + n + 1) {
            // board[1][2] == diagonal for Player A
            if (next.matches(Patterns.IS_PLAYER_A + "0")) {
                list.add(new Square(State.DRY, new FireEngine("A0"), true));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, "A0"),
                        ArgumentType.INCORRECT);
            }
        } else if (i == 2 + n + (n - 2)) {
            // board[1][n-2] == diagonal for Player D
            if (next.matches(Patterns.IS_PLAYER_D + "0")) {
                list.add(new Square(State.DRY, new FireEngine("D0"), true));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, "D0"),
                        ArgumentType.INCORRECT);
            }
        } else if (i == 2 + n * (m - 2) + 1) {
            // board[m-2][1] == diagonal for player C
            if (next.matches(Patterns.IS_PLAYER_C + "0")) {
                list.add(new Square(State.DRY, new FireEngine("C0"), true));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, "C0"),
                        ArgumentType.INCORRECT);
            }
        } else if (i == 2 + n * (m - 1) - 2) {
            // board[m-2][n-2] == diagonal for player B
            if (next.matches(Patterns.IS_PLAYER_B + "0")) {
                list.add(new Square(State.DRY, new FireEngine("B0"), true));
            } else {
                return new Arguments(null, String.format(ArgumentMessages.ARGUMENT_WRONG, next, "B0"),
                        ArgumentType.INCORRECT);
            }
        } else {
            return new Arguments(null, "correct", ArgumentType.CORRECT);
        }
        return new Arguments(null, "lol", ArgumentType.CORRECT);
    }
    
    /**
     * Parses the command line arguments to the wanted format.
     * 
     * @param args  String[]    command line arguments (should be one, with commas separated)
     * @return      Arguments   List<BoardObject> as well as ArgumentType inside
     */
    public static Arguments parseArgs(String[] args) {
        List<Square> list = new ArrayList<>();
        
        Arguments a = testArgs(args);
        if (a.getType() == ArgumentType.INCORRECT) {
            return a;
        }
        
        // try to catch the number of rows m and number of columns n from the first two arguments
        String[] split = args[0].split(",", -1);
        int m = Integer.parseInt(split[0]);
        int n = Integer.parseInt(split[1]);
        
        // iterate through all arguments from the comma-separated-list:
        for (int i = 2; i < split.length; i++) {
            String next = split[i];
            // Test the squares for the Players -> Add FireStations
            Arguments first = firstTest(next, i, m, n, list);
            if (first.getType() == ArgumentType.INCORRECT) {
                return first;
            } else {
                Arguments rest = testRest(next, i, m, n, list);
                if (rest.getType() == ArgumentType.INCORRECT) {
                    return rest;
                }
            }
        }
        // test if there is no burning square
        if (lightly == 0) {
            return new Arguments(null, ArgumentMessages.NO_SPARKING_SQUARE, ArgumentType.INCORRECT);
        } else if (heavily == 0) {
            return new Arguments(null, ArgumentMessages.NO_BURNING_SQUARE, ArgumentType.INCORRECT);
        } 
        
        // all tests have been passed, return the correct list:
        Application.setBoardConstants(m, n);
        
        return new Arguments(list, "", ArgumentType.CORRECT);
    }
    
    /**
     * Tests if the given String matches two integers, separated by a single comma
     * and then tests if the position is inside the boards bounds.
     * 
     * @param g Game object
     * @param s String to test
     * @return Result of the testing
     */
    public static Result isValidPosition(Game g, String s) {
        String[] split = s.split(",", 2);
        // test if the split string is of correct length
        if (split.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, split.length, 2), Type.FAILURE);
        }
        
        int i = 0;
        int j = 0;
        try {
            i = Integer.valueOf(split[0]);
            j = Integer.valueOf(split[1]);
        } catch (NumberFormatException e) {
            return new Result(ErrorMessages.INVALID_POSITION_REPRESENTATION, Type.FAILURE);
        }
        if (i < 0 || i >= g.getBoard().getRows()) {
            return new Result(String.format(ErrorMessages.ROW_OUT_OF_BOUNDS, i, g.getBoard().getRows()), Type.FAILURE);
        } else if (j < 0 || j >= g.getBoard().getColumns()) {
            return new Result(String.format(ErrorMessages.COLUMN_OUT_OF_BOUNDS, i, g.getBoard().getColumns()),
                    Type.FAILURE);
        }
        return new Result(String.valueOf(i) + "," + String.valueOf(j), Type.SUCCESS);
    }
    
    /**
     * Tests if the given String matches a fire engine id and two correct integers, separated by a comma
     * 
     * @param g Game object
     * @param s String to test
     * @return Result of the testing
     */
    public static Result isValidExtinguish(Game g, String s) {
        String[] split = s.split(",", 2);
        // test if the split string is of correct length
        if (split.length != 2) {
            return new Result(String.format(ErrorMessages.INVALID_AMOUNT_OF_ARGUMENTS, split.length, 2), Type.FAILURE);
        }
        // test if split[0] has the correct form
        if (!split[0].matches(Patterns.IS_FIRE_ENGINE)) {
            return new Result(ErrorMessages.FIRE_ENGINE_ID_INVALID, Type.FAILURE);
        }
        
        // test if split[1] is a correct position
        Result pos = isValidPosition(g, split[1]);
        if (pos.getType().equals(Type.FAILURE)) {
            return pos;
        }
        // return the success
        return new Result("", Type.SUCCESS);
    }
}
