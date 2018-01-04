import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

public class PanelNowaGra extends JPanel implements ZmienJezykListener {
    private OknoGlowne frame;
    private JLabel label1, label2;
    private JTextField textField1, textField2;
    private AvatarComboBox customComboBox1, customComboBox2;
    //wybor kolorow
    private JButton startButton, backButton;
    private AbstractAction startAction;
    private AbstractAction backAction;

    public PanelNowaGra(OknoGlowne frame) {
        super(new GridBagLayout());
        this.frame = frame;
        label1 = new JLabel("Label 1");
        label2 = new JLabel("Label 2");
        textField1 = new JTextField("Wprowadz imie 1");
        textField2 = new JTextField("Wprowadz imie 2");
        //
        //

        startAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/start_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gracz gracz1 = new Gracz(textField1.getText(), customComboBox1.getSelectedImage(), -1);
                Gracz gracz2 = new Gracz(textField2.getText(), customComboBox2.getSelectedImage(), 1);
                frame.zmianaOkna(new OknoGry(frame, gracz1, gracz2, 300, 300, 8));
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

        frame.getMenu().addZmienJezykListener(this);
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();
            setBackground(new Color(46, 46, 46));

            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;


            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 10, 10, 10);
            //c.fill = GridBagConstraints.BOTH;
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
            add(customComboBox1, c);

            c.gridx = 1;
            c.gridy = 2;
            add(customComboBox2, c);

            c.gridx = 0;
            c.gridy = 3;
            add(backButton, c);

            c.gridx = 1;
            c.gridy = 3;
            add(startButton, c);
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            startAction.putValue(Action.NAME, rb.getString("start"));
            backAction.putValue(Action.NAME, rb.getString("back"));
        });
    }
}
