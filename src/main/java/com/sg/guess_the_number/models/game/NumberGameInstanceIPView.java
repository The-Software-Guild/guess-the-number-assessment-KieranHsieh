package com.sg.guess_the_number.models.game;

import com.sg.guess_the_number.models.ENumberGameStatus;

/**
 * An immutable view of a NumberGameInstance set to the IN_PROGRESS status
 */
public class NumberGameInstanceIPView implements NumberGameInstanceView{
    /**
     * The ID of the view's game instance
     */
    private int gameId;
    /**
     * The status of instance's associated with the view
     */
    private static final ENumberGameStatus status = ENumberGameStatus.IN_PROGRESS;

    /**
     * Constructs a NumberGameInstanceFView from a NumberGameInstance object
     * @param instance The instance to construct the view from
     */
    public NumberGameInstanceIPView(NumberGameInstance instance) {
        gameId = instance.getGameId();
    }

    /**
     * Gets the ID of the view's associated game instance
     * @return The ID of the view's associated game instance
     */
    public int getGameId() {
        return gameId;
    }

    /**
     * Gets the status of the view's associated game instance
     * @return The status of the view's associated game instance
     */
    public ENumberGameStatus getStatus() {
        return status;
    }
}
