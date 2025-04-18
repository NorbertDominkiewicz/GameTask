package app.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game extends JFrame {
    GamePanel gamePanel;
    public Game(String folder, int maxScore) {
        setTitle("Beethspace");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Obtain User Window Resolutions
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        // Force Game to be played in full screen
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(this);

        // Setting up blank cursor, we don't need it in the game
        BufferedImage cursorImage = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0, 0), "blank cursor");
        getContentPane().setCursor(blankCursor);

        // init gamePanel and we use pack() so all screen dimensions will rely on gamePanel's dims
        gamePanel = new GamePanel(screenWidth, screenHeight,gd,folder,maxScore);
        add(gamePanel);
        pack();
        setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}
