package com.sg.guess_the_number.models.game;

/**
 * A utility class for creating views from a given instance
 */
public class NumberGameInstanceViewFactory {
    /**
     * Constructs a NumberGameInstanceView from an NumberGameInstance object
     * - Game instances with a status of IN_PROGRESS will construct a NumberGameInstanceIPView
     * - Game instances with a status of FINISHED will construct a NumberGameInstanceFView
     * @param instance The instance to construct a view of
     * @return The constructed view
     */
    public static NumberGameInstanceView createFromInstance(NumberGameInstance instance) {
        switch(instance.getStatus()) {
            case IN_PROGRESS: return new NumberGameInstanceIPView(instance);
            case FINISHED: return new NumberGameInstanceFView(instance);
        }
        assert false;
        return null;
    }
}
