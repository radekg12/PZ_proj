import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ResourceBundle;

public class StronaStartowa extends JPanel implements ZmienJezykListener {
    private JButton button;
    private AbstractAction startAction;
    private OknoGry frame;

    public StronaStartowa(OknoGry frame) {
        this.frame = frame;
        setBackground(Color.blue);
        startAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new Gra(frame, 300, 300, 8));
            }
        };
        button = new JButton(startAction);
        add(button, BorderLayout.NORTH);
        add(new JButton(frame.getMenu().exitAction));
        frame.getMenu().addZmienJezykListener(this::changeLocal);
    }


    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ResourceBundle rb = event.getRb();
                startAction.putValue(Action.NAME, rb.getString("start"));
            }
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(OknoGry frame) {
        this.frame = frame;
    }
}
