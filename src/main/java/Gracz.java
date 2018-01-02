import java.awt.*;
import java.util.ArrayList;

public class Gracz {
    private String nazwa;
    private ArrayList<Pionek> pionki = new ArrayList<>();
    private Image avatar;
    private int k;
    boolean won = false;

    public Gracz() {
    }

    public Gracz(String nazwa, Image avatar, int k) {
        this.nazwa = nazwa;
        this.avatar = avatar;
        this.k = k;

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
}
