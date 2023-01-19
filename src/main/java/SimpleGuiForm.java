import com.intellij.ui.JBColor;
import com.intellij.util.xmlb.annotations.Property;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleGuiForm {

    private final JFrame frame;
    private AtomicReference<String> memePath = null;

    public SimpleGuiForm(MemeProcessor memeProcessor) {
        this.frame = new JFrame();
        JButton refreshButton = new JButton("Refresh memes");
        JButton generateButton = new JButton("Generate Meme");

        frame.setSize(400, 300);
        frame.setTitle("Meme");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setMinimumSize(new Dimension(400, 300));

        JPanel panel = new JPanel();
        panel.setSize(70, 30);
        panel.add(generateButton);

        refreshButton.addActionListener(e -> {
            memeProcessor.getTopMemes();
        });
        frame.add(refreshButton);

        JTextPane templateIdPane = new JTextPane();
        templateIdPane.setBackground(JBColor.LIGHT_GRAY);
        JTextPane text0Pane = new JTextPane();
        text0Pane.setBackground(JBColor.LIGHT_GRAY);
        JTextPane text1Pane = new JTextPane();
        text1Pane.setBackground(JBColor.LIGHT_GRAY);

        frame.add(templateIdPane);
        frame.add(text0Pane);
        frame.add(text1Pane);

        generateButton.addActionListener(e -> {

            String templateId = templateIdPane.getText();
            String text0 = text0Pane.getText();
            String text1 = text1Pane.getText();

            this.memePath = new AtomicReference<>(memeProcessor.generateMeme(templateId, text0, text1));

            frame.setVisible(false);
        });
        frame.add(generateButton);

        GridLayout experimentLayout = new GridLayout(5, 1, 3, 3);
        frame.setLayout(experimentLayout);

    }

    public String getMemePath() {
        while (this.memePath == null) {

        }

        return this.memePath.get();
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }
}
