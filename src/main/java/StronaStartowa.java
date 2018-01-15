import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class StronaStartowa extends JPanel implements ZmienJezykListener {
    //private static final Logger LOGGER = Logger.getLogger(StronaStartowa.class.getSimpleName(), "LogsMessages");
    private JButton button1, button2;
    private AbstractAction startAction;
    private OknoGlowne frame;
    private PanelNotowania notowania;

    public StronaStartowa(OknoGlowne frame) {
        this.frame = frame;
        notowania = new PanelNotowania(frame);
        startAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new PanelNowaGra(frame));
            }
        };
        button1 = new JButton(startAction);
        button2 = new JButton(frame.getMenu().exitAction);
        initGUI();
        frame.getMenu().addZmienJezykListener(this);
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(button1);
            add(button2);
            add(notowania, BorderLayout.SOUTH);
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(OknoGlowne frame) {
        this.frame = frame;
    }
}
