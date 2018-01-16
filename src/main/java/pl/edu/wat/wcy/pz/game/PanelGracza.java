package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.checkers.Gra;
import pl.edu.wat.wcy.pz.checkers.Gracz;
import pl.edu.wat.wcy.pz.events.KoniecCzasuEvent;
import pl.edu.wat.wcy.pz.events.ReplayEvent;
import pl.edu.wat.wcy.pz.events.WygranaEvent;
import pl.edu.wat.wcy.pz.events.ZmienKolejEvent;
import pl.edu.wat.wcy.pz.frame.OknoGlowne;
import pl.edu.wat.wcy.pz.frame.StronaStartowa;
import pl.edu.wat.wcy.pz.listeners.KoniecCzasuListener;
import pl.edu.wat.wcy.pz.listeners.ReplayListener;
import pl.edu.wat.wcy.pz.listeners.WygranaListener;
import pl.edu.wat.wcy.pz.listeners.ZmienKolejListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PanelGracza extends JPanel implements ZmienKolejListener, WygranaListener, ReplayListener {
    private static final Logger LOGGER = Logger.getLogger(PanelGracza.class.getSimpleName(), "LogsMessages");
    private JButton button = new JButton("usun");
    private Gracz gracz;
    private boolean aktywny;
    private Color color, activeColor, foreground, foreground2;
    private JLabel nazwaLabel, avatarLabel, timerLabel;
    private boolean won;
    private BufferedImage medal;
    private ImageIcon hourglass, timeEnd;
    private Timer timer;
    private int time;
    private int tmpTime;
    private static ArrayList<KoniecCzasuListener> koniecCzasuListeners = new ArrayList<>();

    public PanelGracza(OknoGlowne frame, Gracz gracz, Gra gra, int time) {
        loadProperties();
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new StronaStartowa(frame));
            }
        });
        this.gracz = gracz;
        this.time = time;
        won = false;
        aktywny = gracz.getK() == 1;
        setBackground(aktywny ? activeColor : color);
        try {
            medal = ImageIO.read(new File(String.valueOf(getClass().getClassLoader().getResource("icons/won_70.png")).replace("file:/", "")));
            hourglass = new ImageIcon(getClass().getClassLoader().getResource("icons/hourglass_24.gif"));
            timeEnd = new ImageIcon(getClass().getClassLoader().getResource("icons/timeEnd_24.png"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "image.open", e);
        }
        nazwaLabel = new JLabel(gracz.getNazwa());
        nazwaLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel = new JLabel(new ImageIcon(gracz.getAvatar()));
        tmpTime = time;
        timerLabel = new JLabel(String.valueOf(tmpTime), hourglass, JLabel.LEFT);
        timerLabel.setMinimumSize(new Dimension(hourglass.getIconWidth(), hourglass.getIconHeight()));
        gra.addZmienKolejListener(this);
        gra.addWygranaListener(this);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmpTime--;
                if (tmpTime <= 5) timerLabel.setForeground(foreground2);
                SwingUtilities.invokeLater(() -> timerLabel.setText(String.valueOf(tmpTime)));
                if (tmpTime <= 0) {
                    timer.stop();
                    timerLabel.setIcon(timeEnd);
                    fireKoniecCzasuEvent();
                }
            }
        });
        if (aktywny) {
            timer.start();
        }
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            setLayout(new BorderLayout());
            add(nazwaLabel, BorderLayout.NORTH);
            add(avatarLabel, BorderLayout.CENTER);
            if (!aktywny)
                //timerLabel.setIcon(hourglass);
                timerLabel.setVisible(false);
            add(timerLabel, BorderLayout.SOUTH);
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (won)
            g.drawImage(medal, 0, 0, null);
    }

    @Override
    public void zmienKolej(ZmienKolejEvent event) {
        aktywny = !aktywny;
        SwingUtilities.invokeLater(() -> {
            if (aktywny) {
                timer.start();
                //timerLabel.setIcon(hourglass);
                timerLabel.setVisible(true);
                timerLabel.setForeground(foreground);
            } else {
                timer.stop();
                gracz.addTime(time - tmpTime);
                tmpTime = time;
                timerLabel.setText(String.valueOf(tmpTime));
                //timerLabel.setIcon(null);
                timerLabel.setVisible(false);
            }
            setBackground(aktywny ? activeColor : color);
        });
        revalidate();
    }


    @Override
    public void wygrana(WygranaEvent event) {
        timer.stop();
        gracz.addTime(time - tmpTime);
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
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);
            color = Color.decode(properties.getProperty("background.bright"));
            activeColor = Color.decode(properties.getProperty("background.bright2"));
            foreground = Color.decode(properties.getProperty("foreground"));
            foreground2 = Color.decode(properties.getProperty("foreground2"));
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

    public static synchronized void addKoniecCzasuListener(KoniecCzasuListener l) {
        koniecCzasuListeners.add(l);
    }

    public static synchronized void removeKoniecCzasuListener(KoniecCzasuListener l) {
        koniecCzasuListeners.remove(l);
    }

    private synchronized void fireKoniecCzasuEvent() {
        KoniecCzasuEvent event = new KoniecCzasuEvent(this, gracz);
        for (KoniecCzasuListener l : koniecCzasuListeners) l.koniecCzasu(event);
    }

    @Override
    public void clean(ReplayEvent event) {
        won = false;
        timerLabel.setIcon(hourglass);
        gracz.setWon(false);
        gracz.setMoves(0);
        gracz.setTime(0);
        repaint();
    }
}
