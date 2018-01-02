import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PanelGracza extends JPanel implements ZmienKolejListener, WygranaListener {
    private JButton button = new JButton("usun");
    private OknoGlowne frame;
    private Gracz gracz;
    private boolean aktywny;
    private final Color color = new Color(64, 64, 64);
    private final Color activeColor = new Color(80, 80, 80);
    private JLabel nazwaLabel;
    private JLabel avatarLabel;
    private boolean won;
    private BufferedImage confetti;
    private Image confetti2;


    public PanelGracza(OknoGlowne frame, Gracz gracz, Gra gra) {
        this.frame = frame;
        setMinimumSize(new Dimension(100, 100));
        setMaximumSize(new Dimension(250, 250));
        setPreferredSize(new Dimension(150, 150));
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new StronaStartowa(frame));
            }
        });
        this.gracz = gracz;
        won = false;
        aktywny = gracz.getK() == 1;
        setBackground(aktywny ? activeColor : color);
        nazwaLabel = new JLabel(gracz.getNazwa());
        //nazwaLabel.setFont(font);
        //nazwaLabel.setForeground(fontColor);
        avatarLabel = new JLabel(new ImageIcon(gracz.getAvatar()));
        gra.addZmienKolejListener(this);
        gra.addWygranaListener(this);
        try {
            confetti = ImageIO.read(new File(String.valueOf(getClass().getResource("icons/won_70.png")).replace("file:/", "")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        confetti2 = new ImageIcon(getClass().getResource("icons/confetti.gif")).getImage();
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            //add(button);
            setLayout(new BorderLayout());
            add(nazwaLabel, BorderLayout.NORTH);
            add(avatarLabel, BorderLayout.CENTER);
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (won)
            g.drawImage(confetti, 0, 0, null);
    }

    @Override
    public void zmienKolej(ZmienKolejEvent event) {
        aktywny = !aktywny;
        setBackground(aktywny ? activeColor : color);
        revalidate();
    }


    @Override
    public void wygrana(WygranaEvent event) {
        if (event.getWygrany() == gracz) {
            won = true;
            repaint();
        }
    }
}
