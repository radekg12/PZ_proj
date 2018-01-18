package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.util.ResourceBundle;

public class TopPanel extends JPanel implements ChangeLanguageListener {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.TopPanel.class.getSimpleName(), "LogsMessages");
    private JLabel titleLabel;

    public TopPanel(MainFrame frame) {
        titleLabel = new JLabel();
        ChangeLanguageAction.addChangeLanguageListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(titleLabel);
        });
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            titleLabel.setText(rb.getString("gameName"));
        });
    }
}

