package com.sg.guess_the_number.data;

/**
 * An exception thrown when a game instance could not be found
 */
public class GameNotFoundException extends Exception {
    /**
     * Constructs a new exception in the format "Game "gameId" could not be found."
     * @param gameId The ID of the game that couldn't be found
     */
    public GameNotFoundException(int gameId) {
        super(String.format("Game \"%d\" could not be found.", gameId));
    }
}
