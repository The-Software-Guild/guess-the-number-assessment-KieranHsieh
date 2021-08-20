package com.sg.guess_the_number.data;

import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.models.NumberGameRound;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * The in memory implementation of NumberGameDao
 */
@Repository
@Profile("memory")
public class NumberGameMemoryDao implements NumberGameDao {
    /**
     * The games stored by the DAO
     */
    private List<NumberGameInstance> games = new ArrayList<>();
    /**
     * The rounds stored for each game in the DAO
     */
    private HashMap<Integer, List<NumberGameRound>> gameRounds = new HashMap<>();
    /**
     * Retrieves all games stored by the DAO
     *
     * @return ALl games stored by the DAO
     */
    @Override
    public List<NumberGameInstance> getAllGames() {
        return games;
    }

    /**
     * Retrieves a game associated with a given id
     *
     * @param id The ID of the game to retrieve
     * @return The retrieved game
     * @throws GameNotFoundException thrown when the gameId could not be associated with a stored game
     */
    @Override
    public NumberGameInstance getGameById(int id) throws GameNotFoundException {
        checkGameIdBounds(id);
        return games.get(id - 1);
    }

    /**
     * Gets the rounds played for a given game
     *
     * @param gameId The ID of the game to retrieve rounds for
     * @return A list of rounds played for the given game
     * @throws GameNotFoundException thrown when the given gameId could not be associated with a stored game
     */
    @Override
    public List<NumberGameRound> getGameRounds(int gameId) throws GameNotFoundException {
        checkGameIdBounds(gameId);
        return gameRounds.get(gameId);
    }

    /**
     * Creates a new game
     *
     * @param answer The answer to the game
     * @param status The status to create the game with
     * @return The ID of the created game
     */
    @Override
    public int createGame(String answer, ENumberGameStatus status) {
        NumberGameInstance game = new NumberGameInstance();
        game.setGameId(games.size() + 1);
        game.setStatus(ENumberGameStatus.IN_PROGRESS);
        game.setAnswer(answer);
        games.add(game);
        gameRounds.put(game.getGameId(), new ArrayList<>());
        return game.getGameId();
    }

    /**
     * Adds a round to the specified game
     *
     * @param gameId The ID of the game to add a round to
     * @param round  The round to add to the game
     * @throws GameNotFoundException thrown when the game ID could not be associated with a stored game
     */
    @Override
    public void addRound(int gameId, NumberGameRound round) throws GameNotFoundException {
        checkGameIdBounds(gameId);
        gameRounds.get(gameId).add(round);
        gameRounds.get(gameId).sort(Comparator.comparing(NumberGameRound::getTimestamp));
    }

    /**
     * Updates the status for a given game
     *
     * @param gameId The ID of the game to update
     * @param status The new status of the game
     * @throws GameNotFoundException thrown when the given game id could not be associated with a stored game
     */
    @Override
    public void updateStatus(int gameId, ENumberGameStatus status) throws GameNotFoundException {
        checkGameIdBounds(gameId);
        games.get(gameId - 1).setStatus(status);
    }

    /**
     * Removes a game from the dao's underlying storage
     *
     * @param gameId The ID of the game to remove
     */
    @Override
    public void removeGame(int gameId) throws GameNotFoundException {
        checkGameIdBounds(gameId);
        games.remove(gameId - 1);
        gameRounds.remove(gameId);
    }

    /**
     * Validates a given ID and ensures that it is within the bounds of the DAO's storage
     * @param id The ID to validate
     * @throws GameNotFoundException thrown when the ID was invalid
     */
    private void checkGameIdBounds(int id) throws GameNotFoundException {
        assert games.size() == gameRounds.size();
        if((id - 1) < 0 || (id) > games.size()) {
            throw new GameNotFoundException(id);
        }
    }
}
