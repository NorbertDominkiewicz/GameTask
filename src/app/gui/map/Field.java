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
    public Field(String imageName, int x, int y, int size, boolean collision){
        this.size = size;
        hitbox = new Rectangle(x, y, size, size);
        this.collision = collision;
        try{
            if (imageName.equals("blackStone.jpg")){
                type = 1;
            } else{
                type = 0;
            }
            fieldImage = ImageIO.read(getClass().getResource("/tiles/" + imageName));
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public Rectangle getHitbox(int x, int y) {
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }

    @Override
    public String toString(){
        return "(" + hitbox.x + "," + hitbox.y +")";
    }

    public void draw(Graphics2D g2d){
        g2d.drawRect(hitbox.x,hitbox.y,hitbox.width,hitbox.height);
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
