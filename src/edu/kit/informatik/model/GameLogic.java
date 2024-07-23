package edu.kit.informatik.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.informatik.model.Square.State;
import edu.kit.informatik.model.objects.BoardObject;
import edu.kit.informatik.model.objects.FireEngine;
import edu.kit.informatik.presenter.input.Patterns;
import edu.kit.informatik.presenter.output.EliminationType;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * Class that is called for methods that are needed for the control structure of the game itself and its logic.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class GameLogic {
    /**
     * Checks every square to move on or over if it is allowed.
     * Works for the case that Constants.MAX_MOVE_DISTANCE == 2!
     * 
     * @param d         cardinal direction the object wants to move in
     * @param amount    amount of squares (0 < amount <= 2) to move
     * @param b         FireEngine to check for
     * @return Result if successful and if not with ErrorMessage
     */
    private Result checkDirection(Direction d, int amount, BoardObject b, Board board) {
        // First checks for both:
        int distance = Math.abs(amount);
        int[] a = d.intArray();
        int iPos =  b.getRowPosition() + a[0];
        int jPos = b.getColumnPosition() + a[1];
        // check if the first field is BURNING check if IS_FOREST
        Square s = board.getSquareAtPosition(iPos, jPos);
        if (!s.IS_FOREST) {
            return new Result(ErrorMessages.MOVE_NOT_FOREST, Type.FAILURE);
        } else if (s.getState().equals(State.BURNING)) {
            return new Result(ErrorMessages.MOVE_IS_BURNING, Type.FAILURE);
        }
        
        // check if the second square is valid to move over
        // apply the positions of the second square if it is the target, old ones if not
        if (distance == 2) {
            iPos = b.getRowPosition() + 2 * a[0];
            jPos = b.getColumnPosition() + 2 * a[1];
            s = board.getSquareAtPosition(iPos, jPos);
            // check if the first field is BURNING check if IS_FOREST
            if (!s.IS_FOREST) {
                return new Result(ErrorMessages.MOVE_NOT_FOREST, Type.FAILURE);
            } else if (s.getState().equals(State.BURNING)) {
                return new Result(ErrorMessages.MOVE_IS_BURNING, Type.FAILURE);
            }
        }
        // Position of s now at target square
        // check if lightly burning
        s = board.getSquareAtPosition(iPos, jPos);
        if (s.getState().equals(State.SPARKING)) {
            return new Result(ErrorMessages.MOVE_LIGHTLY_BURNING, Type.FAILURE);
        }
        // all checks passed:
        return new Result("", Type.SUCCESS);
    }
    
    /**
     * Returns a list of BoardObjects that hold the references to all fire engines on the board inside.
     * 
     * @param board current Board of the game
     * @return List of all BoardObjects that are fire stations and on the board
     */
    public List<BoardObject> fillFireStations(Board board) {
        List<BoardObject> fireStations = new ArrayList<>();
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Square s = board.getSquareAtPosition(i, j);
                List<BoardObject> l = s.getObjects();
                if (l.size() > 0) {
                    if (l.get(0).getName().matches(Patterns.IS_PLAYER)) {
                        fireStations.add(l.get(0));
                    }
                }
            }
        }
        return fireStations;
    }
    
    /**
     * Takes The position of one and checks if the given position is in the surrounding Squares of one:
     * 
     * @param one   BoardObject, the base to check from
     * @param rowTwo Row-Number of the position to check for
     * @param columnTwo  Column-Number of the position to check for
     * @param sp    Parameter that defines what "surrounding" means
     * @return if two is in the Surrounding squares of one
     */
    public boolean checkIfSurrounding(BoardObject one, int rowTwo, int columnTwo, SurroundingParam sp) {
        int deltaRow = Math.abs(one.getRowPosition() - rowTwo);
        int deltaColumn = Math.abs(one.getColumnPosition() - columnTwo);
        if (deltaRow > 2 || deltaColumn > 2) {
            return false;
        }
        // check in case other params are added later:
        if (sp.equals(SurroundingParam.DIAGONAL) || sp.equals(SurroundingParam.DIRECT)) {
            if ((deltaRow == 0 && deltaColumn == 1) || (deltaRow == 1 && deltaColumn == 0)) {
                return true;
            }
            if (sp.equals(SurroundingParam.DIAGONAL)) {
                if (deltaRow == 1 && deltaColumn == 1) {
                    return true;
                }
            }
        }
        if (sp == SurroundingParam.LINE) {
            // if moved in a direct Line
            if ((deltaRow == 0 && deltaColumn >= 1) || (deltaRow >= 1 && deltaColumn == 0)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the Player can move his fire engine to the specified position.
     * 
     * @param one       the FireEngine to check for
     * @param row       the row position of the square to move on
     * @param column    the column position of the square to move on
     * @param b         the borad that 
     * @return  Result if successful and if not with ErrorMessage
     */
    public Result canMove(BoardObject one, int row, int column, Board b) {
        int deltaRow = one.getRowPosition() - row;
        int deltaColumn = column - one.getColumnPosition();
        // Test if one of them is greater than MAX_MOVE_DISTANCE, cannot move to far
        if (deltaRow > Constants.MAX_MOVE_DISTANCE || deltaColumn > Constants.MAX_MOVE_DISTANCE) {
            return new Result(String.format(ErrorMessages.MOVE_TOO_FAR, Constants.MAX_MOVE_DISTANCE), Type.FAILURE);
        }
        
        Result r = new Result(ErrorMessages.MOVE_UNABLE, Type.FAILURE);
        // since checkIfSurrounding(LINE) has been checked beforehand do this:
        if (deltaRow == 0) {
            // => moves to E/W
            if (deltaColumn > 0) {
                r = this.checkDirection(Direction.E, deltaColumn, one, b);
            } else if (deltaColumn < 0) {
                r = this.checkDirection(Direction.W, deltaColumn, one, b);
            }
        } else if (deltaColumn == 0) {
            // => moves to N/S
            if (deltaRow > 0) {
                r = this.checkDirection(Direction.N, deltaRow, one, b);
            } else if (deltaRow < 0) {
                r = this.checkDirection(Direction.S, deltaRow, one, b);
            }
        }
        return r;
    }
    
    /**
     * Checks if a player can move to a square that is not reachable by moving in a straight line.
     * 
     * @param b         BoardObject to check around
     * @param newRow    row position where it shall move
     * @param newColumn column position where it shall move
     * @param board     BoardObject of the game to operate on
     * @return  Result of the method, if the object can move to the position or not
     */
    public Result checkDiagonals(BoardObject b, int newRow, int newColumn, Board board) {
        // check if the parameters are correct:
        if (Math.abs(b.getRowPosition() - newRow) == 0 || Math.abs(b.getColumnPosition() - newColumn) == 0) {
            return new Result(ErrorMessages.MOVE_DIAGONAL, Type.FAILURE);
        }
        // check if the fire engine can stay on the target location (not move any further)
        Square s = board.getSquareAtPosition(newRow, newColumn);
        if (s.getState() == State.BURNING) {
            return new Result(ErrorMessages.MOVE_IS_BURNING, Type.FAILURE);
        } else if (s.getState() == State.SPARKING) {
            return new Result(ErrorMessages.MOVE_LIGHTLY_BURNING, Type.FAILURE);
        } else if (!s.IS_FOREST) {
            return new Result(ErrorMessages.MOVE_NOT_FOREST, Type.FAILURE);
        }
        // check the two possible squares that the fire engine can move over to access the target location
        int yDirection = newRow - b.getRowPosition();
        int xDirection = newColumn - b.getColumnPosition();
        // check x and y direction
        // if you can walk over none of them, return an error message
        // this is the case either when
        Square y = board.getSquareAtPosition(b.getRowPosition() + yDirection, b.getColumnPosition());
        Square x = board.getSquareAtPosition(b.getRowPosition(), b.getColumnPosition() + xDirection);
        if (y.getState() == State.BURNING && x.getState() == State.BURNING) {
            return new Result(ErrorMessages.MOVE_IS_BURNING, Type.FAILURE);
        } else if (!y.IS_FOREST && !x.IS_FOREST) {
            return new Result(ErrorMessages.MOVE_NOT_FOREST, Type.FAILURE);
        }
        // return that its successful
        return new Result("", Type.SUCCESS);
    }
    
    /**
     * Checks if a player shall be eliminated, eliminates them and checks if any was the first player so the next
     * one can be output in the method for that.
     * 
     * @param board reference to the board to operate on
     * @param pc    reference to the PlayerController of the game
     * @return the EliminationType of the method, if someone was removed or none, 
     *      if it was the first player or if the game is over after this.
     */
    public EliminationType eliminatePlayers(Board board, PlayerController pc) {
        // Initialize Lists of fire engines for every remaining player
        Map<Player, List<FireEngine>> map = new HashMap<>();
        for (Player p : pc.getCycle()) {
            map.put(p, new ArrayList<>());
        }
        // iterate through the board and add the fire engines to the lists for the right player
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                // get the list of BoardObjects on the current square
                Square s = board.getSquareAtPosition(i, j);
                if (!s.IS_FOREST) {
                    continue;
                }
                List<BoardObject> currentList = s.getObjects();
                // if list is not empty get all FireEngines of it
                for (BoardObject b : currentList) {
                    if (b.getName().matches(Patterns.IS_FIRE_ENGINE)) {
                        // get the name of the fire engine and compare it to the Players
                        FireEngine f = (FireEngine) b;
                        String name = String.valueOf(f.getName().charAt(0));
                        // get the player with the name corresponding to the fire engine
                        for (Player p : pc.getCycle()) {
                            if (p.getID().toString().equals(name)) {
                                // add the fire engine to the list in the map of this player
                                map.get(p).add(f);
                            }
                        }
                    }
                }
            }
        }
        // test if the list of one of the players is empty
        List<Player> empty = new ArrayList<>();
        for (Player p : map.keySet()) {
            if (map.get(p).isEmpty()) {
                empty.add(p);
            }
        }
        // test if one of the players to eliminate is the current first one
        EliminationType result = EliminationType.NONE;
        // for all players in the empty-list: eliminate them
        if (!empty.isEmpty()) {
            result = EliminationType.SECONDARY;
            for (int i = empty.size() - 1; i >= 0; i--) {
                if (empty.get(i).equals(pc.currentPlayer())) {
                    result = EliminationType.FIRST;
                }
                boolean gameOver = pc.eliminate(empty.get(i).getID());
                if (gameOver) {
                    result = EliminationType.LOSE;
                }
            }
        }
        // return the result of removing player(s)
        return result;
    }
    
    /**
     * Uses the "intArray" method of the Direction to call the correct neighboring Squares of all heavily burning ones.
     * Calls their burnSquare() method which automatically checks if burned before in that round so that only happens
     * once. Automatically burns all lightly burning Squares to be heavily burning while iterating over the board.
     * 
     * @param d     Direction: N, E, S, W are possible
     * @param board Board instance of the game to burn on
     */
    public void burnToDirection(Direction d, Board board) {
        // gets the int array of d with the 2 values and reads them accordingly
        int[] arr = d.intArray();
        int addRow = arr[0]; //
        int addColumn = arr[1];
        // iterate over all Squares of the board
        for (int i = 0; i < board.getRows(); i++) {
            // get the row position and continue to next iteration if out of bounds
            int r = i + addRow;
            for (int j = 0; j < board.getColumns(); j++) {
                // check if the square is allowed to set itself or sth. else on fire
                // Check if the current Square has been newly set to its state or if its at this State from the last
                Square auto = board.getSquareAtPosition(i, j);
                if (auto.burnedThisRound()) {
                    continue;
                }
                // set the state to heavily burning when it is lightly burning 
                if (auto.getState() == State.SPARKING) {
                    auto.burnSquare();
                    continue;
                }
                // get the column position and continue to next iteration if out of bounds
                int c = j + addColumn;
                if (c < 0 || c >= board.getColumns()) {
                    continue;
                } else if (r < 0 || r >= board.getRows()) {
                    continue;
                }
                // it should proceed normally
                if (auto.getState() == State.BURNING) {
                    Square sq = board.getSquareAtPosition(r, c);
                    sq.burnSquare();
                }
            }
        }
    }
    
    /**
     * Checks all surrounding squares if they're a lake or fire station.
     * 
     * @param e     fire engine to check if refillable
     * @param board Board instance of the game to check on
     * @return <i>true</i> if a lake or fire station is located around e
     */
    public boolean checkForRefill(FireEngine e, Board board) {
        int iPos = e.getRowPosition();
        int jPos = e.getColumnPosition();
        // check the 3x3 part of the board around the fire engine:
        for (int i = -1; i <= 1; i++) {
            int x = iPos + i;
            if (x < 0 || x >= board.getRows()) {
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                int y = jPos + j;
                if (y < 0 || y >= board.getColumns()) {
                    continue;
                }
                if (i == 0 && j == 0) {
                    continue;
                }
                // here we call all the squares around e with x and y coordinate
                if (!board.getSquareAtPosition(x, y).IS_FOREST) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Resets all variables inside the game class that need to be reset after every round.<br>
     * Resets all Squares and all FireEngines on those squares by calling their special reset method.
     * 
     * @param board     current Board instance of the game
     */
    public void roundReset(Board board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Square s = board.getSquareAtPosition(i, j);
                List<BoardObject> sObjects = s.getObjects();
                for (BoardObject b : sObjects) {
                    if (b.getName().matches(Patterns.IS_FIRE_ENGINE)) {
                        FireEngine f = (FireEngine) b;
                        f.roundReset();
                    }
                }
                s.roundReset();
            }
        }
    }
    
    /**
     * Checks if the players have won the game by looking if all squares have been extinguished
     * or rather by searching for the lightly and heavily burning squares and if none are existent the players won.
     * 
     * @param board     current Board instance of the game
     * @return <i>true</i> if the players won the game and it will be over
     */
    public boolean playersWon(Board board) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Square s = board.getSquareAtPosition(i, j);
                if (s.getState() == State.BURNING || s.getState() == State.SPARKING) {
                    return false;
                }
            }
        }
        return true;
    }
}
