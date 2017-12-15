import java.util.ArrayList;

public class Gracz {
    private String nazwa;
    private ArrayList<Pionek> pionki = new ArrayList<>();
    private int k;

    public Gracz() {
    }

    public Gracz(String nazwa, int k) {
        this.nazwa = nazwa;
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
}
