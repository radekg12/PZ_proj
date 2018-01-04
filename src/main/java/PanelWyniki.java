import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PanelWyniki extends JFrame implements ZmienJezykListener {
    WynikiTableModel data;
    JTable table;
    ArrayList<Wyniki> result = new ArrayList<>();


    public PanelWyniki(OknoGlowne frame) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new GridLayout(1, 1));
        data = new WynikiTableModel(result, frame);
        table = new JTable(data);
        table.setPreferredScrollableViewportSize(new Dimension(400, 400));
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

            @Override
            protected void done() {
                super.done();

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
                BufferedImage img = null;
                try {
                    img = ImageIO.read(getClass().getResource("icons/avatars/boy.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Image icon = img.getScaledInstance(50, 50, img.getType());
                l.setIcon(new ImageIcon(icon));
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