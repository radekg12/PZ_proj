import java.awt.*;

public class Pole {
    //    private static Color jasne = new Color(189, 195, 199);
//    private static Color ciemne = new Color(127, 140, 141);
    private static Color jasne = new Color(62, 62, 62);
    private static Color ciemne = new Color(39, 39, 39);
    private static Color aktywneKolor = new Color(155, 89, 182);
    private static Color poleBiciaKolor = new Color(192, 57, 43);
    private static int size;
    private int kolumna;
    private int wiersz;
    private Color color;
    private boolean zajete;
    private boolean aktywne;
    private boolean poleBicia;

    public Pole(int kolumna, int wiersz, Color color) {
        this.kolumna = kolumna;
        this.wiersz = wiersz;
        this.color = color;
        zajete = true;
        aktywne = false;
    }

    public int getKolumna() {
        return kolumna;
    }

    public void setKolumna(int kolumna) {
        this.kolumna = kolumna;
    }

    public int getWiersz() {
        return wiersz;
    }

    public void setWiersz(int wiersz) {
        this.wiersz = wiersz;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isZajete() {
        return zajete;
    }

    public void setZajete(boolean zajete) {
        this.zajete = zajete;
    }

    public boolean isAktywne() {
        return aktywne;
    }

    public void setAktywne(boolean aktywne) {
        this.aktywne = aktywne;
    }

    public boolean isPoleBicia() {
        return poleBicia;
    }

    public void setPoleBicia(boolean poleBicia) {
        this.poleBicia = poleBicia;
    }

    public static Color getJasne() {
        return jasne;
    }

    public static void setJasne(Color jasne) {
        Pole.jasne = jasne;
    }

    public static Color getCiemne() {
        return ciemne;
    }

    public static void setCiemne(Color ciemne) {
        Pole.ciemne = ciemne;
    }

    public static Color getAktywneKolor() {
        return aktywneKolor;
    }

    public static void setAktywneKolor(Color aktywneKolor) {
        Pole.aktywneKolor = aktywneKolor;
    }

    public static Color getPoleBiciaKolor() {
        return poleBiciaKolor;
    }

    public static void setPoleBiciaKolor(Color poleBiciaKolor) {
        Pole.poleBiciaKolor = poleBiciaKolor;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Pole.size = size;
    }
}
