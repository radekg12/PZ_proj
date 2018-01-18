package pl.edu.wat.wcy.pz.score;

import pl.edu.wat.wcy.pz.actions.ChangeLanguageAction;
import pl.edu.wat.wcy.pz.checkers.Player;
import pl.edu.wat.wcy.pz.database.Database;
import pl.edu.wat.wcy.pz.events.ChangeLanguageEvent;
import pl.edu.wat.wcy.pz.frame.MainFrame;
import pl.edu.wat.wcy.pz.listeners.ChangeLanguageListener;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScorePanel extends JFrame implements ChangeLanguageListener {
    private static final Logger LOGGER = Logger.getLogger(ScorePanel.class.getSimpleName(), "LogsMessages");
    private ScoreTableModel model;
    private JTable table;
    private ArrayList<Score> result = new ArrayList<>();
    private int defaultWidth, defaultHeight, avatarSize, medalSize;
    private BufferedImage medalImg;

    public ScorePanel(MainFrame frame) {
        loadProperties();
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(defaultWidth, defaultHeight);
        setLayout(new GridLayout(1, 1));
        model = new ScoreTableModel(result, frame);
        table = new JTable(model);
        ScoreCellRenderer renderer = new ScoreCellRenderer();
        table.setDefaultRenderer(Object.class, renderer);
        table.setRowHeight(avatarSize + 20);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
        try {
            medalImg = ImageIO.read(getClass().getClassLoader().getResource("icons/won_70.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new SwingWorker<ArrayList<Score>, Void>() {
            @Override
            protected ArrayList<Score> doInBackground() throws Exception {
                Database database = new Database();
                database.checkScore(model);
                database.closeConnection();
                return result;
            }

        }.execute();
        ChangeLanguageAction.addChangeLanguageListener(this);
        setVisible(true);
    }


    @Override
    public void changeLocal(ChangeLanguageEvent event) {
        SwingUtilities.invokeLater(() -> {
            ResourceBundle rb = event.getRb();
            setTitle(rb.getString("score"));
        });
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;
        String propertiesName = "config.properties";
        try {
            input = getClass().getClassLoader().getResource(propertiesName).openStream();
            properties.load(input);
            defaultWidth = Integer.parseInt(properties.getProperty("score.defaultWidth"));
            defaultHeight = Integer.parseInt(properties.getProperty("score.defaultHeight"));
            avatarSize = Integer.parseInt(properties.getProperty("score.avatarSize"));
            medalSize = Integer.parseInt(properties.getProperty("score.medalSize"));
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

    class ScoreCellRenderer extends DefaultTableCellRenderer {

        public ScoreCellRenderer() {
            setHorizontalAlignment(RIGHT);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            panel.setOpaque(false);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 4;
            c.weighty = 1;

            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            panel.add(l, c);
            l.setHorizontalAlignment(JLabel.LEFT);
            l.setVerticalAlignment(JLabel.CENTER);
            l.setHorizontalTextPosition(JLabel.RIGHT);
            l.setVerticalTextPosition(JLabel.CENTER);
            if (value instanceof Player) {
                l.setText(((Player) value).getName());
                BufferedImage img;
                try {
                    String name = ((Player) value).getAvatarName();
                    Color color = ((Player) value).getColor();
                    if (name != null) {
                        img = ImageIO.read(getClass().getClassLoader().getResource("icons/avatars/" + name));
                        Image icon = img.getScaledInstance(avatarSize, avatarSize, img.getType());
                        l.setIcon(new ImageIcon(icon));
                        l.setForeground(color);
                    }
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "image.open", e);
                }
                if (((Player) value).isWon()) {
                    Image icon = medalImg.getScaledInstance(medalSize, medalSize, medalImg.getType());
                    c.gridx = 1;
                    c.weightx = 1;
                    panel.add(new JLabel(new ImageIcon(icon)), c);
                }

            }

            if (value instanceof String) {
                l.setText(((String) value).split(" ")[0]);
                l.setForeground(Color.BLACK);
                l.setIcon(null);
                l.setHorizontalAlignment(JLabel.CENTER);
                l.setVerticalAlignment(JLabel.CENTER);
            }
            return panel;
        }
    }
}