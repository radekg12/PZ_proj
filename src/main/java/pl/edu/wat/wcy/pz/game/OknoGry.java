package pl.edu.wat.wcy.pz.game;

import pl.edu.wat.wcy.pz.checkers.Gra;
import pl.edu.wat.wcy.pz.checkers.Gracz;
import pl.edu.wat.wcy.pz.database.Baza;
import pl.edu.wat.wcy.pz.events.WygranaEvent;
import pl.edu.wat.wcy.pz.frame.OknoGlowne;
import pl.edu.wat.wcy.pz.listeners.WygranaListener;

import javax.swing.*;
import java.awt.*;

public class OknoGry extends JPanel implements WygranaListener {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.game.OknoGry.class.getSimpleName(), "LogsMessages");
    private Gra gra;
    private Gracz gracz1, gracz2;
    private PanelGracza panel1;
    private PanelGracza panel2;
    private GornaBelka gornaBelka;
    private DolnaBelka dolnaBelka;
    private OknoGlowne frame;
    private int time;

    public OknoGry(OknoGlowne frame, Gracz gracz1, Gracz gracz2, int time) {
        super(new GridBagLayout());
        this.frame = frame;
        this.gracz1 = gracz1;
        this.gracz2 = gracz2;
        this.time = time;
        gra = new Gra(frame, gracz1, gracz2);
        panel1 = new PanelGracza(frame, gracz1, gra, time);
        panel2 = new PanelGracza(frame, gracz2, gra, time);
        gornaBelka = new GornaBelka(frame);
        dolnaBelka = new DolnaBelka(frame, gra);
        gra.addWygranaListener(this);
        PanelGracza.addKoniecCzasuListener(gra);
        PanelGracza.addKoniecCzasuListener(dolnaBelka);

        dolnaBelka.addReplayListener(gra);
        dolnaBelka.addReplayListener(panel1);
        dolnaBelka.addReplayListener(panel2);

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
            add(gornaBelka, c);

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
            add(gra, c);

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
            add(dolnaBelka, c);
        });
    }

    @Override
    public void wygrana(WygranaEvent event) {
        int x = 0;
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Baza baza = new Baza();
                baza.zapiszWyniki(gracz1, gracz2);
                baza.closeConnection();
                return null;
            }
        }.execute();
    }
}
