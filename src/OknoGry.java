import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OknoGry extends JFrame {
    private BufferedImage icon;
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;

    public OknoGry() {
        try {
            icon = ImageIO.read(new java.io.File("icons/pionek-100.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Warcaby");
        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setIconImage(icon);
        frame.getContentPane().setBackground(Color.pink);
        frame.setLayout(new GridLayout(1, 3, 20, 20));
        frame.add(new Test(400, 400, 8));
        frame.setJMenuBar(new Menu());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new OknoGry();
    }

}
