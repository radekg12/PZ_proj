package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.checkers.Gra;
import pl.edu.wat.wcy.pz.events.*;
import pl.edu.wat.wcy.pz.frame.OknoGlowne;
import pl.edu.wat.wcy.pz.frame.StronaStartowa;
import pl.edu.wat.wcy.pz.listeners.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DolnaBelka extends JPanel implements ZmienKolejListener, ZmienJezykListener, WygranaListener, KoniecCzasuListener {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.DolnaBelka.class.getSimpleName(), "LogsMessages");
    private JLabel label1, label2;
    private JButton replayButton, homeButton;
    private String turnString, winString, endTimeString;
    private Gra gra;
    private OknoGlowne frame;
    private AbstractAction replayAction, homeAction;
    private ArrayList<ReplayListener> replayListeners = new ArrayList<>();


    public DolnaBelka(OknoGlowne frame, Gra gra) {
        setLayout(new GridLayout(2, 2));
        gra.addZmienKolejListener(this);
        gra.addWygranaListener(this);
        this.gra = gra;
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
        homeButton = new JButton(homeAction = new AbstractAction(null, new ImageIcon(getClass().getClassLoader().getResource("icons/home_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.zmianaOkna(new StronaStartowa(frame));
            }
        });
        replayButton.setVisible(false);
        homeButton.setVisible(false);
        replayButton.setHorizontalAlignment(JLabel.RIGHT);
        homeButton.setHorizontalAlignment(JLabel.RIGHT);
        label1.setText(gra.getAktualnyGracz().getNazwa() + "  <- " + turnString);
        frame.getMenu().addZmienJezykListener(this);
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
    public void zmienKolej(ZmienKolejEvent event) {
        SwingUtilities.invokeLater(() -> {
            label1.setText(event.getAktualnyGracz().getNazwa() + "  <- " + turnString);
            revalidate();
        });
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            turnString = rb.getString("turnString");
            label1.setText(gra.getAktualnyGracz().getNazwa() + "  <- " + turnString);
            winString = rb.getString("winString");
            endTimeString = rb.getString("endTime");
            replayAction.putValue(Action.NAME, rb.getString("replay"));
            homeAction.putValue(Action.NAME, rb.getString("homePage"));
        });
    }


    @Override
    public void wygrana(WygranaEvent event) {
        SwingUtilities.invokeLater(() -> label1.setText(event.getWygrany().getNazwa() + "  " + winString + " ! ! ! "));
        replayButton.setVisible(true);
        homeButton.setVisible(true);
        repaint();
    }

    @Override
    public void koniecCzasu(KoniecCzasuEvent event) {
        SwingUtilities.invokeLater(() -> {
            label2.setText(event.getPalyer().getNazwa() + "  " + endTimeString);
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
        for (ReplayListener l : replayListeners) l.clean(event);
    }
}
