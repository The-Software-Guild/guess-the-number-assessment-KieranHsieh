package com.sg.guess_the_number.data;

import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.models.NumberGameRound;

import java.util.List;

/**
 * An abstraction for game and round storage for guess the number games
 */
public interface NumberGameDao {
    /**
     * Retrieves all games stored by the DAO
     * @return ALl games stored by the DAO
     */
    List<NumberGameInstance> getAllGames();

    /**
     * Retrieves a game associated with a given id
     * @param id The ID of the game to retrieve
     * @return The retrieved game
     * @throws GameNotFoundException thrown when the gameId could not be associated with a stored game
     */
    NumberGameInstance getGameById(int id) throws GameNotFoundException;

    /**
     * Gets the rounds played for a given game
     * @param gameId The ID of the game to retrieve rounds for
     * @return A list of rounds played for the given game
     * @throws GameNotFoundException thrown when the given gameId could not be associated with a stored game
     */
    List<NumberGameRound> getGameRounds(int gameId) throws GameNotFoundException;

    /**
     * Creates a new game
     * @param answer The answer to the game
     * @param status The status to create the game with
     * @return The ID of the created game
     */
    int createGame(String answer, ENumberGameStatus status);

    /**
     * Adds a round to the specified game
     * @param gameId The ID of the game to add the round for
     * @param round The round to add to the game
     * @throws GameNotFoundException thrown when the given game ID could not be associated with a stored game
     */
    void addRound(int gameId, NumberGameRound round) throws GameNotFoundException;

    /**
     * Updates the status for a given game
     * @param gameId The ID of the game to update
     * @param status The new status of the game
     * @throws GameNotFoundException thrown when the given game id could not be associated with a stored game
     */
    void updateStatus(int gameId, ENumberGameStatus status) throws GameNotFoundException;

    /**
     * Removes a game from the dao's underlying storage
     * @param gameId The ID of the game to remove
     */
   void removeGame(int gameId) throws GameNotFoundException;
}
