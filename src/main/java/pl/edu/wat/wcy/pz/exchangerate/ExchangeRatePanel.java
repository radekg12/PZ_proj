package pl.edu.wat.wcy.pz.exchangerate;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExchangeRatePanel extends JComponent implements ChangeLanguageListener {
    private Logger LOGGER = Logger.getLogger(ExchangeRatePanel.class.getSimpleName(), "LogsMessages");
    private JLabel titleLabel, dateLabel, rateLabel, imageLabel;
    private String base, res;
    private ImageIcon exchangeRateImage;

    public ExchangeRatePanel(MainFrame frame) {
        setLayout(new GridBagLayout());
        titleLabel = new JLabel();
        dateLabel = new JLabel(" ");
        rateLabel = new JLabel(" ");
        imageLabel = new JLabel(" ");
        loadProperties();

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
                WebClient client = new WebClient();
                WebApiResponse xxx;
                try {
                    xxx = client.getResponse();
                    Method method = xxx.getRates().getClass().getMethod("get" + res);
                    rateLabel.setText("1 " + base + " = " + method.invoke(xxx.getRates()) + " " + res);
                    dateLabel.setText(xxx.getDate());
                } catch (UnknownHostException e) {
                    LOGGER.log(Level.WARNING, "rest.open", e);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "io.open", e);
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

        ChangeLanguageAction.addChangeLanguageListener(this);
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
            add(titleLabel, c);

            c.gridy = 2;
            add(dateLabel, c);

            c.gridy = 3;
            add(rateLabel, c);
        });
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        ResourceBundle rb = event.getRb();
        titleLabel.setText(rb.getString("exchangeRates"));
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
