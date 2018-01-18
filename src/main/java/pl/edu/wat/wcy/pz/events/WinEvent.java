package pl.edu.wat.wcy.pz.events;

import pl.edu.wat.wcy.pz.checkers.CheckersGame;
import pl.edu.wat.wcy.pz.checkers.Player;

import java.util.EventObject;

public class WinEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */

    private Player wonPlayer;
    private Player lostPlayer;
    private CheckersGame checkersGame;

    public WinEvent(Object source, Player wonPlayer, Player lostPlayer) {
        super(source);
        this.wonPlayer = wonPlayer;
        this.lostPlayer = lostPlayer;
        this.checkersGame = (CheckersGame) source;
    }

    public Player getWonPlayer() {
        return wonPlayer;
    }

    public Player getLostPlayer() {
        return lostPlayer;
    }

    public CheckersGame getCheckersGame() {
        return checkersGame;
    }
}
