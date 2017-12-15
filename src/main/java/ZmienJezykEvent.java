import java.util.EventObject;
import java.util.ResourceBundle;

public class ZmienJezykEvent extends EventObject {

    private ResourceBundle rb;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ZmienJezykEvent(Object source, ResourceBundle rb) {
        super(source);
        this.rb = rb;
    }

    public ResourceBundle getRb() {
        return rb;
    }
}
