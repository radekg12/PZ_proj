package pl.edu.wat.wcy.pz.events;

import pl.edu.wat.wcy.pz.checkers.Player;

import java.util.EventObject;

public class EndOfTimeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private Player palyer;

    public EndOfTimeEvent(Object source, Player player) {
        super(source);
        this.palyer = player;
    }

    public Player getPalyer() {
        return palyer;
    }
}
