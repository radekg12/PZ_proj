import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class PanelNowaGra extends JPanel implements ZmienJezykListener {
    //private static final Logger LOGGER = Logger.getLogger(PanelNowaGra.class.getSimpleName(), "LogsMessages");
    private OknoGlowne frame;
    private JLabel label1, label2;
    private JTextField textField1, textField2;
    private AvatarComboBox customComboBox1, customComboBox2;
    //TODO
    //wybor kolorow
    private JButton startButton, backButton;
    private AbstractAction startAction, backAction, selectSexF1Action, selectSexF2Action, selectSexM1Action, selectSexM2Action;
    private JRadioButton radioButtonF1, radioButtonF2, radioButtonM1, radioButtonM2;
    private ButtonGroup group1, group2;
    private Box box1, box2;
    private TitledBorder border;

    public PanelNowaGra(OknoGlowne frame) {
        super(new GridBagLayout());
        this.frame = frame;
        label1 = new JLabel();
        label2 = new JLabel();
        textField1 = new JTextField();
        //textField1.setBorder(BorderFactory.createEmptyBorder());
        textField2 = new JTextField();

        startAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gracz gracz1 = new Gracz(textField1.getText(), customComboBox1.getSelectedImage(), customComboBox1.getSelectedImageName(), -1);
                Gracz gracz2 = new Gracz(textField2.getText(), customComboBox2.getSelectedImage(), customComboBox2.getSelectedImageName(), 1);
                frame.zmianaOkna(new OknoGry(frame, gracz1, gracz2));

            }
        };
        backAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/back_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new StronaStartowa(frame));
            }
        };


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

        frame.getMenu().addZmienJezykListener(this);
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

            c.gridx = 0;
            c.gridy = 4;
            add(backButton, c);

            c.gridx = 1;
            c.gridy = 4;
            add(startButton, c);
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
            backAction.putValue(Action.NAME, rb.getString("back"));
            selectSexF1Action.putValue(Action.NAME, rb.getString("female"));
            selectSexF2Action.putValue(Action.NAME, rb.getString("female"));
            selectSexM1Action.putValue(Action.NAME, rb.getString("male"));
            selectSexM2Action.putValue(Action.NAME, rb.getString("male"));
            textField1.setText(rb.getString("firstName") + " 1");
            textField2.setText(rb.getString("firstName") + " 2");
            border.setTitle(rb.getString("sex"));
            label1.setText(rb.getString("player1"));
            label2.setText(rb.getString("player2"));
        });
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
