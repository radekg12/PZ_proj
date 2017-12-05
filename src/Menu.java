
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;


public class Menu extends JMenuBar {
    JMenu file, edit, view, changeLook, changeLanguage;
    JMenuItem look1, look2, look3, save, exit, chPL, chEN;
    AbstractAction exitAction, changeToPL, changeToEN;
    Locale locale = Locale.getDefault();
    ResourceBundle rb;


    public Menu() {
        initMenu();
    }

    private void initMenu() {
        file = new JMenu("File");
        edit = new JMenu("Edit");
        view = new JMenu("View");

        exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(1);
            }
        };


        save = new JMenuItem("Save");
        exit = new JMenuItem(exitAction);


        file.add(save);
        file.add(exit);

        changeLook = new JMenu("Change look");
        look1 = new JMenuItem("Look 1");
        look2 = new JMenuItem("Look 2");
        look3 = new JMenuItem("Look 3");
        changeLook.add(look1);
        changeLook.add(look2);
        changeLook.add(look3);

        view.add(changeLook);

        add(file);
        add(edit);
        add(view);

        look1.addActionListener(new chLook("Look 1"));
        look2.addActionListener(new chLook("Look 2"));
        look3.addActionListener(new chLook("Look 3"));

        class chLanguageAction extends AbstractAction {
            String language;
            String country;

            public chLanguageAction(String language, String country) {
                super(language);
                this.language = language;
                this.country = country;

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                locale = new Locale(language, country);
                changeLocale();
            }
        }

        changeToPL = new chLanguageAction("pl", "PL");
        changeToEN = new chLanguageAction("en", "US");

        changeLanguage = new JMenu("Change language");
        chPL = new JMenuItem(changeToPL);
        chEN = new JMenuItem(changeToEN);

        changeLanguage.add(chPL);
        changeLanguage.add(chEN);

        view.add(changeLanguage);

        changeLocale();
    }


    public void update() {
        SwingUtilities.updateComponentTreeUI(this);
    }

    public void changeLocale() {
        rb=ResourceBundle.getBundle("bundleName", locale);
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
        changeToEN.putValue(Action.NAME, rb.getString("en"));;
    }


    public class chLook extends AbstractAction {

        String plaf;

        public chLook(String look) {
            super(look);

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

}