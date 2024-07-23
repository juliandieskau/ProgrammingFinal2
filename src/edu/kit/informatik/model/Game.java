package edu.kit.informatik.model;

import java.util.List;

import edu.kit.informatik.model.Square.State;
import edu.kit.informatik.model.objects.BoardObject;
import edu.kit.informatik.model.objects.FireEngine;
import edu.kit.informatik.presenter.input.Patterns;
import edu.kit.informatik.presenter.output.EliminationType;
import edu.kit.informatik.presenter.output.ErrorMessages;
import edu.kit.informatik.presenter.output.Messages;
import edu.kit.informatik.presenter.output.NextReturn;
import edu.kit.informatik.presenter.output.NextReturn.Next;
import edu.kit.informatik.presenter.output.Result;
import edu.kit.informatik.presenter.output.Result.Type;

/**
 * The game-class which holds all elements that are in the game as well as methods that define the actions you can 
 * take on them. It is a round based game, so it holds track of the game's state, like current player, the board,
 * all Objects on the board and everything else.
 * <br><br>
 * <b> Warning:</b> <br>
 * Always Check in commands if the game is over or not and if the command can be proceded or not! <br>
 * Might cause problems if not checked!
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Game {
    // Attributes
    private PlayerController pc = new PlayerController();
    private GameLogic logic = new GameLogic();
    private boolean over = false;
    private Board board;
    private List<BoardObject> fireStations;
        
    // Private methods
    /**
     * Fills the Lists of fireEngines so that we are able to access them throughout the game very quickly and
     * check if a object on the board is located around them.
     */
    private void fillObjectLists() {
        this.fireStations = logic.fillFireStations(this.board);
    }
    
    /**
     * Takes The position of one and checks if the given position is in the surrounding Squares of one:
     * 
     * @param one   First BoardObject, the base to check from
     * @param rowTwo        Row Number of the position to check for
     * @param columnTwo     Column Number of the position to check for
     * @param sp    Parameter that defines what "surrounding" means
     * @return      if two is in the Surrounding squares of one
     */
    private boolean checkIfSurrounding(BoardObject one, int rowTwo, int columnTwo, SurroundingParam sp) {
        return logic.checkIfSurrounding(one, rowTwo, columnTwo, sp);
    }
    
    /**
     * Checks if the Player can move his fire engine to the specified position.
     * 
     * @param one       the FireEngine to check for
     * @param row       the row position of the square to move on
     * @param column    the column position of the square to move on
     * @return  Result if successful and if not with ErrorMessage
     */
    private Result canMove(BoardObject one, int row, int column) {
        return logic.canMove(one, row, column, this.board);
    }
    
    /**
     * Checks if a player can move to a square that is not reachable by moving in a straight line.
     * 
     * @param b         BoardObject to check around
     * @param newRow    row position where it shall move
     * @param newColumn column position where it shall move
     * @return  Result of the method, if the object can move to the position or not
     */
    private Result checkDiagonals(BoardObject b, int newRow, int newColumn) {
        // return if it is successful
        return logic.checkDiagonals(b, newRow, newColumn, this.board);
    }
    
    /**
     * Checks all surrounding squares if they're a lake or fire station.
     * 
     * @param e fire engine to refill
     * @return <i>true</i> if a lake or fire station is located around e
     */
    private boolean checkForRefill(FireEngine e) {
        return logic.checkForRefill(e, this.board);
    }
    
    /**
     * Takes the int value of a dice and returns the corresponding Direction.
     * If the given value is not in the valid range of 2 through to 5 null will be returned!
     * 
     * @param dice element{1 - 6}
     */
    private Direction diceToDirection(int dice) {
        switch (dice) {
            case 2:
                return Direction.N;
            case 3:
                return Direction.E;
            case 4:
                return Direction.S;
            case 5:
                return Direction.W;
            default:
                return null;
        }
    }
    
    /**
     * Uses the "intArray" method of the Direction to call the correct neighboring Squares of all heavily burning ones.
     * Calls their burnSquare() method which automatically checks if burned before in that round so that only happens
     * once. Automatically burns all lightly burning Squares to be heavily burning while iterating over the board.
     * 
     * @param d Direction: N, E, S, W are possible
     */
    private void burnToDirection(Direction d) {
        logic.burnToDirection(d, this.board);
    }
    
    /**
     * Checks if a player shall be eliminated, eliminates them and checks if any was the first player so the next
     * one can be output in the method for that.
     * 
     * @return the EliminationType of the method, if someone was removed or none, 
     *      if it was the first player or if the game is over after this.
     */
    private EliminationType eliminatePlayers() {
        // return the result of removing player(s)
        return logic.eliminatePlayers(this.board, this.pc);
    }
    
    /**
     * Resets all variables inside the game class that need to be reset after every round.
     * => Resets all fireEngines and all Squares
     */
    private void roundReset() {
        logic.roundReset(this.board);
    }
    
    /**
     * Checks if the players have won the game.
     * 
     * @return <i>true</i> if the players won the game and it will be over
     */
    private boolean playersWon() {
        return logic.playersWon(this.board);
    }
    
    /**
     * End the game.
     */
    private void endGame() {
        this.over = true;
    }
    
    // Getter
    /**
     * Getter for the current Player that is in turn.
     * 
     * @return PlayerController's current Player
     */
    public Player currentPlayer() {
        return this.pc.currentPlayer();
    }
    
    /**
     * Getter that tells the calling method if the game is over or not.
     * 
     * @return <i>false</i> if a command shall be performed that is used to be inside a game
     */
    public boolean isOver() {
        return this.over;
    }
    
    /**
     * Getter for the current board of the game.
     * 
     * @return the current board of the game at the current state.
     */
    public Board getBoard() {
        return this.board;
    }
    
    /**
     * Getter for the player controller which holds a few important methods such as the boolean if certain commands
     * can be called or if it is waiting for the players to enter their dicing result.
     * 
     * @return the PlayerController to control the order of the next players
     */
    public PlayerController getPlayerController() {
        return this.pc;
    }
    
    // Public methods
    /**
     * Initializes the Game by taking the parsed list with m * n elements and putting it row for row (!) into the
     * array of the Board-object that consists of m rows and n columns. 
     * 
     * @param m     Amount of rows of the board to initialize
     * @param n     Amount of columns of the board to initialize
     * @param list  of all Squares in order from left to right and top to bottom to fill on the board
     */
    public void init(int m, int n, List<Square> list) {
        this.board = new Board(m, n, list);
        this.fillObjectLists();
    }
    
    /**
     * Calls the PlayerController pc to cycle the Players and store which one is in turn.
     * 
     * @return next Player that is in the row
     */
    public Player next() {
        NextReturn next = this.pc.next();
        if (next.getType() == Next.RESET) {
            this.roundReset();
        }
        return next.getPlayer();
    }
    
    /**
     * Tries to buy a fire engine for the current player
     * Returns <i>Type.FAILURE</i> if i and j are invalid or if the balance of the current player is too low
     * Returns <i>Type.SUCCESS</i> if the fire engine was bought and placed
     * 
     * @param i value for the row-position
     * @param j value for the column-position
     * @return  Result of the buying-operation and the amount of reputation points the player has now left
     */
    public Result buyFireEngine(int i, int j) {
        // FIRST check if the positions i and j are surrounding the players fireStation
        String p = this.currentPlayer().getID().toString();
        String id = this.currentPlayer().getNextEngineID();
        for (BoardObject b : this.fireStations) {
            if (b.getName().equals(p)) {
                if (!this.checkIfSurrounding(b, i, j, SurroundingParam.DIAGONAL)) {
                    return new Result(ErrorMessages.NOT_AROUND_FIRE_STATION, Type.FAILURE);
                }
            }
        }
        
        // SECOND check if the Square at i and j is burning
        Square sq = this.board.getSquareAtPosition(i, j);
        if (sq.getState().equals(State.BURNING) || sq.getState().equals(State.SPARKING)) {
            return new Result(ErrorMessages.SQUARE_ON_FIRE, Type.FAILURE);
        }
        
        // THIRD check if the player can pay the money for it, if yes pay
        boolean success = this.pc.currentPlayer().reputation() >= Constants.FIRE_ENGINE_COST;
        if (!success) {
            return new Result(ErrorMessages.BALANCE_TOO_SMALL, Type.FAILURE);
        }
        
        // FOURTH place the engine at the specified position and tell the engine where it's at
        FireEngine b = new FireEngine(id);
        if (sq.placeObject(b)) {
            b.changePosition(i, j);
            this.pc.currentPlayer().pay(Constants.FIRE_ENGINE_COST);
            this.currentPlayer().incrementEngineID();
            return new Result(String.valueOf(currentPlayer().reputation()), Type.SUCCESS);
        } else {
            return new Result(ErrorMessages.SQUARE_NO_FOREST, Type.FAILURE);
        }
    }
    
    /**
     * Method that tries to extinguish a fire at position i,j with the fire-engine that has id engineID
     * Assumes that i and j are valid positions on the board!
     * 
     * @param engineID  String of the name of the engine that should be extinguishing
     * @param i         row-position of the forest field that is to be extinguished
     * @param j         column-position of the forest field that is to be extinguished
     * @return the Result of the operation containing the state of the extinguished square or the error message
     */
    public Result extinguish(String engineID, int i, int j) {
        // test if the engineID is valid
        if (!engineID.matches(Patterns.IS_FIRE_ENGINE)) {
            return new Result(ErrorMessages.FIRE_ENGINE_ID_INVALID, Type.FAILURE);
        }
        // test if a fire engine with given id exists
        // cast is possible, cause the engineID is valid
        FireEngine engine = (FireEngine) this.board.getBoardObject(engineID);
        if (engine == null) {
            return new Result(String.format(ErrorMessages.FIRE_ENGINE_NONEXISTENT, engineID), Type.FAILURE);
        } 
        // test if the found fire engine belongs to the current player
        char[] chars = engineID.toCharArray();
        if (!String.valueOf(chars[0]).matches(this.pc.currentPlayer().getID().toString())) {
            return new Result(ErrorMessages.INVALID_ENGINE_ACCESS, Type.FAILURE);
        }
        // test if i and j are valid
        if (!checkIfSurrounding(engine, i, j, SurroundingParam.DIRECT)) {
            return new Result(ErrorMessages.NOT_NEXT_TO_FIRE_ENGINE, Type.FAILURE);
        }
        // test if the square can be extinguished
        Square sq = this.board.getSquareAtPosition(i, j);
        if (!sq.canExtinguish()) {
            return new Result(ErrorMessages.SQUARE_IS_ALREADY_WET, Type.FAILURE);
        }
        // test if the square has been extinguished by this fire engine before:
        if (engine.extinguishedThisRound(sq)) {
            return new Result(ErrorMessages.EXTINGUISHED_THIS_ROUND, Type.FAILURE);
        }
        
        // test if the engine can extinguish, if yes extinguish with the square too
        Result r = engine.extinguishFire(sq);
        if (r.getType().equals(Type.SUCCESS)) {
            boolean giveRevenue = sq.extinguish();
            if (giveRevenue) {
                this.pc.currentPlayer().obtainSalary(Constants.REPUTATION_SALARY);
            }
            String actionPoints = "," + String.valueOf(engine.getRemainingActionPoints());
            
            // check if the players have won and end the game 
            if (this.playersWon()) {
                this.endGame();
                return new Result(Messages.PLAYERS_WON, Type.SUCCESS);
            }
            return new Result(sq.printSquare() + actionPoints, Type.SUCCESS);
        }
        return r;
    }
    
    /**
     * Tests if it is possible to move the fire engine according to the parameters and if yes does it.
     * 
     * @param id    id of the fire engine to check fore
     * @param iPos  new row position to check if can move to it
     * @param jPos  new column position to check if can move to it
     * @return Result, if the fire engine can move to the specified position on the board
     */
    public Result moveFireEngine(String id, int iPos, int jPos) {
        // check if the position is valid (inside bounds)
        if (iPos < 0 ||  iPos >= this.board.getRows()) {
            return new Result(String.format(ErrorMessages.ROW_OUT_OF_BOUNDS, this.board.getRows()), Type.FAILURE);
        } else if (jPos < 0 || jPos >= board.getColumns()) {
            return new Result(String.format(ErrorMessages.COLUMN_OUT_OF_BOUNDS, 
                    this.board.getColumns()), Type.FAILURE);
        }
        
        // check if the fire engine with the given ID exists
        if (!id.matches(Patterns.IS_FIRE_ENGINE)) {
            return new Result(ErrorMessages.FIRE_ENGINE_ID_INVALID, Type.FAILURE);
        }
        // cast is possible, cause the id of the fire engine is valid
        FireEngine engine = (FireEngine) this.board.getBoardObject(id);
        if (engine == null) {
            return new Result(String.format(ErrorMessages.FIRE_ENGINE_NONEXISTENT, id), Type.FAILURE);
        } 
        // test if the found fire engine belongs to the current player
        char[] chars = id.toCharArray();
        if (!String.valueOf(chars[0]).matches(this.pc.currentPlayer().getID().toString())) {
            return new Result(ErrorMessages.INVALID_ENGINE_ACCESS, Type.FAILURE);
        }
        // check if the positions are on a line with the engine
        // or if they're on the diagonal spot directly next to the engine's old position
        if (checkIfSurrounding(engine, iPos, jPos, SurroundingParam.LINE)) {
            // check if the engine can move to that position
            Result canMove = this.canMove(engine, iPos, jPos);
            if (canMove.getType().equals(Type.FAILURE)) {
                return canMove;
            }
        } else if (checkIfSurrounding(engine, iPos, jPos, SurroundingParam.DIAGONAL)) {
            Result canMove = this.checkDiagonals(engine, iPos, jPos);
            if (canMove.getType().equals(Type.FAILURE)) {
                return canMove;
            }
        } else {
            return new Result(ErrorMessages.MOVE_DIAGONAL, Type.FAILURE);
        }
        
        // check if the fire engine has action points remaining
        if (engine.getRemainingActionPoints() == 0) {
            return new Result(ErrorMessages.NO_ACTION_POINTS, Type.FAILURE);
        }
        
        // call move() on the fire engine and return when it could not do that
        if (!engine.move()) {
            return new Result(ErrorMessages.ALREADY_DONE_ACTION, Type.FAILURE);
        }
        // remove the fire engine from the prior position
        int priorRow = engine.getRowPosition();
        int priorColumn = engine.getColumnPosition();
        Square priorSquare = this.board.getSquareAtPosition(priorRow, priorColumn);
        if (!priorSquare.removeObject(id)) {
            return new Result(ErrorMessages.MOVE_UNABLE, Type.FAILURE);
        }
        // put the engine on the new square on the board
        Square newSquare = this.board.getSquareAtPosition(iPos, jPos);
        newSquare.placeObject(engine);
        engine.changePosition(iPos, jPos);
        return new Result("OK", Type.SUCCESS);
    }
    
    /**
     * Checks if the fire engine with String id is valid and if the tank of it can be refilled,
     * if yes does that and returns then number of remaining action points, 
     * if no returns why it cannot do that.
     * 
     * @param id name of the fire engine
     * @return Result of the operation
     */
    public Result refillFireEngine(String id) {
        // check if id is existing and to the current player
        if (!id.matches(Patterns.IS_FIRE_ENGINE)) {
            return new Result(ErrorMessages.FIRE_ENGINE_ID_INVALID, Type.FAILURE);
        }
        // test if a fire engine with given id exists
        // cast is possible, cause the engineID is valid
        FireEngine engine = (FireEngine) this.board.getBoardObject(id);
        if (engine == null) {
            return new Result(String.format(ErrorMessages.FIRE_ENGINE_NONEXISTENT, id), Type.FAILURE);
        } 
        // test if the found fire engine belongs to the current player
        char[] chars = id.toCharArray();
        if (!String.valueOf(chars[0]).matches(this.pc.currentPlayer().getID().toString())) {
            return new Result(ErrorMessages.INVALID_ENGINE_ACCESS, Type.FAILURE);
        }
        // test if next to lake or fire station
        if (!this.checkForRefill(engine)) {
            return new Result(ErrorMessages.NO_NEAR_REFILL, Type.FAILURE);
        }
        // refill the fire engine
        if (!engine.refillTank()) {
            return new Result(ErrorMessages.CANNOT_REFILL, Type.FAILURE);
        }
        return new Result(String.valueOf(engine.getRemainingActionPoints()), Type.SUCCESS);
    }
    
    /**
     * Algorithm that spreads the fire over the forest according to the dice-integer that is given
     * dice == 2,3,4,5 are for the different cardinal directions, 1 spreads the fire in all
     * 
     * @param dice expecting an integer d with 1 <= d <= 6!
     * @return Result of the method -> "OK" or "lose" as message
     */
    public Result rollFire(int dice) {
        // reset the round variables of the fire engines and squares if the game is not over
        //this.roundReset();
        // 6 corresponds to "no wind" so nothing shall happen:
        if (dice == 6) {
            return new Result("OK", Type.SUCCESS);
        }
        // part for dice == 1,2,3,4,5
        // call a method that converts 2,3,4,5 in a Direction with its 2-dimensional intArray direction
        // exclude direction == 1 for this:
        if (dice != 1) {
            Direction d = this.diceToDirection(dice);
            this.burnToDirection(d);
        }
        // if dice == 1 call the same methods as for 2,3,4,5 but for all of them:
        if (dice == 1) {
            for (int f = 2; f <= 5; f++) {
                Direction d = this.diceToDirection(f);
                this.burnToDirection(d);
            }
        }
        // check if a player shall be eliminated
        EliminationType type = this.eliminatePlayers();
        // check the type for the Result
        String message = Messages.FIRE_ROLLED_NORMALLY;
        if (type == EliminationType.FIRST || type == EliminationType.SECONDARY) {
            message = this.pc.currentPlayer().getID().toString();
        } else if (type == EliminationType.LOSE) {
            // end the game and return the message "lose"
            message = Messages.GAME_OVER;
            this.endGame();
            return new Result(message, Type.SUCCESS);
        }
        // start the next round in the playercontroller
        this.pc.startNextRound();
        return new Result(message, Type.SUCCESS);
    }
}
