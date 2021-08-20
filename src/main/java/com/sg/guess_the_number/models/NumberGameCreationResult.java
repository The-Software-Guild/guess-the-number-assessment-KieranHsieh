package com.sg.guess_the_number.models;

/**
 * An abstraction for the result of a created NumberGameInstance
 */
public class NumberGameCreationResult {
    /**
     * The ID of the game instance
     */
    private int gameId;

    /**
     * Constructs a new NUmberGameCreationResult
     * @param gameId The ID of the result's game instance
     */
    public NumberGameCreationResult(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Retrieves the ID of the result's game instance
     * @return The ID of the result's game instance
     */
    public int getGameId() {
        return gameId;
    }
}
