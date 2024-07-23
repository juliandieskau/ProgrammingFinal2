package edu.kit.informatik.model;

import java.util.List;

import edu.kit.informatik.model.objects.BoardObject;

/**
 * Class that represents the board of the game and holds a matrix for the Squares and what stands on them
 * as well as methods to access those.
 * 
 * @author Julian Dieskau
 * @version 1.0
 */
public class Board {
    /** Square[rows][columns] board */
    private Square[][] board;
    /** amount of rows of the board */
    private int rows;
    /** amount of columns of the board */
    private int columns;
    
    
    /**
     * Constructor for the board that initializes the size of the board
     * and then sets the states of all squares as well as the predefined objects on the board.
     * 
     * @param m int     number of rows
     * @param n int     number of columns
     * @param squares   List of all Square-objects that need to be set on the board
     */
    public Board(int m, int n, List<Square> squares) {
        this.board =  new Square[m][n];
        this.rows = m;
        this.columns = n;
        this.initBoard(squares);
    }
    
    /**
     * Takes the squares-list and puts its elements on the board.
     * The list is parsed from the command line and takes the elements from top left to bottom right,
     * row by row.
     * 
     * @param squares List<Square>  list of all elements of the board
     */
    private void initBoard(List<Square> squares) {
        // Assumption: this.rows * this.columns = squares.length
        int s = 0;
        // Go through the rows and columns and set it to the next Square from the List
        System.out.println(squares.size());
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                Square sq = squares.get(s);
                // Give the Object on the Square their position and then proceed
                List<BoardObject> list = sq.getObjects();
                if (list.size() > 0) {
                    list.get(0).changePosition(i, j);
                }
                this.setSquare(i, j, sq);
                s++;
            }
        }
    }
    
    /**
     * Sets the Square at the specified row and column on the board.
     * 
     * @param i int     row
     * @param j int     column
     */
    private void setSquare(int i, int j, Square s) {
        this.board[i][j] = s;
    }
    
    /**
     * Returns the Square-Object at the specified position on the board,
     * 
     * @param i Position in the rows;       0 <= i < m
     * @param j Position in the columns;    0 <= j < n
     * @return the square-object at the defined object to change it
     */
    public Square getSquareAtPosition(int i, int j) {
        if (i < 0 || i >= this.rows || j < 0 || j >= this.columns) {
            return null;
        }
        return this.board[i][j];
    }
    
    /**
     * Getter for the amount of rows the board-instance has.
     * 
     * @return {@value #rows} amount of rows of the board
     */
    public int getRows() {
        return this.rows;
    }
    
    /**
     * Getter for the amount of columns the board-instance has.
     * 
     * @return {@value #columns} amount of columns of the board
     */
    public int getColumns() {
        return this.columns;
    }
    
    /**
     * Searches the entire board for a board object with the given name and returns it when found.
     * Otherwise returns <i>null</i>!
     * 
     * @param name  String of the identifier the placed object was given
     * @return the BoardObject with the name when found
     */
    public BoardObject getBoardObject(String name) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                for (BoardObject b : this.board[i][j].getObjects()) {
                    if (b.getName().equals(name)) {
                        return b;
                    }
                }
            }
        }
        return null;
    }
}
