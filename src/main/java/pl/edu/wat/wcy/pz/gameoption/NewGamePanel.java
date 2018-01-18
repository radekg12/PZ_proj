package pl.edu.wat.wcy.pz.gameoption;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.HomePanelAction;
import pl.edu.wat.wcy.pz.checkers.Player;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.game.GamePanel;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NewGamePanel extends JPanel implements ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(NewGamePanel.class.getSimpleName(), "LogsMessages");
    private MainFrame frame;
    private JLabel label1, label2, labelSlider;
    private JTextField textField1, textField2;
    private AvatarComboBox customComboBox1, customComboBox2;
    private JButton startButton, backButton;
    private AbstractAction startAction, backAction, selectSexF1Action, selectSexF2Action, selectSexM1Action, selectSexM2Action;
    private JRadioButton radioButtonF1, radioButtonF2, radioButtonM1, radioButtonM2;
    private ButtonGroup group1, group2;
    private JSlider slider;
    private Box box1, box2;
    private TitledBorder border;
    private int minTime, maxTime, defaultTime, sliderMajor, sliderMinor;

    public NewGamePanel(MainFrame frame) {
        super(new GridBagLayout());
        loadProperties();
        this.frame = frame;
        label1 = new JLabel();
        label2 = new JLabel();
        labelSlider = new JLabel();
        textField1 = new JTextField();
        //textField1.setBorder(BorderFactory.createEmptyBorder());
        textField2 = new JTextField();
        slider = new JSlider(JSlider.HORIZONTAL, minTime, maxTime, defaultTime);
        slider.setMinorTickSpacing(sliderMinor);
        slider.setMajorTickSpacing(sliderMajor);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);

        startAction = new AbstractAction(null, new ImageIcon(getClass().getClassLoader().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player1 = new Player(textField1.getText(), customComboBox1.getSelectedImage(), customComboBox1.getSelectedImageName(), -1);
                Player player2 = new Player(textField2.getText(), customComboBox2.getSelectedImage(), customComboBox2.getSelectedImageName(), 1);
                int time = slider.getValue();
                frame.changeContentPane(new GamePanel(frame, player1, player2, time));
            }
        };
        backAction = new HomePanelAction(frame);

        startButton = new JButton(startAction);
        backButton = new JButton(backAction);

        customComboBox1 = new AvatarComboBox();
        customComboBox2 = new AvatarComboBox();

        selectSexF1Action = new SelectSexAction(customComboBox1, "f");
        selectSexM1Action = new SelectSexAction(customComboBox1, "m");
        selectSexF2Action = new SelectSexAction(customComboBox2, "f");
        selectSexM2Action = new SelectSexAction(customComboBox2, "m");

        radioButtonF1 = new JRadioButton(selectSexF1Action);
        //radioButtonF1.setOpaque(false);
        radioButtonM1 = new JRadioButton(selectSexM1Action);
        //radioButtonM1.setOpaque(true);
        radioButtonF2 = new JRadioButton(selectSexF2Action);
        radioButtonM2 = new JRadioButton(selectSexM2Action);

        group1 = new ButtonGroup();
        group1.add(radioButtonM1);
        group1.add(radioButtonF1);

        border = BorderFactory.createTitledBorder("");
        box1 = Box.createVerticalBox();
        box1.setBorder(border);
        box1.add(radioButtonM1);
        box1.add(radioButtonF1);

        group2 = new ButtonGroup();
        group2.add(radioButtonM2);
        group2.add(radioButtonF2);

        box2 = Box.createVerticalBox();
        box2.setBorder(border);
        box2.add(radioButtonM2);
        box2.add(radioButtonF2);

        ChangeLanguageAction.addChangeLanguageListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();
            //setBackground(new Color(46, 46, 46));

            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;


            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 10, 10, 10);
            c.fill = GridBagConstraints.BOTH;
            add(label1, c);

            c.gridx = 1;
            c.gridy = 0;
            add(label2, c);

            c.gridx = 0;
            c.gridy = 1;
            add(textField1, c);

            c.gridx = 1;
            c.gridy = 1;
            add(textField2, c);

            c.gridx = 0;
            c.gridy = 2;
            add(box1, c);

            c.gridx = 1;
            c.gridy = 2;
            add(box2, c);

            c.gridx = 0;
            c.gridy = 3;
            add(customComboBox1, c);

            c.gridx = 1;
            c.gridy = 3;
            add(customComboBox2, c);

            c.fill = GridBagConstraints.NONE;
            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 4;
            add(labelSlider, c);

            c.gridwidth = 2;
            c.gridx = 0;
            c.gridy = 5;
            add(slider, c);

            c.fill = GridBagConstraints.BOTH;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 6;
            add(backButton, c);

            c.gridx = 1;
            c.gridy = 6;
            add(startButton, c);

        });
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
            //backAction.putValue(Action.NAME, rb.getString("back"));
            selectSexF1Action.putValue(Action.NAME, rb.getString("female"));
            selectSexF2Action.putValue(Action.NAME, rb.getString("female"));
            selectSexM1Action.putValue(Action.NAME, rb.getString("male"));
            selectSexM2Action.putValue(Action.NAME, rb.getString("male"));
            textField1.setText(rb.getString("firstName") + " 1");
            textField2.setText(rb.getString("firstName") + " 2");
            border.setTitle(rb.getString("sex"));
            label1.setText(rb.getString("player1"));
            label2.setText(rb.getString("player2"));
            labelSlider.setText(rb.getString("sliderText"));
        });
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);
            minTime = Integer.parseInt(properties.getProperty("game.minTime"));
            maxTime = Integer.parseInt(properties.getProperty("game.maxTime"));
            sliderMinor = Integer.parseInt(properties.getProperty("game.sliderMinor"));
            sliderMajor = Integer.parseInt(properties.getProperty("game.sliderMajor"));
            defaultTime = Integer.parseInt(properties.getProperty("game.defaultTime"));
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

    public class SelectSexAction extends AbstractAction {
        AvatarComboBox comboBox;
        String sex;

        public SelectSexAction(AvatarComboBox comboBox, String sex) {
            this.comboBox = comboBox;
            this.sex = sex;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            comboBox.filter(sex);
        }
    }
}
