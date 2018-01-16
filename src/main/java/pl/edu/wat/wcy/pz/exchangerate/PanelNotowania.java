package pl.edu.wat.wcy.pz.exchangerate;

import pl.edu.wat.wcy.pz.events.ZmienJezykEvent;
import pl.edu.wat.wcy.pz.frame.OknoGlowne;
import pl.edu.wat.wcy.pz.listeners.ZmienJezykListener;

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
    private JLabel tytul, date, not1, imageLabel;
    private String base, res;
    private ImageIcon exchangeRateImage;

    public PanelNotowania(OknoGlowne frame) {
        setLayout(new GridBagLayout());
        tytul = new JLabel();
        date = new JLabel(" ");
        not1 = new JLabel(" ");
        imageLabel = new JLabel(" ");
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

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                exchangeRateImage = new ImageIcon(getClass().getClassLoader().getResource("icons/exchange.png"));
                return null;
            }

            @Override
            protected void done() {
                imageLabel.setIcon(exchangeRateImage);
            }
        }.execute();

        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    private void initGUI() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1;
            c.weighty = 1;
            add(imageLabel, c);

            c.gridy = 1;
            add(tytul, c);

            c.gridy = 2;
            add(date, c);

            c.gridy = 3;
            add(not1, c);
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
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
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
