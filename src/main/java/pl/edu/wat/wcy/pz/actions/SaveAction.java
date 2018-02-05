package pl.edu.wat.wcy.pz.actions;

import pl.edu.wat.wcy.pz.checkers.CheckersGame;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ResourceBundle;

public class SaveAction extends AbstractAction implements ChangeLanguageListener {
    private static CheckersGame checkersGame;
    private Integer a = 4;

    public SaveAction() {
        super(null, new ImageIcon(ExitAction.class.getClassLoader().getResource("icons/save_24.png")));
        ChangeLanguageAction.addChangeLanguageListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        saveGame();
        //System.exit(0);
    }

    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            putValue(Action.NAME, rb.getString("save"));
        });
    }

    public static void setCheckersGame(CheckersGame checkersGame) {
        SaveAction.checkersGame = checkersGame;
    }

    public void saveGame() {
        //TODO
        try {
            FileOutputStream fileOut = new FileOutputStream("game.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(checkersGame.getPieceTab());
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in game.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream("player.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(checkersGame.getCurrentPlayer());
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in player.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream("opponent.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(checkersGame.getCurrentOpponent());
            out.close();
            fileOut.close();
            System.out.println("Serialized data is saved in opponent.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static CheckersGame getCheckersGame() {
        return checkersGame;
    }
}


