package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;
import pl.edu.wat.wcy.pz.score.ScorePanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class ScoreAction extends AbstractAction implements ChangeLanguageListener {
    private MainFrame frame;

    public ScoreAction(MainFrame frame) {
        super(null, new ImageIcon(ScoreAction.class.getClassLoader().getResource("icons/wyniki_24.png")));
        this.frame = frame;
        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> new ScorePanel(frame));
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString("score"));
        });
    }
}


