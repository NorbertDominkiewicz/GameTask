package app.unit;

import java.awt.*;

abstract public class Unit {
    public int speedRate;
    public int worldX;
    public int worldY;
    private boolean collision;
    public Rectangle unitArea;

    public boolean hasCollision() {
        return collision;
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
