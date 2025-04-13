package app.unit;

import app.gui.GamePanel;
import app.gui.JoyStick;
import app.gui.Victory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player extends Unit{
    public boolean inAir;
    public boolean isStanding;
    public int screenX;
    public int screenY;
    public int jumpRate;
    public int groundLevel;
    public float jumpPower = 35f;
    public float gravity = 0.8f;
    public float jumpVelocity = 0f;
    public int playerWidth;
    public int playerHeight;
    public boolean isJumping = false;
    public boolean unKillable = true;
    public int playerScore = 0;

    private String direction;
    private BufferedImage leftImage;
    private BufferedImage rightImage;

    GamePanel gamePanel;
    JoyStick joyStick;

    String folder;

    public Player(GamePanel gamePanel, JoyStick joyStick, String folder) {
        this.gamePanel = gamePanel;
        this.folder = folder;
        groundLevel = (this.gamePanel.map.mapHeight - 4) * this.gamePanel.TILE_SIZE;
        speedRate = 17;
        this.joyStick = joyStick;
        this.initImages();
        this.initDefaults();
        this.initUnitArea();
        this.setKillable();
    }

    private void setKillable(){
        new Thread(() -> {
            try{
                Thread.sleep(8000);
                this.unKillable = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void initDefaults(){
        worldX = gamePanel.TILE_SIZE * 10;
        groundLevel = (gamePanel.map.mapHeight - 3) * gamePanel.map.fieldSize;
        worldY = groundLevel;
        screenX = gamePanel.SCREEN_WIDTH / 2 - gamePanel.TILE_SIZE / 2;
        screenY = gamePanel.SCREEN_HEIGHT - gamePanel.TILE_SIZE * 4;
        jumpRate = 25;
        direction = "left";
        playerWidth = gamePanel.TILE_SIZE;
        playerHeight = gamePanel.TILE_SIZE *2;
        inAir = false;
        isStanding = true;
    }

    private void initUnitArea(){
        unitArea = new Rectangle(worldX, worldY, playerWidth/2, playerHeight*2);
    }

    private void initImages(){
        try{
            leftImage = ImageIO.read(getClass().getResource("/units/" + folder + "/left.png"));
            rightImage = ImageIO.read(getClass().getResource("/units/" + folder + "/right.png"));
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public String detectCollisionWithMap() {
        int leftWorldX = worldX;
        int rightWorldX = worldX + playerWidth;
        int topWorldY = worldY;
        int bottomWorldY = worldY + playerHeight;

        int leftTile = leftWorldX / gamePanel.TILE_SIZE;
        int rightTile = rightWorldX / gamePanel.TILE_SIZE;
        int topTile = topWorldY / gamePanel.TILE_SIZE;
        int bottomTile = bottomWorldY / gamePanel.TILE_SIZE;

        if (gamePanel.map.map[leftTile][bottomTile].isCollision() || gamePanel.map.map[rightTile][bottomTile].isCollision()) {
            System.out.println("bottom kolizja");
            return "bottom";
        } else if (gamePanel.map.map[leftTile][topTile].isCollision() || gamePanel.map.map[rightTile][topTile].isCollision()) {
            System.out.println("top kolizja");
            return "top";
        } else if (gamePanel.map.map[leftTile][topTile].isCollision() || gamePanel.map.map[leftTile][bottomTile].isCollision()) {
            System.out.println("left kolizja");
            return "left";
        } else if (gamePanel.map.map[rightTile][topTile].isCollision() || gamePanel.map.map[rightTile][bottomTile].isCollision()) {
            System.out.println("right kolizja");
            return "right";
        }
        return "none";
    }

    public void update() {
        int previousWorldX = worldX;
        int previousWorldY = worldY;

        if (joyStick.keyDPressed) {
            setDirection("right");
            if (worldX + playerWidth < gamePanel.MAX_WORLD_COLUMNS * gamePanel.TILE_SIZE) {
                worldX += speedRate;
            }
            if (detectCollisionWithMap().equals("bottom")) {
                worldY = previousWorldY;
                jumpVelocity = 0;
                inAir = false;
                isStanding = true;
            } else {
                inAir = true;
                jumpVelocity += gravity;
                worldY += (int) jumpVelocity;
            }
            if (detectCollisionWithMap().equals("right")) {
                worldX = previousWorldX;
            }
        } else if (joyStick.keyAPressed) {
            setDirection("left");
            if(worldX > 0){
                worldX -= speedRate;
            }
            if (detectCollisionWithMap().equals("left")) {
                worldX = previousWorldX;
            }
            if (detectCollisionWithMap().equals("bottom")) {
                worldY = previousWorldY;
                jumpVelocity = 0;
                inAir = false;
                isStanding = true;
            } else {
                inAir = true;
                jumpVelocity += gravity;
                worldY += (int) jumpVelocity;
            }
        }
        if (joyStick.keyWPressed && !inAir && isStanding) {
            isJumping = true;
            inAir = true;
            isStanding = false;
            setDirection("up");
            jumpVelocity = -jumpPower;
        }

        if (inAir) {
            jumpVelocity += gravity;
            worldY += (int) jumpVelocity;
            if (detectCollisionWithMap().equals("top")) {
                jumpVelocity = 0;
                inAir = true;
                isStanding = false;
                worldY = previousWorldY;
                isJumping = false;
            }
            if (detectCollisionWithMap().equals("bottom")) {
                worldY = previousWorldY - 10;
                jumpVelocity = 0;
                inAir = false;
                isStanding = true;
            } else {

                inAir = true;
                jumpVelocity += gravity;
                worldY += (int) jumpVelocity;
            }
        }
        if (!isStanding) {
            jumpVelocity += gravity;
            worldY += (int) jumpVelocity;
        }
        updateUnitArea();
        System.out.println("World: " + worldX + ", " + worldY);
        updateScore();
    }

    public boolean checkCollisionWithPiano(Piano piano) {
        if(unKillable){
            return false;
        } else {
            return unitArea.intersects(piano.getCollisionArea());
        }
    }

    public void updateUnitArea() {
        unitArea.x = worldX - gamePanel.cameraX;
        unitArea.y = worldY - gamePanel.cameraY;
        unitArea.width = playerWidth;
        unitArea.height = playerHeight;
    }

    public void updateScore() {
        int score = (int) Math.sqrt((gamePanel.maxScore * gamePanel.TILE_SIZE - worldY));
        playerScore = score;
    }


    public void setDirection(String direction){
        this.direction = direction;
    }

    public void draw(Graphics2D g){

        switch(direction){
            case "left":
                g.drawImage(leftImage, unitArea.x, unitArea.y, unitArea.width,unitArea.height,null);
                break;
            case "right":
                g.drawImage(rightImage, unitArea.x, unitArea.y, unitArea.width,unitArea.height,null);
                break;
            default:
                g.drawImage(leftImage, unitArea.x, unitArea.y, unitArea.width,unitArea.height,null);
                break;
        }
    }
}
