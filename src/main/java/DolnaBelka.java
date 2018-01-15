import javax.swing.*;
import java.util.ResourceBundle;

public class DolnaBelka extends JPanel implements ZmienKolejListener, ZmienJezykListener, WygranaListener {
    //private static final Logger LOGGER = Logger.getLogger(DolnaBelka.class.getSimpleName(), "LogsMessages");
    private JLabel label;
    private String turnString;
    private String winString;
    private Gra gra;


    public DolnaBelka(OknoGlowne frame, Gra gra) {
        gra.addZmienKolejListener(this);
        gra.addWygranaListener(this);
        this.gra = gra;
        label = new JLabel();
        label.setText(gra.getAktualnyGracz().getNazwa() + "  <- " + turnString);
        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(label);
        });
    }


    @Override
    public void zmienKolej(ZmienKolejEvent event) {
        SwingUtilities.invokeLater(() -> {
            label.setText(event.getAktualnyGracz().getNazwa() + "  <- " + turnString);
            revalidate();
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            turnString = rb.getString("turnString");
            label.setText(gra.getAktualnyGracz().getNazwa() + "  <- " + turnString);
            winString = rb.getString("winString");
        });
    }


    @Override
    public void wygrana(WygranaEvent event) {
        SwingUtilities.invokeLater(() -> label.setText(event.getWygrany().getNazwa() + winString + " ! ! ! "));
        repaint();
    }
}
