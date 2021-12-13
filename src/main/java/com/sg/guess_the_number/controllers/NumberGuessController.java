package com.sg.guess_the_number.controllers;

import com.sg.guess_the_number.models.ENumberGameStatus;
import com.sg.guess_the_number.models.NumberGameCreationResult;
import com.sg.guess_the_number.models.NumberGameGuess;
import com.sg.guess_the_number.models.game.NumberGameInstance;
import com.sg.guess_the_number.models.NumberGameRound;
import com.sg.guess_the_number.models.game.NumberGameInstanceView;
import com.sg.guess_the_number.models.game.NumberGameInstanceViewFactory;
import com.sg.guess_the_number.service.InvalidGuessException;
import com.sg.guess_the_number.service.NumberGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The main REST controller for the guess_the_number backend
 */
@RestController
public class NumberGuessController {

    /**
     * The service used by the controller
     */
    @Autowired
    private NumberGameService service;

    /**
     * Starts a new game
     * @return The ID of the created game
     */
    @PostMapping("begin")
    public ResponseEntity<NumberGameCreationResult> startGame() {
        return new ResponseEntity<>(new NumberGameCreationResult(service.createGame()), HttpStatus.CREATED);
    }

    /**
     * Processes a guess for a game
     * @param guess The guess information
     * @return The round created from the guess.
     *
     * - If the guess was in an invalid format, HTTP 422 UNPROCESSABLE ENTITY is returned
     * - If the game id of the guess was invalid, HTTP 404 NOT FOUND is returned
     * - If the guess is valid, HTTP 200 OK is returned
     */
    @PostMapping("guess")
    public ResponseEntity<NumberGameRound> guessNumber(@RequestBody NumberGameGuess guess) {
        NumberGameRound round;
        try {
            NumberGameInstance instance = service.getGameById(guess.getGameId());
            if(instance.getStatus() == ENumberGameStatus.FINISHED) {
                return new ResponseEntity<>(null, HttpStatus.LOCKED);
            }
            round = service.guessForGame(guess.getGameId(), guess.getGuess());
        }
        catch(InvalidGuessException e) {
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if(round == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(round);
    }

    /**
     * Gets all games currently stored
     * @return A list of all currently stored games
     */
    @GetMapping("game")
    public List<NumberGameInstanceView> getGames() {
        ArrayList<NumberGameInstanceView> views = new ArrayList<>();
        service.getAllGames().forEach(i -> views.add(NumberGameInstanceViewFactory.createFromInstance(i)));
        return views;
    }

    /**
     * Gets game information for a specified game
     * @param gameId The ID of the game to get information for
     * @return The information about the given game if it was found, or HTTP 404 NOT FOUND if it was not
     */
    @GetMapping("game/{gameId}")
    public ResponseEntity<NumberGameInstanceView> getGameById(@PathVariable int gameId) {
        NumberGameInstance instance = service.getGameById(gameId);
        if(instance == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(NumberGameInstanceViewFactory.createFromInstance(instance));
    }

    /**
     * Gets the rounds played for a given game
     * @param gameId The ID of the game to get rounds for
     * @return The rounds of the given game
     */
    @GetMapping("rounds/{gameId}")
    public List<NumberGameRound> getRoundsById(@PathVariable int gameId) {
        return service.getRoundsForGame(gameId);
    }

}
