package pl.edu.wat.wcy.pz.checkers;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.checkers.Player.class.getSimpleName(), "LogsMessages");
    private String name;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private Image avatar;
    private String avatarName;
    private Color color;
    private int k;
    private boolean won = false;
    private int time;
    private int moves;

    public Player() {
    }

    public Player(String name, Image avatar, String avatarName, int k) {
        this.name = name;
        this.avatar = avatar;
        this.avatarName = avatarName;
        this.k = k;
        time = 0;
        moves = 0;
        if (k == 1) {
            color = Piece.getLightColor();
        } else if (k == -1) {
            color = Piece.getDarkColor();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Piece> pieces) {
        this.pieces = pieces;
    }

    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    public void removePiece(Piece piece) {
        pieces.remove(piece);
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Image getAvatar() {
        return avatar;
    }

    public Player setAvatar(Image avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isWon() {
        return won;
    }

    public Player setWon(boolean won) {
        this.won = won;
        return this;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public Player setAvatarName(String avatarName) {
        this.avatarName = avatarName;
        return this;
    }

    public void addTime(int x) {
        time += x;
    }

    public void movesInc() {
        moves++;
    }

    public Color getColor() {
        return color;
    }

    public int getTime() {
        return time;
    }

    public int getMoves() {
        return moves;
    }

    public Player setColor(Color color) {
        this.color = color;
        return this;
    }

    public Player setTime(int time) {
        this.time = time;
        return this;
    }

    public Player setMoves(int moves) {
        this.moves = moves;
        return this;
    }
}
