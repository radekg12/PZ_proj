import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class OknoGry extends JFrame {
    private BufferedImage icon;
    public static final int DEFAULT_WIDTH = 500;
    public static final int DEFAULT_HEIGHT = 500;
    private Menu menu;

    public OknoGry(String title) {
        super(title);
        try {
            icon = ImageIO.read(new java.io.File("icons/pionek-100.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setIconImage(icon);
        getContentPane().setBackground(Color.pink);
        menu=new Menu();
        setJMenuBar(menu);
        //setLayout(new GridLayout(1, 3, 20, 20));
        //getContentPane().add(new Test(this, 400, 400, 8));
        //panel.setSize(300,300);
        //panel.setBackground(Color.green.darker());
        //etContentPane().add(panel);
        
        getContentPane().add(new StronaStartowa(this));

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OknoGry("Warcaby"));
    }


    public void zmianaOkna(Component component) {
        getContentPane().removeAll();
        getContentPane().invalidate();

        getContentPane().add(component);
        getContentPane().revalidate();
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
