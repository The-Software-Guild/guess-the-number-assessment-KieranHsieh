package com.sg.guess_the_number.models.game;

import com.sg.guess_the_number.models.ENumberGameStatus;

/**
 * An immutable view of a NumberGameInstance set to the FINISHED status
 */
public class NumberGameInstanceFView implements NumberGameInstanceView {
    /**
     * The ID of the game instance
     */
    private int gameId;
    /**
     * The answer to the game instance
     */
    private String answer;
    /**
     * The status associated with the game instance
     */
    private static final ENumberGameStatus status = ENumberGameStatus.FINISHED;

    /**
     * Constructs a NumberGameInstanceFView from a NumberGameInstance object
     * @param instance The instance to construct the view from
     */
    public NumberGameInstanceFView(NumberGameInstance instance) {
        gameId = instance.getGameId();
        answer = instance.getAnswer();
    }

    /**
     * Gets the game ID of the view's game instance
     * @return The game instance's id
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Gets the answer to the view's game instance
     * @return The answer to the view's game instance
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets the status of the view's game instance
     * @return The status of the view's game instance
     */
    public ENumberGameStatus getStatus() {
        return status;
    }
}
