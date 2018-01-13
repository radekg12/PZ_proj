import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.ResourceBundle;

public class PanelWyniki extends JFrame implements ZmienJezykListener {
    private WynikiTableModel data;
    private JTable table;
    private ArrayList<Wyniki> result = new ArrayList<>();
    private int defaultWidth, defaultHeight;


    public PanelWyniki(OknoGlowne frame) {
        loadProperties();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        setLayout(new GridLayout(1, 1));
        data = new WynikiTableModel(result, frame);
        table = new JTable(data);
        WynikiCellRenderer renderer = new WynikiCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        table.setRowHeight(70);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);

        new SwingWorker<ArrayList<Wyniki>, Void>() {
            @Override
            protected ArrayList<Wyniki> doInBackground() throws Exception {
                Baza baza = new Baza();
                baza.sprawdzWyniki(data);
                baza.closeConnection();
                System.out.println("Pobrano wyniki!!!");
                return result;
            }

        }.execute();
        frame.getMenu().addZmienJezykListener(this);
        setVisible(true);
    }


    @Override
    public void changeLocal(ZmienJezykEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            setTitle(rb.getString("score"));
        });
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = getClass().getResource("config.properties").openStream();
            properties.load(input);
            defaultWidth = Integer.parseInt(properties.getProperty("score.defaultWidth"));
            defaultHeight = Integer.parseInt(properties.getProperty("score.defaultHeight"));
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

    class WynikiCellRenderer extends DefaultTableCellRenderer {

        public WynikiCellRenderer() {
            setHorizontalAlignment(RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            l.setHorizontalAlignment(JLabel.LEFT);
            l.setVerticalAlignment(JLabel.CENTER);
            l.setHorizontalTextPosition(JLabel.RIGHT);
            l.setVerticalTextPosition(JLabel.CENTER);
            if (value instanceof Gracz) {
                l.setText(((Gracz) value).getNazwa());
                BufferedImage img;
                try {
                    String name = ((Gracz) value).getAvatarName();
                    if (name != null) {
                        img = ImageIO.read(getClass().getResource("icons/avatars/" + name));
                        Image icon = img.getScaledInstance(50, 50, img.getType());
                        l.setIcon(new ImageIcon(icon));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (((Gracz) value).isWon())
                    l.setForeground(Color.RED);
                else
                    l.setForeground(Color.BLACK);
            }

            if (value instanceof String) {
                l.setText(((String) value).split(" ")[0]);
                l.setForeground(Color.GREEN);
                l.setIcon(null);
                l.setHorizontalAlignment(JLabel.CENTER);
                l.setVerticalAlignment(JLabel.CENTER);
            }
            return l;
        }
    }
}