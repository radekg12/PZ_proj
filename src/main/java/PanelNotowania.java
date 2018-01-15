import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelNotowania extends JComponent implements ZmienJezykListener {
    private Logger LOGGER = Logger.getLogger(PanelNotowania.class.getSimpleName(), "LogsMessages");
    private JLabel tytul, date, not1;
    private String base, res;

    public PanelNotowania(OknoGlowne frame) {
        setLayout(new GridLayout(3, 1));
        tytul = new JLabel();
        date = new JLabel();
        not1 = new JLabel();
        loadProperties();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {

                WebClient client = new WebClient();
                WebApiResponse xxx;
                try {
                    xxx = client.getResponse(base);
                    Method method = xxx.getRates().getClass().getMethod("get" + res);
                    not1.setText("1 " + base + " = " + method.invoke(xxx.getRates()) + " " + res);
                    date.setText(xxx.getDate());
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "rest.open", e);
                }
                LOGGER.info("rest.openInfo");
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

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getResource(propertiesName).openStream();
            properties.load(input);
            base = properties.getProperty("currency.in");
            res = properties.getProperty("currency.out");
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "properties.open", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "properties.close", e);
                }
            }
        }
    }
}
