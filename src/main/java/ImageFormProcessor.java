import javax.swing.*;
import java.awt.*;

public class ImageFormProcessor {

    private final JFrame frame;
    private String imgPath;

    public ImageFormProcessor() {

        this.frame = new JFrame();
    }

    public void load(String imgPath) {

        this.imgPath = imgPath;

        this.frame.setLocationRelativeTo(null);
        this.frame.setTitle("Meme");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);

        ImageIcon imageIcon = new ImageIcon(this.imgPath);
        this.frame.setMinimumSize(new Dimension(imageIcon.getIconWidth() + 150, imageIcon.getIconHeight() + 150));

        this.frame.add(new JLabel(imageIcon));
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }
}
