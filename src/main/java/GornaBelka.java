import javax.swing.*;
import java.util.ResourceBundle;

public class GornaBelka extends JPanel implements ZmienJezykListener {
    private JLabel label;

    public GornaBelka(OknoGlowne frame) {
        label = new JLabel();
        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
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

