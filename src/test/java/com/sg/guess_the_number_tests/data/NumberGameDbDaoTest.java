package com.sg.guess_the_number_tests.data;

import com.sg.guess_the_number.data.GameNotFoundException;
import com.sg.guess_the_number.data.NumberGameDao;
import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.NumberGameRound;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number_tests.TestNumberGameConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestNumberGameConfiguration.class)
@ActiveProfiles("database")
class NumberGameDbDaoTest {
    @Autowired
    private NumberGameDao dao;

    @BeforeEach
    public void initData() throws GameNotFoundException {
        List<NumberGameInstance> games = dao.getAllGames();
        for(NumberGameInstance instance : games) {
            dao.removeGame(instance.getGameId());
        }
    }

    @Test
    void getAllGames() {
        assertEquals(0, dao.getAllGames().size());
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertEquals(1, dao.getAllGames().size());
        assertEquals("1234", dao.getAllGames().get(0).getAnswer());
        assertEquals(ENumberGameStatus.IN_PROGRESS, dao.getAllGames().get(0).getStatus());
        assertEquals(gameId, dao.getAllGames().get(0).getGameId());
    }

    @Test
    void getGameById() {
        assertThrows(GameNotFoundException.class, () -> dao.getGameById(1));
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertDoesNotThrow(() -> dao.getGameById(gameId));
        try {
            NumberGameInstance instance = dao.getGameById(gameId);
            assertEquals("1234", instance.getAnswer());
            assertEquals(ENumberGameStatus.IN_PROGRESS, instance.getStatus());
            assertEquals(gameId, instance.getGameId());
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void getGameRounds() {
        assertThrows(GameNotFoundException.class, () -> dao.getGameRounds(1));
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        assertDoesNotThrow(() -> dao.getGameRounds(gameId));
        try {
            List<NumberGameRound> rounds = dao.getGameRounds(gameId);
            assertEquals(0, rounds.size());
            NumberGameRound curRound = new NumberGameRound("4444", "e:1:p:0");
            dao.addRound(gameId, curRound);
            rounds = dao.getGameRounds(gameId);
            assertEquals(1, rounds.size());
            assertEquals("4444", rounds.get(0).getGuess());
            assertEquals("e:1:p:0", rounds.get(0).getGuessResult());
        }
        catch (GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void createGame() {
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        int gameIdTwo = dao.createGame("5678", ENumberGameStatus.IN_PROGRESS);
        assertNotEquals(gameId, gameIdTwo);
        try {
            NumberGameInstance instance = dao.getGameById(gameId);
            assertEquals(instance.getAnswer(), "1234");
            assertEquals(instance.getStatus(), ENumberGameStatus.IN_PROGRESS);
            NumberGameInstance instanceTwo = dao.getGameById(gameIdTwo);
            assertEquals(instanceTwo.getAnswer(), "5678");
            assertEquals(instanceTwo.getStatus(), ENumberGameStatus.IN_PROGRESS);
        }
        catch(GameNotFoundException e) {
            fail();
        }
    }

    @Test
    void updateStatus() {
        int gameId = dao.createGame("1234", ENumberGameStatus.IN_PROGRESS);
        try {
            NumberGameInstance instance = dao.getGameById(gameId);
            assertEquals(ENumberGameStatus.IN_PROGRESS, instance.getStatus());
            dao.updateStatus(gameId, ENumberGameStatus.FINISHED);
            instance = dao.getGameById(gameId);
            assertEquals(ENumberGameStatus.FINISHED, instance.getStatus());

        }
        catch(GameNotFoundException e) {
            fail();
        }
    }
}