import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelGracza extends JPanel implements ZmienKolejListener, WygranaListener {
    private static final Logger LOGGER = Logger.getLogger(PanelGracza.class.getSimpleName(), "LogsMessages");
    private JButton button = new JButton("usun");
    private Gracz gracz;
    private boolean aktywny;
    private Color color, activeColor;
    private JLabel nazwaLabel;
    private JLabel avatarLabel;
    private boolean won;
    private BufferedImage confetti;


    public PanelGracza(OknoGlowne frame, Gracz gracz, Gra gra) {
        loadProperties();
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
        nazwaLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel = new JLabel(new ImageIcon(gracz.getAvatar()));
        gra.addZmienKolejListener(this);
        gra.addWygranaListener(this);
        try {
            confetti = ImageIO.read(new File(String.valueOf(getClass().getResource("icons/won_70.png")).replace("file:/", "")));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "image.open", e);
        }
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
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
            setBackground(color);
            repaint();
        }
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getResource(propertiesName).openStream();
            properties.load(input);
            color = Color.decode(properties.getProperty("background.bright"));
            activeColor = Color.decode(properties.getProperty("background.bright2"));
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
