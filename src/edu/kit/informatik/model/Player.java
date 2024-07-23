package edu.kit.informatik.model;

/**
 * Class that represents a Player and methods for his balance in the game.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Player {
    private Players p;
    private int balance; //reputation points
    private int lastEngineID = 0;
    
    /**
     * Constructor that sets the Player to its Players enum and its balance to 0.
     * 
     * @param p identifier of type Players of the Player
     */
    public Player(Players p) {
        this.p = p;
        this.balance = 0;
    }
    
    /**
     * Getter for the amount of reputation points (balance) the Player has.
     * 
     * @return balance of reputation points
     */
    public int reputation() {
        return this.balance;
    }
    
    /**
     * Reduces the balance by @param amount if the balance is at least as big as the amount.
     * 
     * @return {@value true} if the payment was successful {@value false} if not
     */
    public boolean pay(int amount) {
        if (this.balance < amount) {
            return false;
        } else {
            this.balance -= amount;
            return true;
        }
    }
    
    /**
     * Increases the balance by the given amount
     * 
     * @param amount of money
     */
    public void obtainSalary(int amount) {
        this.balance += amount;
    }
    
    /**
     * Getter for the "ID" of a Player, stored as enum-type
     * 
     * @return id of the player
     */
    public Players getID() {
        return this.p;
    }
    
    /**
     * Computes the next ID a fire engine for this Player should get.
     * Call incrementEngineID every time after successfully adding a new fire Engine.
     * 
     * @return Player-name plus the counter for all fire engines
     */
    public String getNextEngineID() {
        int id = Integer.valueOf(this.lastEngineID) + 1;
        return this.p.toString() + String.valueOf(id);
    }
    
    /**
     * Increments the id that was used for already bought engines so no id will ever be set twice.
     */
    public void incrementEngineID() {
        this.lastEngineID++;
    }
    
    @Override
    public String toString() {
        return this.p.toString();
    }
    
    /**
     * Enum for defining the possible Players
     * 
     * @author Julian Dieskau
     * @version 1.0
     */
    public enum Players {
        /** Player A */
        A{
            @Override
            public String toString() {
                return "A";
            }
        },
        /** Player B */
        B{
            @Override
            public String toString() {
                return "B";
            }
        },
        /** Player C */
        C{
            @Override
            public String toString() {
                return "C";
            }
        },
        /** Player D */
        D{
            @Override
            public String toString() {
                return "D";
            }
        }
    }
}