import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Pole {
    private static final Logger LOGGER = Logger.getLogger(Pole.class.getSimpleName(), "LogsMessages");
    private static Color jasne, ciemne, aktywneKolor, poleBiciaKolor;
    private static int size;
    private int kolumna;
    private int wiersz;
    private Color color;
    private boolean zajete;
    private boolean aktywne;
    private boolean poleBicia;

    static {
        loadProperties();
    }

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

    private static void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";

        try {
            input = Pole.class.getResource(propertiesName).openStream();
            properties.load(input);

            jasne = Color.decode(properties.getProperty("square.jasne"));
            ciemne = Color.decode(properties.getProperty("square.ciemne"));
            aktywneKolor = Color.decode(properties.getProperty("square.aktywneKolor"));
            poleBiciaKolor = Color.decode(properties.getProperty("square.poleBiciaKolor"));
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "properties.open", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "properties.close", e);
                }
            }
        }
    }
}
