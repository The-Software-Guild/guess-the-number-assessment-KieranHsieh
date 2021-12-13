package com.sg.guess_the_number.data;

import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.NumberGameRound;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

/**
 * The database backed implementation of NumberGameDao
 */
@Repository
@Profile("database")
public class NumberGameDbDao implements NumberGameDao {
    /**
     * The JDBC template used by the DAO
     */
    private final JdbcTemplate template;

    /**
     * Constructs the NumberGameDbDao with a specified template
     * @param template The template used byt he NumberGameDbDao
     */
    @Autowired
    public NumberGameDbDao(JdbcTemplate template) {
        this.template = template;
    }

    /**
     * Retrieves all games stored by the DAO
     *
     * @return ALl games stored by the DAO
     */
    @Override
    public List<NumberGameInstance> getAllGames() {
        final String sql = "SELECT game.gameId, game.answer, progress.progressDesc FROM game " +
                "INNER JOIN progress ON progress.progressId = game.progressId " +
                "GROUP BY game.gameId;";
        return template.query(sql, new GameInstanceMapper());
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
        final String sql = "SELECT game.gameId, game.answer, progress.progressDesc FROM game " +
                "INNER JOIN progress ON progress.progressId = game.progressId " +
                "WHERE game.gameId = ? " +
                "GROUP BY game.gameId;";
        try {
            return template.queryForObject(sql, new GameInstanceMapper(), id);
        }
        catch(EmptyResultDataAccessException e) {
            throw new GameNotFoundException(id);
        }
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
        getGameById(gameId);
        final String sql = "SELECT rounds.guess, rounds.result, rounds.roundTime FROM rounds " +
                "WHERE rounds.gameId = ? " +
                "ORDER BY rounds.roundTime ASC;";
        return template.query(sql, new GameRoundMapper(), gameId);
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
        final String sql = "INSERT INTO game(answer, progressId) " +
                "VALUES(?, ?);";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        template.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, answer);
            statement.setInt(2, status.ordinal() + 1);
            return statement;
        }, keyHolder);
        assert keyHolder.getKey() != null;
        return keyHolder.getKey().intValue();
    }

    /**
     * Adds a round to the specified game
     *
     * @param gameId The ID of the game to add the round for
     * @param round  The round to add to the game
     */
    @Override
    public void addRound(int gameId, NumberGameRound round) {
        final String sql = "INSERT INTO rounds (gameId, guess, result, roundTime) " +
                "VALUES (?, ?, ?, ?);";
        template.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, gameId);
            statement.setString(2, round.getGuess());
            statement.setString(3, round.getGuessResult());
            Timestamp timestamp = Timestamp.valueOf(round.getTimestamp());
            statement.setTimestamp(4, timestamp);
            return statement;
        });
    }

    /**
     * Updates the status for a given game
     *
     * @param gameId The ID of the game to update
     * @param status The new status of the game
     */
    @Override
    public void updateStatus(int gameId, ENumberGameStatus status) {
        final String sql = "UPDATE game SET progressId = ? WHERE gameId = ?;";
        template.update((Connection conn) -> {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, status.ordinal() + 1);
            statement.setInt(2, gameId);
            return statement;
        });
    }

    /**
     * Removes a game from the dao's underlying storage
     *
     * @param gameId The ID of the game to remove
     */
    @Override
    public void removeGame(int gameId) {
        final String delRounds = "DELETE FROM rounds WHERE rounds.gameId = ?";
        final String delGame = "DELETE FROM game WHERE game.gameId = ?";
        template.update(delRounds, gameId);
        template.update(delGame, gameId);
    }

    /**
     * A utility class used to map NumberGameInstance objects to database entries
     */
    private static final class GameInstanceMapper implements RowMapper<NumberGameInstance> {
        @Override
        public NumberGameInstance mapRow(ResultSet resultSet, int i) throws SQLException {
            NumberGameInstance instance = new NumberGameInstance();
            instance.setGameId(resultSet.getInt("game.gameId"));
            instance.setAnswer(resultSet.getString("game.answer"));
            instance.setStatus(ENumberGameStatus.valueOf(resultSet.getString("progress.progressDesc")));
            return instance;
        }
    }

    /**
     * A utility class used to map NumberGameRound objects to database entries
     */
    private static final class GameRoundMapper implements RowMapper<NumberGameRound> {
        @Override
        public NumberGameRound mapRow(ResultSet resultSet, int i) throws SQLException {
            NumberGameRound round = new NumberGameRound();
            round.setGuess(resultSet.getString("rounds.guess"));
            round.setGuessResult(resultSet.getString("rounds.result"));
            Timestamp timestamp = resultSet.getTimestamp("rounds.roundTime");
            round.setTimestamp(timestamp.toLocalDateTime());
            return round;
        }
    }
}
