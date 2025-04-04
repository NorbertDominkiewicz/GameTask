package app.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class VictoryPanel extends JPanel {
    int width;
    int height;
    BufferedImage image;
    VictoryPanel(int width, int height) {
        this.width = width;
        this.height = height;
        this.setBackground(Color.BLACK);
        try{
            image = ImageIO.read(getClass().getResource("/gamePanel/victory.jpg"));
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0,width,height, null);
    }
}

public class Victory extends JFrame {
    public Victory(int width, int height) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Wygrana!!!");
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(new VictoryPanel(width,height), BorderLayout.CENTER);
        this.setVisible(true);
    }
}
