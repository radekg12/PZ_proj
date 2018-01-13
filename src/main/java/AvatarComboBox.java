import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class AvatarComboBox extends JPanel {
    private ArrayList<Image> images = new ArrayList<>();
    private ArrayList<String> imageIndex = new ArrayList<>();
    private ArrayList<String> imageIndex2 = new ArrayList<>();
    private JComboBox avatarList;
    private Color color, selectedColor;
    private TreeViewer treeViewer;
    private int avatarSize;


    public AvatarComboBox() {
        loadProperties();
        avatarList = new JComboBox();
        setOpaque(false);
        ComboBoxRenderer renderer = new ComboBoxRenderer();
        renderer.setPreferredSize(new Dimension(avatarSize, avatarSize));
        avatarList.setRenderer(renderer);
        avatarList.setMaximumRowCount(3);

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                treeViewer = new TreeViewer();
                return null;
            }
        }.execute();

        String s = getClass().getResource("icons/avatars").getPath();
        File file = new File(s);
        new SwingWorker<Void, ArrayList<String>>() {
            @Override
            protected Void doInBackground() {
                BufferedImage img;
                int i = 0;
                for (File f : file.listFiles()) {
                    try {

                        String a = f.getName();
                        String b = f.getPath();

                        img = ImageIO.read(f);

                        images.add(img.getScaledInstance(avatarSize, avatarSize, img.getType()));
                        imageIndex.add(a);
                        imageIndex2.add(a);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    publish(imageIndex);
                }
                return null;
            }

            @Override
            protected void process(List<ArrayList<String>> chunks) {
                for (ArrayList<String> c : chunks) {
                    String array[] = new String[c.size()];
                    array = c.toArray(array);
                    avatarList.setModel(new DefaultComboBoxModel(array));
                    revalidate();
                }
            }
        }.execute();
        avatarList.getEditor().getEditorComponent().setBackground(color);
        SwingUtilities.invokeLater(() -> add(avatarList));
    }

    public Image getSelectedImage() {
        return images.get(imageIndex.indexOf(imageIndex2.get(avatarList.getSelectedIndex())));
    }

    public String getSelectedImageName() {
        return imageIndex2.get(avatarList.getSelectedIndex());
    }

    public void filter(String s) {
        HashMap<String, String> hashMap = treeViewer.getAvatarsHashMap();
        imageIndex2.clear();
        imageIndex.stream().filter(x -> hashMap.get(x.split("\\.")[0]).equals(s)).forEach(i -> imageIndex2.add(i));
        String[] array11 = new String[imageIndex2.size()];
        array11 = imageIndex2.toArray(array11);
        avatarList.setModel(new DefaultComboBoxModel<>(array11));
    }

    private void loadProperties() {
        Properties properties = new Properties();
        InputStream input = null;

        try {
            input = Pionek.class.getResource("config.properties").openStream();
            properties.load(input);

            color = new Color(Integer.parseInt(properties.getProperty("background.dark"), 16));
            selectedColor = new Color(Integer.parseInt(properties.getProperty("background.bright"), 16));
            avatarSize = Integer.parseInt(properties.getProperty("avatar.size"));
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

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }


        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            //Integer selectedIndex = (Integer) value;
            Integer selectedIndex = null;
            if (value != null)
                selectedIndex = imageIndex.indexOf(value.toString());
            if (isSelected) {
                setBackground(selectedColor);
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(color);
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

