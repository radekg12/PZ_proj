package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.checkers.CheckersGame;
import pl.edu.wat.wcy.pz.checkers.Player;
import pl.edu.wat.wcy.pz.events.ChangeTurnEvent;
import pl.edu.wat.wcy.pz.events.EndOfTimeEvent;
import pl.edu.wat.wcy.pz.events.ReplayEvent;
import pl.edu.wat.wcy.pz.events.WinEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeTurnListener;
import pl.edu.wat.wcy.pz.listeners.EndOfTimeListener;
import pl.edu.wat.wcy.pz.listeners.ReplayListener;
import pl.edu.wat.wcy.pz.listeners.WinListener;

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

public class PlayerPanel extends JPanel implements ChangeTurnListener, WinListener, ReplayListener {
    private static final Logger LOGGER = Logger.getLogger(PlayerPanel.class.getSimpleName(), "LogsMessages");
    private Player player;
    private boolean active;
    private Color color, activeColor, foreground, foreground2;
    private JLabel nameLabel, avatarLabel, timerLabel;
    private boolean won;
    private BufferedImage medal;
    private ImageIcon hourglass, timeEnd;
    private Timer timer;
    private final int time;
    private int tmpTime;
    private static ArrayList<EndOfTimeListener> endOfTimeListeners = new ArrayList<>();

    public PlayerPanel(MainFrame frame, Player player, CheckersGame checkersGame, int time) {
        loadProperties();
        this.player = player;
        this.time = time;
        won = false;
        active = player.getK() == 1;
        setBackground(active ? activeColor : color);
        try {
            medal = ImageIO.read(new File(String.valueOf(getClass().getClassLoader().getResource("icons/won_70.png")).replace("file:/", "")));
            hourglass = new ImageIcon(getClass().getClassLoader().getResource("icons/hourglass_24.gif"));
            timeEnd = new ImageIcon(getClass().getClassLoader().getResource("icons/timeEnd_24.png"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "image.open", e);
        }
        nameLabel = new JLabel(player.getName());
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        avatarLabel = new JLabel(player.getAvatar());
        tmpTime = time;
        timerLabel = new JLabel(String.valueOf(tmpTime), hourglass, JLabel.LEFT);
        timerLabel.setMinimumSize(new Dimension(hourglass.getIconWidth(), hourglass.getIconHeight()));
        checkersGame.addChangeTurnListener(this);
        checkersGame.addWinListener(this);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmpTime--;
                if (tmpTime <= 5) timerLabel.setForeground(foreground2);
                SwingUtilities.invokeLater(() -> timerLabel.setText(String.valueOf(tmpTime)));
                if (tmpTime <= 0) {
                    timer.stop();
                    timerLabel.setIcon(timeEnd);
                    fireEndOfTimeEvent();
                }
            }
        });
        if (active) {
            timer.start();
        }
        initGUI();
    }

    private void initGUI() {
        SwingUtilities.invokeLater(() -> {
            setLayout(new BorderLayout());
            add(nameLabel, BorderLayout.NORTH);
            add(avatarLabel, BorderLayout.CENTER);
            if (!active)
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
    public void changeTurn(ChangeTurnEvent event) {
        active = !active;
        SwingUtilities.invokeLater(() -> {
            if (active) {
                timer.start();
                //timerLabel.setIcon(hourglass);
                timerLabel.setVisible(true);
                timerLabel.setForeground(foreground);
            } else {
                timer.stop();
                player.addTime(time - tmpTime);
                tmpTime = time;
                timerLabel.setText(String.valueOf(tmpTime));
                //timerLabel.setIcon(null);
                timerLabel.setVisible(false);
            }
            setBackground(active ? activeColor : color);
        });
        revalidate();
    }


    @Override
    public void win(WinEvent event) {
        timer.stop();
        player.addTime(time - tmpTime);
        if (event.getWonPlayer() == player) {
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

    public static synchronized void addEndOfTimeListener(EndOfTimeListener l) {
        endOfTimeListeners.add(l);
    }

    public static synchronized void removeEndOfTimeListener(EndOfTimeListener l) {
        endOfTimeListeners.remove(l);
    }

    private synchronized void fireEndOfTimeEvent() {
        EndOfTimeEvent event = new EndOfTimeEvent(this, player);
        for (EndOfTimeListener l : endOfTimeListeners)
            l.endOfTime(event);
    }

    @Override
    public void replay(ReplayEvent event) {
        won = false;
        timerLabel.setIcon(hourglass);
        player.setWon(false);
        player.setMoves(0);
        player.setTime(0);
        tmpTime = time;
        active = player.getK() == 1;
        repaint();
    }
}
