package com.sg.guess_the_number.models;

/**
 * An abstraction for a guess for a NumberGameInstance
 */
public class NumberGameGuess {
    /**
     * The ID of the game instance where the guess is applied
     */
    private int gameId;
    /**
     * The guess applied to the game instance
     */
    private String guess;

    /**
     * Constructs a new NumberGameGuess object
     * @param gameId The ID of the game
     * @param guess The guess
     */
    public NumberGameGuess(int gameId, String guess) {
        this.gameId = gameId;
        this.guess = guess;
    }

    /**
     * Retrieves the game ID of the guess
     * @return The game ID of the guess
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the game ID of the guess
     * @param gameId The new game ID of the guess
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Gets the guess string
     * @return The guess string
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Sets the guess string
     * @param guess The new guess string
     */
    public void setGuess(String guess) {
        this.guess = guess;
    }
}
