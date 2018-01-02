
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ResourceBundle;

public class OknoGlowne extends JFrame implements ZmienJezykListener {
    private BufferedImage icon;
    public static final int DEFAULT_WIDTH = 720;
    public static final int DEFAULT_HEIGHT = 480;
    private Menu menu;
    private String gameName;
    Image image;
    OknoGlowne frame;


    public OknoGlowne() {

//        String coss = getClass().getClassLoader().getResource("icons/aaa.png").getPath();
//
//        URL url = getClass().getResource("icons/myLogo.png");
//        URL url2 = OknoGlowne.class.getResource("icons/myLogo.png");
//        try {
//            File file = new File(url.getFile());
//            icon = ImageIO.read(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        image = new ImageIcon(getClass().getResource("icons/myLogo.png")).getImage();
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(image);
        getContentPane().setBackground(new Color(48, 48, 48));
        menu = new Menu(this);
        setJMenuBar(menu);
        addWindowListener(new MyDialog());
        menu.addZmienJezykListener(this);
        UIManager.getDefaults().put("Label.font", new Font(Font.DIALOG, Font.BOLD, 14));
        UIManager.getDefaults().put("Label.foreground", new Color(195, 195, 195));
        //setLayout(new GridLayout(1, 3, 20, 20));
        //getContentPane().add(new Test(this, 400, 400, 8));
        //panel.setSize(300,300);
        //panel.setBackground(Color.green.darker());
        //etContentPane().add(panel);
//        addComponentListener(new ComponentAdapter() {
//            @Override
//            public void componentResized(ComponentEvent e) {
//                setSize(getSize().width, 2*getSize().width/3);
//            }
//        });
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


    private class MyDialog extends WindowAdapter implements ZmienJezykListener {
        JButton anulujButton = new JButton();
        JButton zapiszButton = new JButton();
        JButton zamknijButton = new JButton();
        JButton[] buttons = {anulujButton, zapiszButton, zamknijButton};
        String komunikat;
        String tytul;
        Icon alertIcon;
        JOptionPane optionPane;
        JDialog dialog;

        MyDialog() {
            zamknijButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(1);
                }
            });
            MyDialog okno = this;
            anulujButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.setVisible(false);
                }
            });
            alertIcon = new ImageIcon(getClass().getResource("icons/warning_2.png"));
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

            optionPane = new JOptionPane(
                    komunikat, JOptionPane.QUESTION_MESSAGE, JOptionPane.DEFAULT_OPTION, alertIcon, buttons, buttons[1]
            );

            dialog = optionPane.createDialog(frame, tytul);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setVisible(true);

        }

        @Override
        public void changeLocal(ZmienJezykEvent event) {
            ResourceBundle rb = event.getRb();
            komunikat = (rb.getString("alert.statement"));
            tytul = (rb.getString("alert.title"));
            anulujButton.setText(rb.getString("cancel"));
            zamknijButton.setText(rb.getString("exit"));
            zapiszButton.setText(rb.getString("save"));
        }
    }
}
