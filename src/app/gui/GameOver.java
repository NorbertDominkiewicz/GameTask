package app.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class OverPanel extends JPanel {
    int width;
    int height;
    BufferedImage image;
    OverPanel(int width, int height) {
        this.width = width;
        this.height = height;
        this.setBackground(Color.BLACK);
        try{
            image = ImageIO.read(GameOver.class.getResource("/gamePanel/gameover.jpg"));
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

public class GameOver extends JFrame {
    public GameOver(int width, int height) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Game Over");
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.add(new OverPanel(width,height), BorderLayout.CENTER);
        this.setVisible(true);
    }
}
