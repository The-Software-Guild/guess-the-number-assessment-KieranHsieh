package com.sg.guess_the_number.models.game;

import com.sg.guess_the_number.models.ENumberGameStatus;

/**
 * An abstraction for a specific guess the number game instance
 */
public class NumberGameInstance {
    /**
     * The ID of the game
     */
    private int gameId;
    /**
     * The answer to the game
     */
    private String answer;
    /**
     * The current status of the game
     */
    private ENumberGameStatus status;

    /**
     * Retrieves the gameId of the game
     * @return The gameId of the game
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Sets the gameId of the game
     * @param gameId The new gameId of the game
     */
    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    /**
     * Gets the answer to the game
     * @return The answer to the game
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Sets the answer to the game
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Gets the current status of the game
     * @return The current status of the game
     */
    public ENumberGameStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the game
     * @param status The new current status of the game
     */
    public void setStatus(ENumberGameStatus status) {
        this.status = status;
    }

}
