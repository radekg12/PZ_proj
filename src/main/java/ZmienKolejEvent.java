import java.util.EventObject;

public class ZmienKolejEvent extends EventObject {
    Gracz aktualnyGracz;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public ZmienKolejEvent(Object source, Gracz aktualnyGracz) {
        super(source);
        this.aktualnyGracz = aktualnyGracz;
    }

    public Gracz getAktualnyGracz() {
        return aktualnyGracz;
    }
}
