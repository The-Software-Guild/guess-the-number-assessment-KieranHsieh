package com.sg.guess_the_number.service;

import com.sg.guess_the_number.data.GameNotFoundException;
import com.sg.guess_the_number.data.NumberGameDao;
import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.models.NumberGameRound;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * The default implementation of the guess_the_number service layer backend
 */
@Component
public class NumberGameDefaultService implements NumberGameService {
    private final NumberGameDao dao;

    public NumberGameDefaultService(NumberGameDao dao) {
        this.dao = dao;
    }

    /**
     * Creates a new game with a set answer
     *
     * @param answer The answer to the game. If this is null, a random answer is created based on the service
     * @return The created game's id
     */
    @Override
    public int createGame(String answer) {
        return dao.createGame(answer, ENumberGameStatus.IN_PROGRESS);
    }

    /**
     * Creates a new game with a set answer
     *
     * @return The created game's id
     */
    @Override
    public int createGame() {
        Random rand = new Random();
        StringBuilder answer = new StringBuilder();
        HashSet<Integer> generatedDigits = new HashSet<>();
        for(int i = 0; i < 4; i ++) {
            int generated = rand.nextInt(10);
            while(generatedDigits.contains(generated)) {
                generated = rand.nextInt(10);
            }
            generatedDigits.add(generated);
            answer.append(generated);
        }
        return createGame(answer.toString());
    }

    /**
     * Processes a guess for a given game
     *
     * @param gameId The ID of the game to process the guess for
     * @param guess  The guess process
     * @return The round for the guess, or null if the game could not be found
     * @throws InvalidGuessException thrown when the guess is in an invalid format
     */
    @Override
    public NumberGameRound guessForGame(int gameId, String guess) throws InvalidGuessException {
        NumberGameInstance curGame;
        // Validation
        try {
            curGame = dao.getGameById(gameId);
        }
        catch(GameNotFoundException e) {
            return null;
        }

        if(guess.length() != curGame.getAnswer().length()) {
            throw new InvalidGuessException(guess);
        }

        MatchResults results = calculateMatchResults(guess, curGame.getAnswer());
        // Update and insert rounds
        NumberGameRound round = new NumberGameRound(guess, results.toString());
        try {
            dao.addRound(gameId, round);
            if(results.fullMatchCount == curGame.getAnswer().length()) {
                dao.updateStatus(curGame.getGameId(), ENumberGameStatus.FINISHED);
            }
        }
        // Should not be reached, since we check earlier, but we have to
        // catch this to make java happy
        catch(GameNotFoundException e) {
            return null;
        }
        return round;
    }

    /**
     * Retrieves all games stored by the service's storage
     *
     * @return All games stored by the service's storage
     */
    @Override
    public List<NumberGameInstance> getAllGames() {
        return dao.getAllGames();
    }

    /**
     * Gets a specified game by it's ID
     *
     * @param gameId The ID of the game to retrieve
     * @return The retrieved game if it exists, or null if it does not
     */
    @Override
    public NumberGameInstance getGameById(int gameId) {
        try {
            return dao.getGameById(gameId);
        }
        catch(GameNotFoundException e) {
            return null;
        }
    }

    /**
     * Gets all rounds played for a specified game
     *
     * @param gameId The ID of the game to get the rounds of
     * @return A list of all played rounds for the specified game.
     * If the game could not be found, this will be an empty list
     */
    @Override
    public List<NumberGameRound> getRoundsForGame(int gameId) {
        try {
            return dao.getGameRounds(gameId);
        }
        catch(GameNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private MatchResults calculateMatchResults(String guess, String answer) {
        // Calculate matches
        int partialMatchCount = 0;
        int fullMatchCount = 0;

        for(int i = 0; i < answer.length(); i ++) {
            // Look for full match
            if(answer.charAt(i) == guess.charAt(i)) {
                fullMatchCount++;
            }
            else {
                // Search for partial match
                for(int j = 0; j < guess.length(); j ++) {
                    if(guess.charAt(j) == answer.charAt(i)) {
                        partialMatchCount++;
                    }
                }
            }
        }
        return new MatchResults(fullMatchCount, partialMatchCount);
    }

    private static final class MatchResults {
        private int fullMatchCount;
        private int partialMatchCount;
        MatchResults(int fullMatchCount, int partialMatchCount) {
            this.fullMatchCount = fullMatchCount;
            this.partialMatchCount = partialMatchCount;
        }

        @Override
        public String toString() {
            return String.format("e:%d:p:%d", fullMatchCount, partialMatchCount);
        }
    }
}
