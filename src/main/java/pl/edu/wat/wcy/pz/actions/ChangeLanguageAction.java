package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChangeLanguageAction extends AbstractAction implements ChangeLanguageListener {
    private String language;
    private String country;
    private static Locale locale = Locale.getDefault();
    private static ResourceBundle rb = ResourceBundle.getBundle("Languages", locale);
    private static ArrayList<ChangeLanguageListener> changeLanguageListeners = new ArrayList<>();

    public ChangeLanguageAction(String language, String country) {
        super(language, new ImageIcon(ChangeLookAction.class.getClassLoader().getResource("icons/" + language + "_24.png")));
        this.language = language;
        this.country = country;
        addChangeLanguageListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        locale = new Locale(language, country);
        rb = ResourceBundle.getBundle("Languages", locale);
        fireChangeLanguageEvent();
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString(language));
        });
    }

    public static void addChangeLanguageListener(ChangeLanguageListener l) {
        changeLanguageListeners.add(l);
        l.changeLocal(new ChangeLanguageEvent(ChangeLookAction.class, rb));
    }

    public static void removeChangeLanguageListener(ChangeLanguageListener l) {
        changeLanguageListeners.remove(l);
    }

    private void fireChangeLanguageEvent() {
        ChangeLanguageEvent event = new ChangeLanguageEvent(this, rb);
        for (ChangeLanguageListener l : changeLanguageListeners) l.changeLocal(event);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static ResourceBundle getRb() {
        return rb;
    }
}
