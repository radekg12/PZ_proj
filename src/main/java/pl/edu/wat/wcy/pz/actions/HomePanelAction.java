package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.HomePanel;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class HomePanelAction extends AbstractAction implements ChangeLanguageListener {
    MainFrame frame;

    public HomePanelAction(MainFrame frame) {
        super(null, new ImageIcon(HomePanel.class.getClassLoader().getResource("icons/home_24.png")));
        this.frame=frame;
        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.changeContentPane(new HomePanel(frame));
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString("homePage"));
        });
    }
}

