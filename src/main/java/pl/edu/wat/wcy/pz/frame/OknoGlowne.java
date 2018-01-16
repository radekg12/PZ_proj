package pl.edu.wat.wcy.pz.frame;

import pl.edu.wat.wcy.pz.events.ZmienJezykEvent;
import pl.edu.wat.wcy.pz.game.OknoGry;
import pl.edu.wat.wcy.pz.listeners.ZmienJezykListener;
import pl.edu.wat.wcy.pz.logger.LoggerConfiguration;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OknoGlowne extends JFrame implements ZmienJezykListener {
    private Logger logger;
    private int defaultWidth, defaultHeight;
    private Menu menu;
    private String gameName;
    private Image image;
    private OknoGlowne frame;
    private Color backgroundColor, backgroundColorLight, foregroundColor;
    private Font myFont1;


    public OknoGlowne() {
        new LoggerConfiguration();
        logger = Logger.getLogger(OknoGlowne.class.getSimpleName(), "LogsMessages");
        loadProperties();
        image = new ImageIcon(getClass().getClassLoader().getResource("icons/myLogo3.png")).getImage();
        setSize(defaultWidth, defaultHeight);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(image);
        menu = new Menu(this);
        setJMenuBar(menu);
        addWindowListener(new MyDialog());
        menu.addZmienJezykListener(this);
        loadUILook();
        frame = this;
        SwingUtilities.invokeLater(() -> {
            getContentPane().add(new StronaStartowa(frame));
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OknoGlowne::new);
    }


    public void zmianaOkna(Component component) {
        SwingUtilities.invokeLater(() -> {
            getContentPane().removeAll();
            getContentPane().invalidate();

            getContentPane().add(component);
            getContentPane().revalidate();
        });
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            gameName = rb.getString("gameName");
            setTitle(gameName);
        });
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";

        try {
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);

            backgroundColor = Color.decode(properties.getProperty("background.dark"));
            backgroundColorLight = Color.decode(properties.getProperty("background.bright"));
            foregroundColor = Color.decode(properties.getProperty("foreground"));
            myFont1 = new Font(Font.DIALOG, Font.BOLD, Integer.parseInt(properties.getProperty("myFont.size")));
            defaultWidth = Integer.parseInt(properties.getProperty("default_width"));
            defaultHeight = Integer.parseInt(properties.getProperty("default_height"));
        } catch (IOException ex) {
            logger.log(Level.WARNING, "properties.open", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    logger.log(Level.WARNING, "properties.close", e);
                }
            }
        }
    }

    public void loadUILook() {
        SwingUtilities.invokeLater(() -> {
            UIManager.put("Label.font", myFont1);
            UIManager.put("Label.foreground", foregroundColor);
            UIManager.put("TextField.background", backgroundColor);
            UIManager.put("TextField.font", myFont1);
            UIManager.put("TextField.foreground", foregroundColor);
            UIManager.put("ComboBox.background", backgroundColor);
            UIManager.put("ComboBox.selectionBackground", backgroundColorLight);
            UIManager.put("RadioButton.background", backgroundColor);
            UIManager.put("RadioButton.foreground", foregroundColor);
            UIManager.put("RadioButton.font", myFont1);
            UIManager.put("TitledBorder.font", myFont1);
            UIManager.put("TitledBorder.titleColor", foregroundColor);
            UIManager.put("Panel.background", backgroundColor);
            UIManager.put("Slider.background", backgroundColor);
        });
    }


    private class MyDialog extends WindowAdapter implements ZmienJezykListener {
        //TODO ?????
        private JButton anulujButton;
        //private JButton zapiszButton = new JButton();
        private JButton zamknijButton = new JButton();
        private JButton[] buttons;
        private String komunikat, tytul;
        private Icon alertIcon;
        private JOptionPane optionPane;
        private JDialog dialog;
        private AbstractAction anulujAction;

        MyDialog() {
            zamknijButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            anulujAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            };
            anulujButton = new JButton(anulujAction);
            //buttons = new JButton[]{anulujButton, zapiszButton, zamknijButton};
            buttons = new JButton[]{anulujButton, zamknijButton};
            alertIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/warning_2.png"));
            menu.addZmienJezykListener(this);

        }

        @Override
        public void windowClosing(WindowEvent e) {
            Component[] cos = getContentPane().getComponents();
            boolean x = Arrays.stream(cos).anyMatch(c -> c instanceof OknoGry);
            if (x)
                zamykanie();
            else System.exit(0);
        }

        public void zamykanie() {
            SwingUtilities.invokeLater(() -> {
                dialog.setVisible(true);
            });
        }

        @Override
        public void changeLocal(ZmienJezykEvent event) {
            ResourceBundle rb = event.getRb();
            komunikat = (rb.getString("alert.statement"));
            tytul = (rb.getString("alert.title"));
            anulujButton.setText(rb.getString("cancel"));
            zamknijButton.setText(rb.getString("exit"));
            //zapiszButton.setText(rb.getString("save"));
            optionPane = new JOptionPane(
                    komunikat, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, alertIcon, buttons, buttons[1]
            );
            dialog = optionPane.createDialog(tytul);
        }
    }
}
