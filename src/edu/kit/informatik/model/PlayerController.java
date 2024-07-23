package edu.kit.informatik.model;

import java.util.ArrayList;
import java.util.List;

import edu.kit.informatik.model.Player.Players;
import edu.kit.informatik.presenter.output.NextReturn;
import edu.kit.informatik.presenter.output.NextReturn.Next;

/**
 * Class that controls the order of the players and returns which one comes next.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class PlayerController {
    private List<Player> cycle;
    private Player roundElement;
    private boolean waitForDice = false;
    private int moveCountDown; // counts the current player number down every round
    
    /**
     * This Constructor initiates all four Players and saves which element was the first in this cycle.
     */
    public PlayerController() {
        this.cycle = new ArrayList<>();
        this.cycle.add(new Player(Players.A));
        this.cycle.add(new Player(Players.B));
        this.cycle.add(new Player(Players.C));
        this.cycle.add(new Player(Players.D));
        this.roundElement = this.cycle.get(0);
        this.moveCountDown = this.cycle.size();
    }
    
    /**
     * Shifts all Players one Position to the left and queues the first as the last.
     */
    private void rotateCycle() {
        Player first = this.cycle.get(0);
        for (int i = 0; i < this.cycle.size() - 1; i++) {
            this.cycle.set(i, this.cycle.get(i + 1));
        }
        this.cycle.set(this.cycle.size() -1, first);
    }
    
    /**
     * Checks if the player in the parameter p is present in the cycle-array.
     * 
     * @param p Player to check presence 
     * @return {@value true} if present in cycle
     */
    private boolean isPlayerAlive(Player p) {
        for (Player pl : this.cycle) {
            if (pl.getID().equals(p.getID())) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Getter for the array of Players that are currently in the cycle, in correct order.
     * 
     * @return the current cycle of players in form of an Array
     */
    public List<Player> getCycle() {
        return this.cycle;
    }
    
    /**
     * Getter for the first element of the array for the Player-order.
     * 
     * @return this.cycle[0] Player
     */
    public Player currentPlayer() {
        return this.cycle.get(0);
    }
    
    /**
     * Rotates the Cycle once to get the next Player and twice if the current cycle is over,
     * so that in the next one a different Player starts the round.
     * 
     * @return the next Player that has his/her turn
     */
    public NextReturn next() {
        this.moveCountDown--;
        if (this.cycle.size() == 1) {
            this.waitForDice = true;
            return new NextReturn(this.cycle.get(0), Next.RESET);
        }
        if (this.cycle.get(1) == this.roundElement) {
            this.rotateCycle();
            this.rotateCycle();
            this.roundElement = this.cycle.get(0);
            this.waitForDice = true;
            return new NextReturn(this.cycle.get(0), Next.RESET);
        }
        if (!this.isPlayerAlive(this.roundElement) && moveCountDown == 0) {
            this.rotateCycle();
            this.roundElement = this.cycle.get(0);
            this.waitForDice = true;
            return new NextReturn(this.cycle.get(0), Next.RESET);
        }
        this.rotateCycle();
        return new NextReturn(this.cycle.get(0), Next.PROCEED);
    }
    
    /**
     * Removes a Player from the Players-List if the Player with identifier p exists in it
     * and returns if the game is lost for the Players.<br>
     * 
     * @param p Players-id
     * @return {@value true} if the removed Player was the last Player and the game shall be over
     *  {@value false} if he was not the last Player
     */
    public boolean eliminate(Players p) {
        // add all to the list that should not be eliminated
        List<Player> newCycle = new ArrayList<>();
        for (int i = 0; i < this.cycle.size(); i++) {
            if (!this.cycle.get(i).toString().equals(p.toString())) {
                newCycle.add(cycle.get(i));
            }
        }
        // if size is 0 return that the game is over and the players have lost
        if (newCycle.size() == 0) {
            return true;
        }
        this.cycle = newCycle;
        return false;
    }
    
    /**
     * Getter for the waitForDice boolean.<br>
     * Indicates that the next command has to be "fire-to-roll" or some print-command and
     * the game will not go on without that.
     * 
     * @return {@value true} if the normal game flow shall stop and the waiting for the "fire-to-roll" 
     *      command starts.
     */
    public boolean waitForDice() {
        return this.waitForDice;
    }
    
    /**
     * Sets the round variables of the Player Controller.<br>
     * Indicates that the round will go on and normal "next player" requests can be used again
     * as well as other game functions.
     */
    public void startNextRound() {
        this.waitForDice = false;
        this.moveCountDown = this.cycle.size();
    }
}
