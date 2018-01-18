package pl.edu.wat.wcy.pz.frame;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.ExitAction;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.game.GamePanel;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;
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

public class MainFrame extends JFrame implements ChangeLanguageListener {
    private Logger logger;
    private int defaultWidth, defaultHeight;
    private Menu menu;
    private String gameName;
    private Image logoImage;
    private MainFrame frame;
    private Color backgroundColor, backgroundColorLight, foregroundColor;
    private Font myFont1;


    public MainFrame() {
        new LoggerConfiguration();
        logger = Logger.getLogger(MainFrame.class.getSimpleName(), "LogsMessages");
        loadProperties();
        logoImage = new ImageIcon(getClass().getClassLoader().getResource("icons/myLogo3.png")).getImage();
        setSize(defaultWidth, defaultHeight);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(logoImage);
        menu = new Menu(this);
        setJMenuBar(menu);
        addWindowListener(new MyDialog());
        ChangeLanguageAction.addChangeLanguageListener(this);
        loadUILook();
        frame = this;
        SwingUtilities.invokeLater(() -> {
            getContentPane().add(new HomePanel(frame));
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainFrame::new);
    }


    public void changeContentPane(Component component) {
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
    public void changeLocal(ChangeLanguageEvent event) {
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

            backgroundColor = Color.decode(properties.getProperty("background.darkColor"));
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


    private class MyDialog extends WindowAdapter implements ChangeLanguageListener {
        private JButton cancelButton;
        private JButton closeButton = new JButton();
        private JButton[] buttons;
        private String message, title;
        private Icon alertIcon;
        private JOptionPane optionPane;
        private JDialog dialog;
        private AbstractAction cancelAction;

        MyDialog() {
            closeButton.addActionListener(new ExitAction());
            cancelAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            };
            cancelButton = new JButton(cancelAction);
            buttons = new JButton[]{cancelButton, closeButton};
            alertIcon = new ImageIcon(getClass().getClassLoader().getResource("icons/warning_2.png"));
            ChangeLanguageAction.addChangeLanguageListener(this);

        }

        @Override
        public void windowClosing(WindowEvent e) {
            Component[] cos = getContentPane().getComponents();
            boolean x = Arrays.stream(cos).anyMatch(c -> c instanceof GamePanel);
            if (x)
                closing();
            else System.exit(0);
        }

        public void closing() {
            SwingUtilities.invokeLater(() -> {
                dialog.setVisible(true);
            });
        }

        @Override
        public void changeLocal(ChangeLanguageEvent event) {
            ResourceBundle rb = event.getRb();
            message = (rb.getString("alert.statement"));
            title = (rb.getString("alert.title"));
            cancelButton.setText(rb.getString("cancel"));
            closeButton.setText(rb.getString("exit"));
            optionPane = new JOptionPane(
                    message, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, alertIcon, buttons, buttons[1]
            );
            dialog = optionPane.createDialog(title);
        }
    }
}
