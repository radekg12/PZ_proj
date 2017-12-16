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
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    private Menu menu;
    private String gameName;
    Image image;

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
        addWindowListener(new Okno());
        // setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setIconImage(image);
        getContentPane().setBackground(new Color(48, 48, 48));
        menu = new Menu();
        setJMenuBar(menu);
        menu.addZmienJezykListener(this);
        //setLayout(new GridLayout(1, 3, 20, 20));
        //getContentPane().add(new Test(this, 400, 400, 8));
        //panel.setSize(300,300);
        //panel.setBackground(Color.green.darker());
        //etContentPane().add(panel);
        OknoGlowne frame=this;
        SwingUtilities.invokeLater(() -> getContentPane().add(new StronaStartowa(frame)));

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


    private class Okno extends WindowAdapter {
        JButton anulujButton = new JButton("Anuluj");
        JButton zapiszButton = new JButton("Zapisz");
        JButton zamknijButton = new JButton("Zamknij");
        JButton[] buttons = {anulujButton, zapiszButton, zamknijButton};
        String komunikat = "Co mam zrobić? ";
        String tytul = "Program zmodyfikowany";

        Okno() {
            zamknijButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
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

            JOptionPane.showOptionDialog(
                    null,                      // okno
                    komunikat,          // komunikat
                    tytul,   // tytuł
                    JOptionPane.DEFAULT_OPTION, // rodzaj przycisków u dołu (tu nieważny)
                    JOptionPane.QUESTION_MESSAGE,// typ komunikatu (standardowa ikona)
                    null,                        // własna ikona (tu: brak)
                    buttons,                       // własne opcje - przyciski
                    buttons[1]);                   // domyślny przycisk
        }
    }
}
