package app.gui.map;

import app.gui.GamePanel;

import java.awt.*;
import java.io.*;
import java.util.Random;


public class Map {
    public Field[][] map;
    public int mapWidth;
    public int mapHeight;
    public int fieldSize;
    GamePanel gamePanel;

    public Map(int mapWidth, int mapHeight, int fieldSize, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.fieldSize = fieldSize;

        map = new Field[mapWidth][mapHeight];

        generateMap();
        loadMap();
    }

    private String generateFullPattern(){
        StringBuilder pattern = new StringBuilder();
        int length = mapWidth*2;
        for(int i = 0; i < length; i++){
            if(i==length-1){
                break;
            }
            if(i%2!=0){
                pattern.append(" ");
            } else {
                pattern.append(1);
            }
        }
        return pattern.toString();
    }

    private String generateEmptyPattern(){
        StringBuilder pattern = new StringBuilder();
        int length = mapWidth*2;
        for(int i = 0; i < length; i++){
            if(i==length-1){
                break;
            }
            if(i%2!=0){
                pattern.append(" ");
            } else {
                pattern.append(0);
            }
        }
        return pattern.toString();
    }
    private String generatePattern(){
        int counter = 0;
        Random rand = new Random();
        StringBuilder pattern = new StringBuilder();
        int length = mapWidth * 2;

        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                break;
            }

            if (i % 2 != 0) {
                pattern.append(" ");
            } else {
                int j;
                if (counter >= 8) {
                    j = 0;
                } else {
                    j = rand.nextInt(2);
                }
                if (j == 1) {
                    int jedynki = rand.nextInt(2) + 2;
                    pattern.append(1);
                    counter++;
                    for (int k = 1; k < jedynki; k++) {
                        if (i + 2 < length) {
                            pattern.append(" ");
                            pattern.append(1);
                            counter++;
                        }
                    }
                    i += (jedynki - 1) * 2;
                } else {
                    pattern.append(0);
                }
            }
        }
        return pattern.toString();
    }

    private void generateMap() {
        map = new Field[mapWidth][mapHeight];
        try {
            FileWriter fw = new FileWriter("map.beethspace");
            for (int i = 0; i < mapHeight; i++) {
                if (i == mapHeight - 1) {
                    fw.write(generateFullPattern());
                    break;
                }
                if (i % 5 == 0) {
                    fw.write(generatePattern());
                }
                if (i % 5 != 0) {
                    fw.write(generateEmptyPattern());
                }
                fw.write("\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void loadMap() {
        try (BufferedReader br = new BufferedReader(new FileReader("map.beethspace"))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null && y < mapHeight) {
                String[] tokens = line.split(" ");
                for (int x = 0; x < tokens.length && x < mapWidth; x++) {
                    if(Integer.parseInt(tokens[x]) == 1){
                        map[x][y] = new Field("blackStone.jpg", x, y, fieldSize, true);
                    } else if (Integer.parseInt(tokens[x]) == 0){
                        map[x][y] = new Field("transparentBlock.png", x ,y, fieldSize, false);
                    }
                }
                y++;
            }
        } catch (Exception e) {
            System.err.println("Błąd wczytywania mapy: " + e.getMessage());
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    map[x][y] = new Field("transparentBlock.png", x ,y, fieldSize, false);
                }
            }
        }
    }
    public void update() {

    }
    public void draw(Graphics2D g2d) {
        int startX = gamePanel.cameraX / fieldSize;
        int startY = gamePanel.cameraY / fieldSize;
        int endX = Math.min(mapWidth, startX + (gamePanel.SCREEN_WIDTH / fieldSize) + 1);
        int endY = Math.min(mapHeight, startY + (gamePanel.SCREEN_HEIGHT / fieldSize) + 1);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                g2d.drawImage(map[x][y].getFieldImage(), map[x][y].hitbox.x * fieldSize - gamePanel.cameraX, map[x][y].hitbox.y * fieldSize - gamePanel.cameraY, fieldSize, fieldSize, null);
            }
        }
    }
}
