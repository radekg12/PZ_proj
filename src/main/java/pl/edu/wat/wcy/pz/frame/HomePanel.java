package pl.edu.wat.wcy.pz.frame;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.ExitAction;
import pl.edu.wat.wcy.pz.checkers.Piece;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.exchangerate.ExchangeRatePanel;
import pl.edu.wat.wcy.pz.gameoption.NewGamePanel;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HomePanel extends JPanel implements ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(HomePanel.class.getSimpleName(), "LogsMessages");
    private JLabel titleLabel, imageLabel;
    private JButton startButton, exitButton;
    private AbstractAction startAction, exitAction;
    private MainFrame frame;
    private ExchangeRatePanel exchangeRatePanel;
    private ImageIcon logoImage;
    private Color titleForeground;
    private int titleSize;
    private Font myFont;

    public HomePanel(MainFrame frame) {
        super(new GridBagLayout());
        this.frame = frame;
        loadProperties();
        exchangeRatePanel = new ExchangeRatePanel(frame);
        startAction = new AbstractAction(null, new ImageIcon(getClass().getClassLoader().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.changeContentPane(new NewGamePanel(frame));
            }
        };
        startButton = new JButton(startAction);
        exitAction=new ExitAction();
        exitButton = new JButton(exitAction);
        titleLabel = new JLabel();
        titleLabel.setForeground(titleForeground);
        myFont = new Font(Font.SANS_SERIF, Font.PLAIN, titleSize);
        titleLabel.setFont(myFont);
        logoImage = new ImageIcon(getClass().getClassLoader().getResource("icons/myLogo_150.png"));
        imageLabel = new JLabel(logoImage);
        initGUI();
        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 3;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.insets = new Insets(20, 30, 20, 30);
            //c.fill = GridBagConstraints.BOTH;
            add(titleLabel, c);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 3;
            c.gridheight = 1;
            add(imageLabel, c);

            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            add(exitButton, c);

            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            add(startButton, c);

            c.gridx = 1;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
            add(exchangeRatePanel, c);
        });
    }


    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
            titleLabel.setText(rb.getString("gameName").toUpperCase());
            //exitAction.putValue(Action.NAME, rb.getString("exit"));
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(MainFrame frame) {
        this.frame = frame;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = Piece.class.getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);
            titleForeground = Color.decode(properties.getProperty("title.foreground"));
            titleSize = Integer.parseInt(properties.getProperty("title.size"));
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
