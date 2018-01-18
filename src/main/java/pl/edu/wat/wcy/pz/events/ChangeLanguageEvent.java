package pl.edu.wat.wcy.pz.events;

import java.util.EventObject;
import java.util.ResourceBundle;

public class ChangeLanguageEvent extends EventObject {

    private ResourceBundle rb;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ChangeLanguageEvent(Object source, ResourceBundle rb) {
        super(source);
        this.rb = rb;
    }

    public ResourceBundle getRb() {
        return rb;
    }
}
