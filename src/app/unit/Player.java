package app.unit;

import app.gui.GamePanel;
import app.gui.JoyStick;

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
    public float jumpPower = 25f;
    public float gravity = 0.6f;
    public float jumpVelocity = 0f;
    public int playerWidth;
    public int playerHeight;
    public boolean isJumping = false;

    private String direction;
    private BufferedImage leftImage;
    private BufferedImage rightImage;

    GamePanel gamePanel;
    JoyStick joyStick;

    public Player(GamePanel gamePanel, JoyStick joyStick) {
        this.gamePanel = gamePanel;
        groundLevel = (this.gamePanel.map.mapHeight - 4) * this.gamePanel.TILE_SIZE;
        speedRate = 7;
        this.joyStick = joyStick;
        this.initImages();
        this.initDefaults();
        this.initUnitArea();
    }

    private void initDefaults(){
        worldX = gamePanel.TILE_SIZE * 10;
        groundLevel = (gamePanel.map.mapHeight - 4) * gamePanel.map.fieldSize;
        worldY = groundLevel;
        screenX = gamePanel.SCREEN_WIDTH / 2 - gamePanel.TILE_SIZE / 2;
        screenY = gamePanel.SCREEN_HEIGHT - gamePanel.TILE_SIZE * 4;
        jumpRate = 25;
        direction = "left";
        playerWidth = gamePanel.TILE_SIZE;
        playerHeight = gamePanel.TILE_SIZE * 2;
        inAir = false;
        isStanding = true;
    }

    private void initUnitArea(){
        unitArea = new Rectangle(worldX, worldY, playerWidth, playerHeight);
    }

    private void initImages(){
        try{
            leftImage = ImageIO.read(new File("resources/units/player/beethovenLeft.png"));
            rightImage = ImageIO.read(new File("resources/units/player/beethovenRight.png"));
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

        if (gamePanel.map.fields[gamePanel.map.map[leftTile][bottomTile]].isCollision()
                || gamePanel.map.fields[gamePanel.map.map[rightTile][bottomTile]].isCollision()) {
            return "bottom"; // Kolizja od dołu
        } else if (gamePanel.map.fields[gamePanel.map.map[leftTile][topTile]].isCollision()
                || gamePanel.map.fields[gamePanel.map.map[rightTile][topTile]].isCollision()) {
            return "top"; // Kolizja od góry
        } else if (gamePanel.map.fields[gamePanel.map.map[leftTile][topTile]].isCollision()
                || gamePanel.map.fields[gamePanel.map.map[leftTile][bottomTile]].isCollision()) {
            return "left"; // Kolizja z lewej strony
        } else if (gamePanel.map.fields[gamePanel.map.map[rightTile][topTile]].isCollision()
                || gamePanel.map.fields[gamePanel.map.map[rightTile][bottomTile]].isCollision()) {
            return "right"; // Kolizja z prawej strony
        }
        return "none"; // Brak kolizji
    }

    public void update() {
        int previousWorldX = worldX;
        int previousWorldY = worldY;

        // Ruch boczny
        if (joyStick.keyDPressed) {
            setDirection("right");
            worldX += speedRate;
            if (detectCollisionWithMap().equals("right")) {
                worldX = previousWorldX;
            }
        } else if (joyStick.keyAPressed) {
            setDirection("left");
            worldX -= speedRate;
            if (detectCollisionWithMap().equals("left")) {
                worldX = previousWorldX;
            }
        }

        // Rozpoczęcie skoku
        if (joyStick.keyWPressed && !inAir && isStanding) {
            isJumping = true;
            inAir = true;
            isStanding = false;
            setDirection("up");
            jumpVelocity = -jumpPower;
        }

        // Skok i grawitacja
        if (inAir) {
            jumpVelocity += gravity;
            worldY += (int) jumpVelocity;

            if (detectCollisionWithMap().equals("top")) {
                // Zatrzymujemy ruch w górę
                jumpVelocity = 0; // Natychmiast zatrzymujemy skok
                inAir = true;     // Gracz pozostaje w powietrzu, zaczyna opadać
                isStanding = false; // Nie stoi na żadnym bloczku
                isJumping = false; // Skok zakończony
            }

            // Kolizja od dołu (lądowanie)
            if (detectCollisionWithMap().equals("bottom")) {
                // Precyzyjne ustawienie na bloczku
                int bloczekY = (worldY / gamePanel.TILE_SIZE) * gamePanel.TILE_SIZE; // Wyliczenie dolnej krawędzi bloczka
                worldY = bloczekY - playerHeight; // Ustawienie gracza na bloczku

                // Synchronizacja stanu gracza
                jumpVelocity = 0;  // Zresetuj prędkość pionową
                inAir = false;     // Gracz nie jest w powietrzu
                isStanding = true; // Gracz stoi na bloczku
            } else {
                // Jeśli brak kolizji, kontynuuj grawitację
                inAir = true;
                jumpVelocity += gravity;
                worldY += (int) jumpVelocity;
            }
        }

        // Grawitacja, gdy gracz nie stoi
        if (!isStanding) {
            jumpVelocity += gravity;
            worldY += (int) jumpVelocity;
        }

        updateUnitArea();
    }


    public void updateUnitArea() {
        unitArea.x = worldX - gamePanel.cameraX; // Używamy worldX z przesunięciem kamery
        unitArea.y = worldY - gamePanel.cameraY;
        unitArea.width = playerWidth;
        unitArea.height = playerHeight;
    }

    public void setDirection(String direction){
        this.direction = direction;
    }

    String getDirection(){
        return direction;
    }

    public void draw(Graphics2D g){
        int renderX = gamePanel.SCREEN_WIDTH / 2 - playerWidth / 2;
        int renderY = gamePanel.PLAYER_SCREEN_Y - playerHeight;

        switch(direction){
            case "left":
                g.drawImage(leftImage, unitArea.x, unitArea.y, playerWidth,playerHeight,null);
                break;
            case "right":
                g.drawImage(rightImage, unitArea.x, unitArea.y, playerWidth,playerHeight,null);
                break;
            default:
                g.drawImage(leftImage, unitArea.x, unitArea.y, playerWidth,playerHeight,null);
                break;
        }
    }
}
