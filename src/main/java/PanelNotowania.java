import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class PanelNotowania extends JComponent implements ZmienJezykListener {
    private JLabel tytul, date, not1, not2, not3;
    private Font font = new Font(Font.DIALOG, Font.BOLD, 16);
    private Color fontColor = new Color(195, 195, 195);
    private String base = "PLN", dateString;

    public PanelNotowania(OknoGlowne frame) {
        setLayout(new GridLayout(3,1));
        tytul = new JLabel();
        date = new JLabel();
        not1 = new JLabel();
        not2 = new JLabel();
        not3 = new JLabel();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                WebClient client = new WebClient();
                WebApiResponse xxx = null;
                try {
                    xxx = client.getResponse(base);
                    System.out.println("1" + xxx.getBase() + " = " + xxx.getRates().getEUR() + " EUR");
                    not1.setText("1 " + base + " = " + xxx.getRates().getEUR() + " EUR");
                    date.setText(xxx.getDate());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }.execute();

        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    private void initGUI() {
        SwingUtilities.invokeLater(() -> {
        add(tytul);
        add(date);
        add(not1);

        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        ResourceBundle rb = event.getRb();
        tytul.setText(rb.getString("exchangeRates"));
    }
}
