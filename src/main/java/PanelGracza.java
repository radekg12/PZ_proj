import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelGracza extends JPanel {
    JButton button = new JButton("usun");
    OknoGlowne frame;

    public PanelGracza(OknoGlowne frame) {
        this.frame = frame;
        //setBackground(new Color(64, 64, 64));
        setBackground(Color.green);
        setPreferredSize(new Dimension(200, 200));
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new StronaStartowa(frame));
            }
        });
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> add(button));
    }
}
