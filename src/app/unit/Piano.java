package app.unit;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piano {
    private int x, y, width, height;
    private int speed = 6;
    private BufferedImage image;
    private Rectangle collisionArea;

    public Piano(int x, int y, int width, int height) {
        this.loadImage();
        this.x = x;
        this.y = y;
        this.width = width - width/4;
        this.height = height - height/4;
        this.collisionArea = new Rectangle(x, y, width, height);
    }

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResource("/tiles/piano.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        y += speed;
        updateCollisionArea();
    }

    private void updateCollisionArea() {
        collisionArea.setBounds(x, y-40, width, height-20);
    }

    public Rectangle getCollisionArea() {
        return collisionArea;
    }

    public void draw(Graphics2D g) {
        g.drawImage(image, x, y, width, height, null);
        g.setColor(Color.YELLOW);
        g.drawRect(collisionArea.x, collisionArea.y, collisionArea.width, collisionArea.height);
    }

    public int getY() {
        return y;
    }
}

