
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class Menu extends JMenuBar implements ZmienJezykListener {
    private JMenu file, edit, view, changeLook, changeLanguage;
    private JMenuItem look1, look2, look3, save, exit, chPL, chEN;
    AbstractAction exitAction, changeToPL, changeToEN;
    private Locale locale = Locale.getDefault();
    private ResourceBundle rb = ResourceBundle.getBundle("Languages", locale);
    private ArrayList<ZmienJezykListener> zmienJezykListeners = new ArrayList<>();


    public Menu() {
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
                System.exit(1);
            }
        };

        save = new JMenuItem(null, new ImageIcon(getClass().getResource("icons/save_24.png")));
        exit = new JMenuItem(exitAction);

        changeLook = new JMenu();
        changeLook.setIcon(new ImageIcon(getClass().getResource("icons/look_24.png")));
        look1 = new JMenuItem();
        look2 = new JMenuItem();
        look3 = new JMenuItem();


        look1.addActionListener(new chLook("Look 1", new ImageIcon(getClass().getResource("icons/save_24.png"))));
        look2.addActionListener(new chLook("Look 2", null));
        look3.addActionListener(new chLook("Look 3", null));

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

        changeToPL = new chLanguageAction("pl", "PL", new ImageIcon(getClass().getResource("icons/pl_24.png")));
        changeToEN = new chLanguageAction("en", "US", new ImageIcon(getClass().getResource("icons/en_24.png")));

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
        });

    }


    public void update() {
        SwingUtilities.updateComponentTreeUI(this);
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
                e1.printStackTrace();
            }
            update();
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