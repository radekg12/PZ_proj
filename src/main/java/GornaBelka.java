import javax.swing.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class GornaBelka extends JPanel implements ZmienJezykListener {
    //private static final Logger LOGGER = Logger.getLogger(GornaBelka.class.getSimpleName(), "LogsMessages");
    private JLabel label;

    public GornaBelka(OknoGlowne frame) {
        label = new JLabel();
        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(label);
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            label.setText(rb.getString("gameName"));
        });
    }
}

