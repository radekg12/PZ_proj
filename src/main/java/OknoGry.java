import javax.swing.*;
import java.awt.*;

public class OknoGry extends JComponent {
    Gra gra;
    PanelGracza panel1;
    PanelGracza panel2;

    OknoGry(OknoGlowne frame, int width, int height, int liczbaPol) {
        //setLayout(new GridLayout(1, 3, 20, 20));
        setLayout(new BorderLayout());
        gra = new Gra(frame, width, height, liczbaPol);
        panel1 = new PanelGracza(frame);
        panel2 = new PanelGracza(frame);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(panel1, BorderLayout.WEST);
            add(gra, BorderLayout.CENTER);
            add(panel2, BorderLayout.EAST);
        });
    }
}
