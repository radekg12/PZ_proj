package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeLookAction extends AbstractAction implements ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(ChangeLookAction.class.getSimpleName(), "LogsMessages");
    private String plaf;
    private MainFrame frame;
    private String look;

    public ChangeLookAction(String look, MainFrame frame) {
        super(look, new ImageIcon(ChangeLookAction.class.getClassLoader().getResource("icons/" + look.replace(" ", "").toLowerCase() + "_24.png")));
        this.frame = frame;
        this.look = look;

        switch (look) {
            case "Look 1":
                plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                break;
            case "Look 2":
                plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
                break;
            case "Look 3":
                plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
                break;
            default:
                plaf = UIManager.getSystemLookAndFeelClassName();
        }

        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            UIManager.setLookAndFeel(plaf);
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
            LOGGER.log(Level.WARNING, "plaf.change", e);
        }
        frame.loadUILook();
        SwingUtilities.updateComponentTreeUI(frame);
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString(look.replace(" ", "").toLowerCase()));
        });
    }
}
