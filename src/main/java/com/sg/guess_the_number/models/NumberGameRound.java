package com.sg.guess_the_number.models;

import java.time.LocalDateTime;

/**
 * An abstraction for a round of a guess the number game
 */
public class NumberGameRound {
    /**
     * The time that the round was created
     */
    private LocalDateTime timestamp = LocalDateTime.now();
    /**
     * The guess associated with the round
     */
    private String guess;
    /**
     * The result of the guess
     */
    private String guessResult;

    /**
     * Constructs a new NumberGuessRound object with a null guess and guess result
     */
    public NumberGameRound() {

    }

    /**
     * Constructs a new NumberGuessRound object with a guess and result
     * @param guess The guess associated with the round
     * @param result The result of the guess
     */
    public NumberGameRound(String guess, String result) {
        this.guess = guess;
        this.guessResult = result;
    }

    /**
     * Retrieves the guess of the round
     * @return The guess of the round
     */
    public String getGuess() {
        return guess;
    }

    /**
     * Sets the guess of the round
     * @param guess The new guess of the round
     */
    public void setGuess(String guess) {
        this.guess = guess;
    }

    /**
     * Retrieves the result of the round's guess
     * @return The result of the round's guess
     */
    public String getGuessResult() {
        return guessResult;
    }

    /**
     * Sets the result of the round's guess
     * @param guessResult The new result of the round's guess
     */
    public void setGuessResult(String guessResult) {
        this.guessResult = guessResult;
    }

    /**
     * Retrieves the time of the round
     * @return The the time of the round
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the the time of the round
     * @param timestamp The new time of the round
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
