import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pionek {
    private int x;
    private int y;
    private static int height = 50;
    private static int width = 50;
    private int k;
    private Color kolor;
    private Color aktywnyKolor;
    static private Color ciemny = new Color(22, 160, 133);
    static private Color ciemnyAktywny = new Color(26, 188, 156);
    static private Color jasny = new Color(211, 84, 0);
    static private Color jasnyAktywny = new Color(230, 126, 34);
    static private Color kolorBicia = new Color(191, 56, 44);
    private RodzajPionka rodzajPionka;
    private static BufferedImage korona;

    private boolean maRuch;
    private boolean mozeBic;

    static {
        try {
            korona = ImageIO.read(new File("icons/korona-100.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Pionek(int x, int y, int k) {

        this.k = k;
        this.x = x;
        this.y = y;
        if (k == 1) {
            kolor = jasny;
            aktywnyKolor = jasnyAktywny;
            rodzajPionka = RodzajPionka.Jasny;
        } else if (k == -1) {
            kolor = ciemny;
            aktywnyKolor = ciemnyAktywny;
            rodzajPionka = RodzajPionka.Ciemny;
        }
        maRuch = false;
        mozeBic = false;
    }

    public void ustaw(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void hide() {
        maRuch = false;
    }

    public void show() {
        maRuch = true;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        Pionek.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        Pionek.width = width;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Color getKolor() {
        return kolor;
    }

    public void setKolor(Color kolor) {
        this.kolor = kolor;
    }

    public boolean isMaRuch() {
        return maRuch;
    }

    public void setMaRuch(boolean maRuch) {
        this.maRuch = maRuch;
    }

    public boolean isMozeBic() {
        return mozeBic;
    }

    public void setMozeBic(boolean mozeBic) {
        this.mozeBic = mozeBic;
        this.maRuch = mozeBic;
    }

    public static Color getCiemny() {
        return ciemny;
    }

    public static void setCiemny(Color ciemny) {
        Pionek.ciemny = ciemny;
    }

    public static Color getJasny() {
        return jasny;
    }

    public static void setJasny(Color jasny) {
        Pionek.jasny = jasny;
    }

    public static Color getCiemnyAktywny() {
        return ciemnyAktywny;
    }

    public static void setCiemnyAktywny(Color ciemnyAktywny) {
        Pionek.ciemnyAktywny = ciemnyAktywny;
    }

    public static Color getJasnyAktywny() {
        return jasnyAktywny;
    }

    public static void setJasnyAktywny(Color jasnyAktywny) {
        Pionek.jasnyAktywny = jasnyAktywny;
    }

    public Color getAktywnyKolor() {
        return aktywnyKolor;
    }

    public void setAktywnyKolor(Color aktywnyKolor) {
        this.aktywnyKolor = aktywnyKolor;
    }

    public static void setSize(int width, int height) {
        Pionek.width = width;
        Pionek.height = height;

    }

    public RodzajPionka getRodzajPionka() {
        return rodzajPionka;
    }

    public void setRodzajPionka(RodzajPionka rodzajPionka) {
        this.rodzajPionka = rodzajPionka;
    }

    public void ustawDamke() {
        if (k == 1) rodzajPionka = RodzajPionka.JDama;
        else if (k == -1) rodzajPionka = RodzajPionka.CDama;
    }

    public static BufferedImage getKorona() {
        return korona;
    }

    public static void setKorona(BufferedImage korona) {
        Pionek.korona = korona;
    }

    public static Color getKolorBicia() {
        return kolorBicia;
    }

    public static void setKolorBicia(Color kolorBicia) {
        Pionek.kolorBicia = kolorBicia;
    }
}
