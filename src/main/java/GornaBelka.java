import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class GornaBelka extends JPanel implements ZmienJezykListener {
    private JLabel label;
    private String gameName;

    public GornaBelka(OknoGlowne frame) {
        label = new JLabel(gameName);
        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            setBackground(new Color(46, 46, 46));
//            setBackground(new Color(28, 122, 242));
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

