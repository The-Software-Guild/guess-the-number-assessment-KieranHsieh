package com.sg.guess_the_number_tests.data;

import com.sg.guess_the_number.data.GameNotFoundException;
import com.sg.guess_the_number.data.NumberGameMemoryDao;
import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.NumberGameRound;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NumberGameMemoryDaoTest {

    @Test
    void getAllGames() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        assertEquals(0, dao.getAllGames().size());
        dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertEquals(1, dao.getAllGames().size());
        assertEquals("1234", dao.getAllGames().get(0).getAnswer());
        assertEquals(ENumberGameStatus.IN_PROGRESS, dao.getAllGames().get(0).getStatus());
    }

    @Test
    void getGameById() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        assertThrows(GameNotFoundException.class, () -> dao.getGameById(1));
        int id = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertDoesNotThrow(() -> dao.getGameById(id));
        try {
            NumberGameInstance instance = dao.getGameById(id);
            assertEquals("1234", instance.getAnswer());
            assertEquals(ENumberGameStatus.IN_PROGRESS, instance.getStatus());
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void getGameRounds() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        assertThrows(GameNotFoundException.class, () -> dao.getGameRounds(1));
        int id = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertDoesNotThrow(() -> dao.getGameRounds(id));
        try {
            List<NumberGameRound> rounds = dao.getGameRounds(id);
            assertEquals(0, rounds.size());
            dao.addRound(id, new NumberGameRound("1234", "e:4:p:0"));
            rounds = dao.getGameRounds(id);
            assertEquals(1, rounds.size());
            assertEquals("1234", rounds.get(0).getGuess());
            assertEquals("e:4:p:0", rounds.get(0).getGuessResult());
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void createGame() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        int id = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        try {
            dao.getGameById(id);
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void updateStatus() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        try {
            assertEquals(dao.getGameById(gameId).getStatus(), ENumberGameStatus.IN_PROGRESS);
            dao.updateStatus(gameId, ENumberGameStatus.FINISHED);
            assertEquals(dao.getGameById(gameId).getStatus(), ENumberGameStatus.FINISHED);
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void removeGame() {
        NumberGameMemoryDao dao = new NumberGameMemoryDao();
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertDoesNotThrow(() -> dao.getGameById(gameId));
        assertDoesNotThrow(() -> dao.removeGame(gameId));
        assertThrows(GameNotFoundException.class, () -> dao.getGameById(gameId));

    }
}