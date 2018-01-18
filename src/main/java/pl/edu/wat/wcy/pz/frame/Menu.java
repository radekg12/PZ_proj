package pl.edu.wat.wcy.pz.frame;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.actions.ChangeLookAction;
import pl.edu.wat.wcy.pz.actions.ExitAction;
import pl.edu.wat.wcy.pz.actions.ScoreAction;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Menu extends JMenuBar implements ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(Menu.class.getSimpleName(), "LogsMessages");
    private MainFrame frame;
    private JMenu file, view, changeLook, changeLanguage;
    private JMenuItem look1, look2, look3, exit, chPL, chEN, score;
    private AbstractAction exitAction, scoreAction, changeToPLAction, changeToENAction;


    public Menu(MainFrame frame) {
        this.frame = frame;
        initMenu();
        initGUI();
    }

    private void initMenu() {
        file = new JMenu();
        view = new JMenu();

        exitAction = new ExitAction();
        exit = new JMenuItem(exitAction);
        scoreAction = new ScoreAction(frame);
        score = new JMenuItem(scoreAction);

        changeLook = new JMenu();
        changeLook.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/look_24.png")));
        look1 = new JMenuItem(new ChangeLookAction("Look 1", frame));
        look2 = new JMenuItem(new ChangeLookAction("Look 2", frame));
        look3 = new JMenuItem(new ChangeLookAction("Look 3", frame));

        changeLanguage = new JMenu();
        changeLanguage.setIcon(new ImageIcon(getClass().getClassLoader().getResource("icons/lang_24.png")));

        changeToPLAction = new ChangeLanguageAction("pl", "PL");
        changeToENAction = new ChangeLanguageAction("en", "EN");
        chPL = new JMenuItem(changeToPLAction);
        chEN = new JMenuItem(changeToENAction);

        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    public void initGUI() {
        SwingUtilities.invokeLater(() -> {
            add(file);
            file.add(score);
            file.add(exit);

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

    public MainFrame getFrame() {
        return frame;
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            file.setText(rb.getString("file"));
            view.setText(rb.getString("view"));
            look1.setText(rb.getString("look1"));
            look2.setText(rb.getString("look2"));
            look3.setText(rb.getString("look3"));
            changeLanguage.setText(rb.getString("chLanguage"));
            changeLook.setText(rb.getString("chLook"));
            //exitAction.putValue(Action.NAME, rb.getString("exit"));
            //scoreAction.putValue(Action.NAME, rb.getString("score"));
            //changeToPLAction.putValue(Action.NAME, rb.getString("pl"));
            //changeToENAction.putValue(Action.NAME, rb.getString("en"));
        });
    }

}