import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class StronaStartowa extends JPanel implements ZmienJezykListener {
    private JButton button1, button2;
    private AbstractAction startAction;
    private OknoGlowne frame;
    private PanelNotowania notowania;

    public StronaStartowa(OknoGlowne frame) {
        this.frame = frame;
        setBackground(Color.blue);
        notowania = new PanelNotowania(frame);
        startAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
//                frame.zmianaOkna(new OknoGry(frame, 300, 300, 8));
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
