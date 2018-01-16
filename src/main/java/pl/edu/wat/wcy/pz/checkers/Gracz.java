package pl.edu.wat.wcy.pz.checkers;

import java.awt.*;
import java.util.ArrayList;

public class Gracz {
    //private static final Logger LOGGER = Logger.getLogger(pl.edu.wat.wcy.pz.checkers.Gracz.class.getSimpleName(), "LogsMessages");
    private String nazwa;
    private ArrayList<Pionek> pionki = new ArrayList<>();
    private Image avatar;
    private String avatarName;
    private Color color;
    private int k;
    private boolean won = false;
    private int time;
    private int moves;

    public Gracz() {
    }

    public Gracz(String nazwa, Image avatar, String avatarName, int k) {
        this.nazwa = nazwa;
        this.avatar = avatar;
        this.avatarName = avatarName;
        this.k = k;
        time = 0;
        moves = 0;
        if (k == 1) {
            color = Pionek.getJasny();
        } else if (k == -1) {
            color = Pionek.getCiemny();
        }
    }


    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public ArrayList<Pionek> getPionki() {
        return pionki;
    }

    public void setPionki(ArrayList<Pionek> pionki) {
        this.pionki = pionki;
    }

    public void dodajPionek(Pionek pionek) {
        pionki.add(pionek);
    }

    public void usunPionek(Pionek pionek) {
        pionki.remove(pionek);
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

    public Gracz setAvatar(Image avatar) {
        this.avatar = avatar;
        return this;
    }

    public boolean isWon() {
        return won;
    }

    public Gracz setWon(boolean won) {
        this.won = won;
        return this;
    }

    public String getAvatarName() {
        return avatarName;
    }

    public Gracz setAvatarName(String avatarName) {
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

    public Gracz setColor(Color color) {
        this.color = color;
        return this;
    }

    public Gracz setTime(int time) {
        this.time = time;
        return this;
    }

    public Gracz setMoves(int moves) {
        this.moves = moves;
        return this;
    }
}
