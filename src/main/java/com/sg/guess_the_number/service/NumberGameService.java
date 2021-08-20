package com.sg.guess_the_number.service;

import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.models.NumberGameRound;

import java.util.List;

/**
 * The public interface of a service layer for the guess_the_number backend
 */
public interface NumberGameService {

    /**
     * Creates a new game with a set answer
     * @param answer The answer to the game. If this is null, a random answer is created based on the service
     * @return The created game's id
     */
    int createGame(String answer);

    /**
     * Creates a new game with a set answer
     * @return The created game's id
     */
    int createGame();

    /**
     * Processes a guess for a given game
     * @param gameId The ID of the game to process the guess for
     * @param guess The guess process
     * @return The round for the guess, or null if the game could not be found
     */
    NumberGameRound guessForGame(int gameId, String guess) throws InvalidGuessException;

    /**
     * Retrieves all games stored by the service's storage
     * @return All games stored by the service's storage
     */
    List<NumberGameInstance> getAllGames();

    /**
     * Gets a specified game by it's ID
     * @param gameId The ID of the game to retrieve
     * @return The retrieved game if it exists, or null if it does not
     */
    NumberGameInstance getGameById(int gameId);

    /**
     * Gets all rounds played for a specified game
     * @param gameId The ID of the game to get the rounds of
     * @return A list of all played rounds for the specified game.
     * If the game could not be found, this will be an empty list
     */
    List<NumberGameRound> getRoundsForGame(int gameId);

}
