package pl.edu.wat.wcy.pz.checkers;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Piece implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(Piece.class.getSimpleName(), "LogsMessages");
    private int x;
    private int y;
    private static int size;
    private int k;
    private Color color;
    private Color activeColor;

    static private Color darkColor;
    static private Color darkActiveColor;
    static private Color lightColor;
    static private Color lightActiveColor;
    static private Color jumpColor;
    private PieceType pieceType;
    private static BufferedImage crown;
    private boolean movePiece;
    private boolean jumpPiece;

    static {
        loadProperties();
        try {
            crown = ImageIO.read(Piece.class.getClassLoader().getResource("icons/korona-100.png"));
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "image.open", e);
        }
    }

    Piece(int x, int y, int k) {

        this.k = k;
        this.x = x;
        this.y = y;
        if (k == 1) {
            color = lightColor;
            activeColor = lightActiveColor;
            pieceType = PieceType.Light;
        } else if (k == -1) {
            color = darkColor;
            activeColor = darkActiveColor;
            pieceType = PieceType.Dark;
        }
        movePiece = false;
        jumpPiece = false;
    }

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void hide() {
        movePiece = false;
    }

    public void show() {
        movePiece = true;
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

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isMovePiece() {
        return movePiece;
    }

    public void setMovePiece(boolean movePiece) {
        this.movePiece = movePiece;
    }

    public boolean isJumpPiece() {
        return jumpPiece;
    }

    public void setJumpPiece(boolean jumpPiece) {
        this.jumpPiece = jumpPiece;
        this.movePiece = jumpPiece;
    }

    public static Color getDarkColor() {
        return darkColor;
    }

    public static void setDarkColor(Color darkColor) {
        Piece.darkColor = darkColor;
    }

    public static Color getLightColor() {
        return lightColor;
    }

    public static void setLightColor(Color lightColor) {
        Piece.lightColor = lightColor;
    }

    public static Color getDarkActiveColor() {
        return darkActiveColor;
    }

    public static void setDarkActiveColor(Color darkActiveColor) {
        Piece.darkActiveColor = darkActiveColor;
    }

    public static Color getLightActiveColor() {
        return lightActiveColor;
    }

    public static void setLightActiveColor(Color lightActiveColor) {
        Piece.lightActiveColor = lightActiveColor;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(Color activeColor) {
        this.activeColor = activeColor;
    }

    public static void setSize(int size) {
        Piece.size = size;
    }

    public static int getSize() {
        return size;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public void changeToQueen() {
        if (k == 1) pieceType = PieceType.LQueen;
        else if (k == -1) pieceType = PieceType.DQueen;
    }

    public static BufferedImage getCrown() {
        return crown;
    }

    public static void setCrown(BufferedImage crown) {
        Piece.crown = crown;
    }

    public static Color getJumpColor() {
        return jumpColor;
    }

    public static void setJumpColor(Color jumpColor) {
        Piece.jumpColor = jumpColor;
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = Piece.class.getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);
            lightColor = Color.decode(properties.getProperty("piece.lightColor"));
            lightActiveColor = Color.decode(properties.getProperty("piece.lightActiveColor"));
            darkColor = Color.decode(properties.getProperty("piece.darkColor"));
            darkActiveColor = Color.decode(properties.getProperty("piece.darkActiveColor"));
            jumpColor = Color.decode(properties.getProperty("piece.jumpColor"));
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


    public static Logger getLOGGER() {
        return LOGGER;
    }
}
