import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Pionek {
    private int x;
    private int y;
    //TODO
    private static int size = 50;
    private int k;
    private Color kolor;
    private Color aktywnyKolor;

    static private Color ciemny;
    static private Color ciemnyAktywny;
    static private Color jasny;
    static private Color jasnyAktywny;
    static private Color kolorBicia;

    private RodzajPionka rodzajPionka;
    private static BufferedImage korona;

    private boolean maRuch;
    private boolean mozeBic;

    static {
        loadProperties();
        try {
            korona = ImageIO.read(Pionek.class.getResource("icons/korona-100.png"));
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

    public static void setSize(int size) {
        Pionek.size = size;
    }

    public static int getSize() {
        return size;
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

    private static void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = Pionek.class.getResource("config.properties").openStream();
            properties.load(input);

            jasny = new Color(Integer.parseInt(properties.getProperty("pionek.jasny"), 16));
            jasnyAktywny = new Color(Integer.parseInt(properties.getProperty("pionek.jasnyAktywny"), 16));
            ciemny = new Color(Integer.parseInt(properties.getProperty("pionek.ciemny"), 16));
            ciemnyAktywny = new Color(Integer.parseInt(properties.getProperty("pionek.ciemnyAktywny"), 16));
            kolorBicia = new Color(Integer.parseInt(properties.getProperty("pionek.kolorBicia"), 16));
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
