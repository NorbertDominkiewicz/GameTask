package app.gui.map;

import app.gui.GamePanel;

import java.awt.*;
import java.io.*;
import java.util.Random;


public class Map {
    public int[][] map;
    public Field[] fields;
    public int mapWidth;
    public int mapHeight;
    public int fieldSize;
    GamePanel gamePanel;

    public Map(int mapWidth, int mapHeight, int fieldSize, GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.fieldSize = fieldSize;

        map = new int[mapWidth][mapHeight];

        initFieldValues();
        generateMap();
        loadMap();
    }

    private void initFieldValues() {
        fields = new Field[2];
        fields[0] = new Field("transparentBlock.png", fieldSize);
        fields[1] = new Field("stepBlock.png", fieldSize);
        fields[1].setCollision(true);
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
        Random rand = new Random();
        StringBuilder pattern = new StringBuilder();
        int length = mapWidth*2;
        for(int i = 0; i < length; i++){
            if(i==length-1){
                break;
            }
            if(i%2!=0){
                pattern.append(" ");
            } else {
                pattern.append(rand.nextInt(2));
            }
        }
        return pattern.toString();
    }
    private void generateMap() {
        map = new int[mapWidth][mapHeight];
        try {
            FileWriter fw = new FileWriter("resources/maps/map.beethspace");
            for (int i = 0; i < mapHeight; i++) {
                if (i % 4 == 0) {
                    fw.write(generatePattern());
                }
                if (i % 4 != 0) {
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
        try (BufferedReader br = new BufferedReader(new FileReader("resources/maps/map.beethspace"))) {
            String line;
            int y = 0;
            while ((line = br.readLine()) != null && y < mapHeight) {
                String[] tokens = line.split(" ");
                for (int x = 0; x < tokens.length && x < mapWidth; x++) {
                    map[x][y] = Integer.parseInt(tokens[x]);
                }
                y++;
            }
        } catch (Exception e) {
            System.err.println("Błąd wczytywania mapy: " + e.getMessage());
            for (int y = 0; y < mapHeight; y++) {
                for (int x = 0; x < mapWidth; x++) {
                    map[x][y] = 0;
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
                g2d.drawImage(
                        fields[map[x][y]].getFieldImage(),
                        x * fieldSize - gamePanel.cameraX, // Przesunięcie X
                        y * fieldSize - gamePanel.cameraY, // NOWE: przesunięcie Y
                        fieldSize,
                        fieldSize,
                        null
                );
            }
        }
    }
}
