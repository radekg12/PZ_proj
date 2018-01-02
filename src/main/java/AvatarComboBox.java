import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvatarComboBox extends JPanel {
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<Integer> imageIndex = new ArrayList<>();
    private JComboBox avatarList;


    public AvatarComboBox() {
        avatarList = new JComboBox();
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(100, 100));
        avatarList.setRenderer(renderer);
        avatarList.setMaximumRowCount(3);
        SwingUtilities.invokeLater(() -> add(avatarList, BorderLayout.NORTH));

        String s = getClass().getResource("icons/avatars").toString().replace("file:/", "");
        File file = new File(s);
        new SwingWorker<Void, ArrayList<Integer>>() {
            @Override
            protected Void doInBackground() {
                BufferedImage img = null;
                int i = 0;
                for (File f : file.listFiles()) {
                    try {
                        img = ImageIO.read(f);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    images.add(img.getScaledInstance(100, 100, img.getType()));
                    imageIndex.add(i++);
                    publish(imageIndex);
                }
                return null;
            }

            @Override
            protected void process(List<ArrayList<Integer>> chunks) {
                for (ArrayList<Integer> c : chunks) {
                    Integer array[] = new Integer[c.size()];
                    array = c.toArray(array);
                    avatarList.setModel(new DefaultComboBoxModel(array));
                    revalidate();
                }
            }
        }.execute();
    }

    public Image getSelectedImage() {
        return images.get(avatarList.getSelectedIndex());
    }


    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }


        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            Integer selectedIndex = (Integer) value;

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (selectedIndex != null) {
                Image icon = images.get(selectedIndex);
                setIcon(new ImageIcon(icon));
            }
            return this;
        }
    }
}

