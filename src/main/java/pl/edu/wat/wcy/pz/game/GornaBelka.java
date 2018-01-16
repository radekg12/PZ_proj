package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.events.ZmienJezykEvent;
import pl.edu.wat.wcy.pz.frame.OknoGlowne;
import pl.edu.wat.wcy.pz.listeners.ZmienJezykListener;

import javax.swing.*;
import java.util.ResourceBundle;

public class GornaBelka extends JPanel implements ZmienJezykListener {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.GornaBelka.class.getSimpleName(), "LogsMessages");
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

