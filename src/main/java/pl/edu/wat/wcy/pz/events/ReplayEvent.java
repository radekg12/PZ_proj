package pl.edu.wat.wcy.pz.events;

import java.util.EventObject;

public class ReplayEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ReplayEvent(Object source) {
        super(source);
    }
}
