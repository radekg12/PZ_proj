package pl.edu.wat.wcy.pz.events;

import pl.edu.wat.wcy.pz.checkers.Player;

import java.util.EventObject;

public class ChangeTurnEvent extends EventObject {
    Player currentPlayer;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChangeTurnEvent(Object source, Player currentPlayer) {
        super(source);
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
