package app.gui;

import javax.swing.*;
import java.awt.*;

class TitleLabel extends JLabel {
    public TitleLabel() {
        setOpaque(false);
        setLayout(new FlowLayout(FlowLayout.CENTER));
        ImageIcon img = new ImageIcon(getClass().getResource("gamePanel/label.png"));
        setIcon(img);
    }
}

public class Settings extends JPanel {
    TitleLabel tl = new TitleLabel();
    public Settings() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
        add(tl);
    }
}
