package pl.edu.wat.wcy.pz.frame;

import pl.edu.wat.wcy.pz.checkers.Pionek;
import pl.edu.wat.wcy.pz.events.ZmienJezykEvent;
import pl.edu.wat.wcy.pz.exchangerate.PanelNotowania;
import pl.edu.wat.wcy.pz.gameoption.PanelNowaGra;
import pl.edu.wat.wcy.pz.listeners.ZmienJezykListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StronaStartowa extends JPanel implements ZmienJezykListener {
    private static final Logger LOGGER = Logger.getLogger(StronaStartowa.class.getSimpleName(), "LogsMessages");
    private JLabel title, imageLabel;
    private JButton button1, button2;
    private AbstractAction startAction;
    private OknoGlowne frame;
    private PanelNotowania notowania;
    private ImageIcon image;
    private Color titleForeground;
    private int titleSize;
    private Font myFont;

    public StronaStartowa(OknoGlowne frame) {
        super(new GridBagLayout());
        this.frame = frame;
        loadProperties();
        notowania = new PanelNotowania(frame);
        startAction = new AbstractAction(null, new ImageIcon(getClass().getClassLoader().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new PanelNowaGra(frame));
            }
        };
        button1 = new JButton(startAction);
        button2 = new JButton(frame.getMenu().exitAction);
        title = new JLabel();
        title.setForeground(titleForeground);
        myFont = new Font(Font.SANS_SERIF, Font.PLAIN, titleSize);
        title.setFont(myFont);
        image = new ImageIcon(getClass().getClassLoader().getResource("icons/myLogo_150.png"));
        imageLabel = new JLabel(image);
        initGUI();
        frame.getMenu().addZmienJezykListener(this);
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
            add(title, c);

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
            add(button2, c);

            c.gridx = 2;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            add(button1, c);

            c.gridx = 1;
            c.gridy = 3;
            c.gridwidth = 1;
            c.gridheight = 1;
            add(notowania, c);
        });
    }


    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
            title.setText(rb.getString("gameName").toUpperCase());
        });
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(OknoGlowne frame) {
        this.frame = frame;
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = Pionek.class.getClassLoader().getResource(propertiesName).openStream();
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
