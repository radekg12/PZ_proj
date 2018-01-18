package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class ExitAction extends AbstractAction implements ChangeLanguageListener {
    public ExitAction() {
        super(null, new ImageIcon(ExitAction.class.getClassLoader().getResource("icons/exit_24.png")));
        ChangeLanguageAction.addChangeLanguageListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString("exit"));
        });
    }
}


