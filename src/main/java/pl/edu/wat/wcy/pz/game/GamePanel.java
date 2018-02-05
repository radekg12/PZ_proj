package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.checkers.CheckersGame;
import pl.edu.wat.wcy.pz.checkers.Player;
import pl.edu.wat.wcy.pz.database.Database;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.events.WinEvent;
import pl.edu.wat.wcy.pz.exceptions.TooShortGameException;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;
import pl.edu.wat.wcy.pz.listeners.WinListener;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GamePanel extends JPanel implements WinListener, ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.GamePanel.class.getSimpleName(), "LogsMessages");
    private CheckersGame checkersGame;
    private Player player1, player2;
    private PlayerPanel panel1;
    private PlayerPanel panel2;
    private TopPanel topPanel;
    private BottomPanel bottomPanel;
    private int time;
    private String message;

    public GamePanel(MainFrame frame, Player player1, Player player2, int time) {
        super(new GridBagLayout());
        this.player1 = player1;
        this.player2 = player2;
        this.time = time;
        checkersGame = new CheckersGame(frame, player1, player2);
        panel1 = new PlayerPanel(frame, player1, checkersGame, time);
        panel2 = new PlayerPanel(frame, player2, checkersGame, time);
        topPanel = new TopPanel(frame);
        bottomPanel = new BottomPanel(frame, checkersGame);
        checkersGame.addWinListener(this);
        PlayerPanel.addEndOfTimeListener(checkersGame);
        PlayerPanel.addEndOfTimeListener(bottomPanel);

        bottomPanel.addReplayListener(checkersGame);
        bottomPanel.addReplayListener(panel1);
        bottomPanel.addReplayListener(panel2);
        ChangeLanguageAction.addChangeLanguageListener(this);
        initGUI();

    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            GridBagConstraints c = new GridBagConstraints();

            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;
            c.insets = new Insets(10, 10, 10, 10);
            c.fill = GridBagConstraints.BOTH;
            add(topPanel, c);

            c.gridx = 0;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 8;
            add(panel1, c);

            c.gridx = 1;
            c.gridy = 1;
            c.gridwidth = 1;
            c.gridheight = 2;
            c.weightx = 2;
            c.weighty = 8;
            add(checkersGame, c);

            c.gridx = 0;
            c.gridy = 2;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 8;
            add(panel2, c);

            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.gridheight = 1;
            c.weightx = 1;
            c.weighty = 1;
            add(bottomPanel, c);
        });
    }

    @Override
    public void win(WinEvent event) {
        int x = 0;
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                Database database = new Database();
                try {
                    database.saveScore(player1, player2);
                } catch (TooShortGameException e) {
                    JOptionPane.showMessageDialog(null, message);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "db.save", e);
                }
                database.closeConnection();
                return null;
            }
        }.execute();
    }


    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        ResourceBundle rb = event.getRb();
        message = rb.getString("dialogMessage");
    }

    public static Logger getLOGGER() {
        return LOGGER;
    }

    public CheckersGame getCheckersGame() {
        return checkersGame;
    }

    public GamePanel setCheckersGame(CheckersGame checkersGame) {
        this.checkersGame = checkersGame;
        return this;
    }

    public Player getPlayer1() {
        return player1;
    }

    public GamePanel setPlayer1(Player player1) {
        this.player1 = player1;
        return this;
    }

    public Player getPlayer2() {
        return player2;
    }

    public GamePanel setPlayer2(Player player2) {
        this.player2 = player2;
        return this;
    }

    public PlayerPanel getPanel1() {
        return panel1;
    }

    public GamePanel setPanel1(PlayerPanel panel1) {
        this.panel1 = panel1;
        return this;
    }

    public PlayerPanel getPanel2() {
        return panel2;
    }

    public GamePanel setPanel2(PlayerPanel panel2) {
        this.panel2 = panel2;
        return this;
    }

    public TopPanel getTopPanel() {
        return topPanel;
    }

    public GamePanel setTopPanel(TopPanel topPanel) {
        this.topPanel = topPanel;
        return this;
    }

    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    public GamePanel setBottomPanel(BottomPanel bottomPanel) {
        this.bottomPanel = bottomPanel;
        return this;
    }

    public int getTime() {
        return time;
    }

    public GamePanel setTime(int time) {
        this.time = time;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public GamePanel setMessage(String message) {
        this.message = message;
        return this;
    }
}
