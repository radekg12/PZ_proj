import java.util.EventObject;

public class WygranaEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */

    private Gracz wygrany;
    private Gracz przegrany;
    private Gra gra;

    public WygranaEvent(Object source, Gracz wygrany, Gracz przegrany) {
        super(source);
        this.wygrany = wygrany;
        this.przegrany = przegrany;
        this.gra = (Gra) source;
    }

    public Gracz getWygrany() {
        return wygrany;
    }

    public Gracz getPrzegrany() {
        return przegrany;
    }

    public Gra getGra() {
        return gra;
    }
}
