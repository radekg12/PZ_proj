import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Menu extends JMenuBar implements ZmienJezykListener {
    private static final Logger LOGGER = Logger.getLogger(Menu.class.getSimpleName(), "LogsMessages");
    private OknoGlowne frame;
    private JMenu file, edit, view, changeLook, changeLanguage;
    private JMenuItem look1, look2, look3, save, exit, chPL, chEN, score;
    AbstractAction exitAction, scoreAcrion;
    private AbstractAction changeToPL;
    private AbstractAction changeToEN;
    private Locale locale = Locale.getDefault();
    private ResourceBundle rb = ResourceBundle.getBundle("Languages", locale);
    private ArrayList<ZmienJezykListener> zmienJezykListeners = new ArrayList<>();


    public Menu(OknoGlowne frame) {
        this.frame = frame;
        initMenu();
        initGUI();
    }

    private void initMenu() {
        file = new JMenu();
        edit = new JMenu();
        view = new JMenu();

        exitAction = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/exit_24.png"))) {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };

        scoreAcrion = new AbstractAction(null, new ImageIcon(getClass().getResource("icons/wyniki_24.png"))) {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PanelWyniki(frame);
            }
        };

        save = new JMenuItem(null, new ImageIcon(getClass().getResource("icons/save_24.png")));
        exit = new JMenuItem(exitAction);
        score = new JMenuItem(scoreAcrion);

        changeLook = new JMenu();
        changeLook.setIcon(new ImageIcon(getClass().getResource("icons/look_24.png")));
        look1 = new JMenuItem(new chLook("Look 1", new ImageIcon(getClass().getResource("icons/l1_24.png"))));
        look2 = new JMenuItem(new chLook("Look 2", new ImageIcon(getClass().getResource("icons/l2_24.png"))));
        look3 = new JMenuItem(new chLook("Look 3", new ImageIcon(getClass().getResource("icons/l3_24.png"))));

        class chLanguageAction extends AbstractAction {
            String language;
            String country;

            public chLanguageAction(String language, String country, Icon icon) {
                super(language, icon);
                this.language = language;
                this.country = country;

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                locale = new Locale(language, country);
                rb = ResourceBundle.getBundle("Languages", locale);
                fireZmianaJezykaEvent();
            }
        }

        changeToPL = new chLanguageAction("pl", "PL", new ImageIcon(getClass().getResource("icons/pll_24.png")));
        changeToEN = new chLanguageAction("en", "US", new ImageIcon(getClass().getResource("icons/uk_24.png")));

        changeLanguage = new JMenu();
        changeLanguage.setIcon(new ImageIcon(getClass().getResource("icons/lang_24.png")));
        chPL = new JMenuItem(changeToPL);
        chEN = new JMenuItem(changeToEN);

        addZmienJezykListener(this);
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(file);
            file.add(save);
            file.add(score);
            file.add(exit);

            add(edit);

            add(view);
            view.add(changeLook);
            changeLook.add(look1);
            changeLook.add(look2);
            changeLook.add(look3);
            view.add(changeLanguage);
            changeLanguage.add(chPL);
            changeLanguage.add(chEN);
            look1.doClick();
        });

    }

    public OknoGlowne getFrame() {
        return frame;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    public ResourceBundle getRb() {
        return rb;
    }

    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            file.setText(rb.getString("file"));
            edit.setText(rb.getString("edit"));
            view.setText(rb.getString("view"));
            look1.setText(rb.getString("look1"));
            look2.setText(rb.getString("look2"));
            look3.setText(rb.getString("look3"));
            save.setText(rb.getString("save"));
            exitAction.putValue(Action.NAME, rb.getString("exit"));
            scoreAcrion.putValue(Action.NAME, rb.getString("score"));
            changeLook.setText(rb.getString("chLook"));
            changeLanguage.setText(rb.getString("chLanguage"));
            changeToPL.putValue(Action.NAME, rb.getString("pl"));
            changeToEN.putValue(Action.NAME, rb.getString("en"));
        });
    }


    public class chLook extends AbstractAction {

        String plaf;

        public chLook(String look, Icon icon) {
            super(look, icon);

            switch (look) {
                case "Look 1":
                    plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
                    break;
                case "Look 2":
                    plaf = "javax.swing.plaf.nimbus.NimbusLookAndFeel";
                    break;
                case "Look 3":
                    plaf = "javax.swing.plaf.metal.MetalLookAndFeel";
                    break;
                default:
                    plaf = UIManager.getSystemLookAndFeelClassName();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                UIManager.setLookAndFeel(plaf);
            } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e1) {
                LOGGER.log(Level.WARNING,"plaf.change" , e);
            }
            SwingUtilities.updateComponentTreeUI(frame);
        }
    }

    public synchronized void addZmienJezykListener(ZmienJezykListener l) {
        zmienJezykListeners.add(l);
        l.changeLocal(new ZmienJezykEvent(this, rb));
    }

    public synchronized void removeZmienJezykListener(ZmienJezykListener l) {
        zmienJezykListeners.remove(l);
    }

    private synchronized void fireZmianaJezykaEvent() {
        ZmienJezykEvent event = new ZmienJezykEvent(this, rb);
        for (ZmienJezykListener l : zmienJezykListeners) l.changeLocal(event);
    }

}