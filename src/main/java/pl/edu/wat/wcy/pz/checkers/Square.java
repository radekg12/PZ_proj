package pl.edu.wat.wcy.pz.checkers;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Square implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(Square.class.getSimpleName(), "LogsMessages");
    private static Color lightColor, darkColor, moveColor, jumpColor;
    private static int size;

    static {
        loadProperties();
    }

    private int column;
    private int row;
    private Color squareColor;
    private boolean occupiedSquare;
    private boolean moveSquare;
    private boolean jumpSquare;

    public Square(int column, int row, Color squareColor) {
        this.column = column;
        this.row = row;
        this.squareColor = squareColor;
        occupiedSquare = true;
        moveSquare = false;
    }

    public static Color getLightColor() {
        return lightColor;
    }

    public static void setLightColor(Color lightColor) {
        Square.lightColor = lightColor;
    }

    public static Color getDarkColor() {
        return darkColor;
    }

    public static void setDarkColor(Color darkColor) {
        Square.darkColor = darkColor;
    }

    public static Color getMoveColor() {
        return moveColor;
    }

    public static void setMoveColor(Color moveColor) {
        Square.moveColor = moveColor;
    }

    public static Color getJumpColor() {
        return jumpColor;
    }

    public static void setJumpColor(Color jumpColor) {
        Square.jumpColor = jumpColor;
    }

    public static int getSize() {
        return size;
    }

    public static void setSize(int size) {
        Square.size = size;
    }

    private static void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";

        try {
            input = Square.class.getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);

            lightColor = Color.decode(properties.getProperty("square.lightColor"));
            darkColor = Color.decode(properties.getProperty("square.darkColor"));
            moveColor = Color.decode(properties.getProperty("square.moveColor"));
            jumpColor = Color.decode(properties.getProperty("square.jumpColor"));
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

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Color getSquareColor() {
        return squareColor;
    }

    public void setSquareColor(Color squareColor) {
        this.squareColor = squareColor;
    }

    public boolean isOccupiedSquare() {
        return occupiedSquare;
    }

    public void setOccupiedSquare(boolean occupiedSquare) {
        this.occupiedSquare = occupiedSquare;
    }

    public boolean isMoveSquare() {
        return moveSquare;
    }

    public void setMoveSquare(boolean moveSquare) {
        this.moveSquare = moveSquare;
    }

    public boolean isJumpSquare() {
        return jumpSquare;
    }

    public void setJumpSquare(boolean jumpSquare) {
        this.jumpSquare = jumpSquare;
    }
}
