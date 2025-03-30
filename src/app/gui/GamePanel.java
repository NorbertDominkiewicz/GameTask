package app.gui;

import app.gui.map.Map;
import app.unit.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

public class GamePanel extends JPanel implements Runnable {
    // primitives
    public int SCREEN_WIDTH;
    public int SCREEN_HEIGHT;
    public int TILE_SIZE;
    public int MAX_SCREEN_COLUMNS;
    public int MAX_SCREEN_ROWS;
    public int MAX_WORLD_COLUMNS;
    public int MAX_WORLD_ROWS;
    public int FPS;
    public boolean gameRunning = false;
    //
    public int PLAYER_SCREEN_Y;
    public int cameraX = 0;
    public int cameraY = 0;
    private final double CAMERA_SMOOTHING = 0.1;
    //
    private Image BACKGROUND;
    private Thread gameThread;
    public JoyStick joyStick;
    public Player player;
    public Map map;

    public GamePanel(int screenWidth, int screenHeight) {
        SCREEN_WIDTH = screenWidth;
        SCREEN_HEIGHT = screenHeight;
        MAX_SCREEN_COLUMNS = 16;
        MAX_SCREEN_ROWS = 9;
        MAX_WORLD_COLUMNS = 30;
        MAX_WORLD_ROWS = 50;
        FPS = 60;
        PLAYER_SCREEN_Y = SCREEN_HEIGHT * 3 / 4;
        TILE_SIZE = screenWidth / MAX_SCREEN_COLUMNS;
        this.initBackground();
        this.joyStick = new JoyStick();
        this.map = new Map(MAX_WORLD_COLUMNS, MAX_WORLD_ROWS, TILE_SIZE,this);
        this.player = new Player(this, joyStick);

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(joyStick);
        setFocusable(true);
        setDoubleBuffered(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        startGameThread();
    }

    private void initBackground(){
        try{
            BACKGROUND = ImageIO.read(new File("resources/gamePanel/bg.jpg"));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        gameRunning = true;
    }

    @Override
    public void run() {
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                update(delta);
                repaint();
                delta--;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
//    private void updateCamera() {
//        int targetCameraY = player.worldY - gamePanel.PLAYER_SCREEN_Y;
//
//        targetCameraY = Math.max(0, Math.min(targetCameraY, map.mapHeight * map.fieldSize - SCREEN_HEIGHT));
//
//        // Płynne przesunięcie kamery
//        cameraY += (targetCameraY - cameraY) * CAMERA_SMOOTHING;
//    }

    private void updateCamera() {

        int targetCameraX = player.worldX - SCREEN_WIDTH / 2;
        int targetCameraY = player.worldY - PLAYER_SCREEN_Y;

        targetCameraX = Math.max(0, Math.min(targetCameraX, map.mapWidth * map.fieldSize - SCREEN_WIDTH));
        targetCameraY = Math.max(0, Math.min(targetCameraY, map.mapHeight * map.fieldSize - SCREEN_HEIGHT));

        // Stopniowe przesunięcie kamery
        cameraX += (targetCameraX - cameraX) * CAMERA_SMOOTHING;
        cameraY += (targetCameraY - cameraY) * CAMERA_SMOOTHING;
    }

    private void update(double delta) {
        player.update();
        updateCamera();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(BACKGROUND, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        map.draw(g2d);

        int startX = cameraX / TILE_SIZE;
        int startY = cameraY / TILE_SIZE;
        int endX = Math.min(map.mapWidth, startX + (SCREEN_WIDTH / TILE_SIZE) + 1);
        int endY = Math.min(map.mapHeight, startY + (SCREEN_HEIGHT / TILE_SIZE) + 1);

        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++) {
                if (map.fields[map.map[x][y]].isCollision()) {
                    Rectangle tileHitbox = map.fields[map.map[x][y]].getHitbox(
                            x * TILE_SIZE - cameraX,
                            y * TILE_SIZE - cameraY
                    );
                    g2d.setColor(Color.RED); // Kolor hitboxów kafelków
                    g2d.drawRect(tileHitbox.x, tileHitbox.y, tileHitbox.width, tileHitbox.height);
                }
            }
        }

        g2d.setColor(Color.BLUE);
        g2d.drawRect(player.unitArea.x, player.unitArea.y, player.unitArea.width, player.unitArea.height);

        player.draw(g2d);
    }
}
