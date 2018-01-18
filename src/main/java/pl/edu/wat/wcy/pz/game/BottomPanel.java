package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.HomePanelAction;
import pl.edu.wat.wcy.pz.checkers.CheckersGame;
import pl.edu.wat.wcy.pz.events.*;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BottomPanel extends JPanel implements ChangeTurnListener, ChangeLanguageListener, WinListener, EndOfTimeListener {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.BottomPanel.class.getSimpleName(), "LogsMessages");
    private JLabel label1, label2;
    private JButton replayButton, homeButton;
    private String turnString, winString, endTimeString;
    private CheckersGame checkersGame;
    private MainFrame frame;
    private AbstractAction replayAction, homeAction;
    private ArrayList<ReplayListener> replayListeners = new ArrayList<>();


    public BottomPanel(MainFrame frame, CheckersGame checkersGame) {
        setLayout(new GridLayout(2, 2));
        checkersGame.addChangeTurnListener(this);
        checkersGame.addWinListener(this);
        this.checkersGame = checkersGame;
        label1 = new JLabel();
        label2 = new JLabel();
        label1.setHorizontalAlignment(JLabel.LEFT);
        label2.setHorizontalAlignment(JLabel.LEFT);
        replayButton = new JButton(replayAction = new AbstractAction(null, new ImageIcon(getClass().getClassLoader().getResource("icons/replay_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                replayButton.setVisible(false);
                homeButton.setVisible(false);
                label2.setVisible(false);
                fireReplayEvent();
            }
        });
        AbstractAction homeAction = new HomePanelAction(frame);
        homeButton = new JButton(homeAction);
        replayButton.setVisible(false);
        homeButton.setVisible(false);
        replayButton.setHorizontalAlignment(JLabel.RIGHT);
        homeButton.setHorizontalAlignment(JLabel.RIGHT);
        label1.setText(checkersGame.getCurrentPlayer().getName() + "  <- " + turnString);
        ChangeLanguageAction.addChangeLanguageListener(this);
        this.frame = frame;
        initGUI();
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(label1);
            add(replayButton);
            add(label2);
            add(homeButton);
        });
    }


    @Override
    public void changeTurn(ChangeTurnEvent event) {
        SwingUtilities.invokeLater(() -> {
            label1.setText(event.getCurrentPlayer().getName() + "  <- " + turnString);
            revalidate();
        });
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            turnString = rb.getString("turnString");
            label1.setText(checkersGame.getCurrentPlayer().getName() + "  <- " + turnString);
            winString = rb.getString("winString");
            endTimeString = rb.getString("endTime");
            replayAction.putValue(Action.NAME, rb.getString("replay"));
            //homeAction.putValue(Action.NAME, rb.getString("homePage"));
        });
    }


    @Override
    public void win(WinEvent event) {
        SwingUtilities.invokeLater(() -> label1.setText(event.getWonPlayer().getName() + "  " + winString + " ! ! ! "));
        replayButton.setVisible(true);
        homeButton.setVisible(true);
        repaint();
    }

    @Override
    public void endOfTime(EndOfTimeEvent event) {
        SwingUtilities.invokeLater(() -> {
            label2.setText(event.getPalyer().getName() + "  " + endTimeString);
            repaint();
        });
    }

    public synchronized void addReplayListener(ReplayListener l) {
        replayListeners.add(l);
    }

    public synchronized void removeReplayListener(ReplayListener l) {
        replayListeners.remove(l);
    }

    private synchronized void fireReplayEvent() {
        ReplayEvent event = new ReplayEvent(this);
        for (ReplayListener l : replayListeners) l.replay(event);
    }
}
