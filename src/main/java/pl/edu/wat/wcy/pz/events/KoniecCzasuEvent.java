package pl.edu.wat.wcy.pz.events;

import pl.edu.wat.wcy.pz.checkers.Gracz;

import java.util.EventObject;

public class KoniecCzasuEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    private Gracz palyer;

    public KoniecCzasuEvent(Object source, Gracz player) {
        super(source);
        this.palyer = player;
    }

    public Gracz getPalyer() {
        return palyer;
    }
}
