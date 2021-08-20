package com.sg.guess_the_number_tests.service;

import com.sg.guess_the_number.data.NumberGameMemoryDao;
import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.NumberGameRound;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.service.InvalidGuessException;
import com.sg.guess_the_number.service.NumberGameDefaultService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class NumberGameDefaultServiceTest {
    private NumberGameDefaultService service;

    @BeforeEach
    void initializeTests() {
        service = new NumberGameDefaultService(new NumberGameMemoryDao());
    }

    @Test
    void createGame() {
        int gameId = service.createGame();
        NumberGameInstance game = service.getGameById(gameId);
        assertTrue(gameId > 0);
        assertEquals(game.getGameId(), gameId);
        assertSame(game.getStatus(), ENumberGameStatus.IN_PROGRESS);
        assertEquals(4, game.getAnswer().length());

        int gameIdTwo = service.createGame();
        assertNotEquals(game, gameIdTwo);
    }

    private String[] gameGuessHelper(int gameId, String guess) throws InvalidGuessException {
        NumberGameRound round = service.guessForGame(gameId, guess);
        return round.getGuessResult().split(":");
    }

    @Test
    void guessForGame() {
        service.createGame("1234");
        try {
            String[] matches = gameGuessHelper(1, "0001");
            assertEquals(Integer.parseInt(matches[1]), 0);
            assertEquals(Integer.parseInt(matches[3]), 1);

            matches = gameGuessHelper(1, "1000");
            assertEquals(Integer.parseInt(matches[1]), 1);
            assertEquals(Integer.parseInt(matches[3]), 0);

            matches = gameGuessHelper(1, "4444");
            assertEquals(Integer.parseInt(matches[1]), 1);
            assertEquals(Integer.parseInt(matches[3]), 0);

            matches = gameGuessHelper(1, "1111");
            assertEquals(Integer.parseInt(matches[1]), 1);
            assertEquals(Integer.parseInt(matches[3]), 0);

            matches = gameGuessHelper(1, "4321");
            assertEquals(Integer.parseInt(matches[1]), 0);
            assertEquals(Integer.parseInt(matches[3]), 4);

            matches = gameGuessHelper(1, "1234");
            assertEquals(Integer.parseInt(matches[1]), 4);
            assertEquals(Integer.parseInt(matches[3]), 0);

            assertThrows(InvalidGuessException.class, () -> service.guessForGame(1, "12345"));
            assertNull(service.guessForGame(999, "12345"));
        }
        catch(InvalidGuessException e) {
            fail();
        }

    }

    @Test
    void getAllGames() {
        assertEquals(service.getAllGames().size(), 0);
        service.createGame();
        assertEquals(service.getAllGames().size(), 1);
    }

    @Test
    void getGameById() {
        service.createGame();
        service.createGame();
        assertEquals(service.getGameById(1).getGameId(), 1);
        assertEquals(service.getGameById(2).getGameId(), 2);
        assertNull(service.getGameById(999));
    }

    @Test
    void getRoundsForGame() {
        service.createGame();
        service.createGame();
        assertEquals(service.getRoundsForGame(1).size(), 0);
        assertEquals(service.getRoundsForGame(2).size(), 0);
        assertDoesNotThrow(() -> service.guessForGame(1, "0000"));
        assertEquals(service.getRoundsForGame(1).size(), 1);
        assertEquals(service.getRoundsForGame(2).size(), 0);
        assertNull(service.getGameById(999));
    }
}