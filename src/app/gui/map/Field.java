package app.gui.map;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Field {
    BufferedImage fieldImage;
    private final int size;
    private boolean collision;
    private int type;
    public Rectangle hitbox;
    public Field(String imageName, int size){
        this.size = size;
        hitbox = new Rectangle(0, 0, size, size);
        this.collision = false;
        try{
            if (imageName.equals("stepBlock.png")){
                type = 1;
            } else{
                type = 0;
            }
            fieldImage = ImageIO.read(new File("resources/tiles/" + imageName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public Rectangle getHitbox(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }

    public int getType(){
        return type;
    }

    public int getSize() {
        return size;
    }

    public BufferedImage getFieldImage(){
        return fieldImage;
    }

    public boolean isCollision(){
        return collision;
    }

    public void setCollision(boolean collision){
        this.collision = collision;
    }
}
