package app.gui;

import app.gui.map.Map;
import app.unit.Piano;
import app.unit.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

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
    public boolean gameWon = false;
    //
    public int PLAYER_SCREEN_Y;
    public int cameraX = 0;
    public int cameraY = 0;
    final double smooth = 0.1;
    //
    private Image BACKGROUND;
    private Thread gameThread;
    public JoyStick joyStick;
    public Player player;
    public Map map;
    private Random rand = new Random();
    private ArrayList<Piano> pianos;
    private GraphicsDevice gd;
    //public PianoGenerator pianoGenerator;

    public GamePanel(int screenWidth, int screenHeight, GraphicsDevice gd) {
        SCREEN_WIDTH = screenWidth;
        SCREEN_HEIGHT = screenHeight;
        MAX_SCREEN_COLUMNS = 16;
        MAX_SCREEN_ROWS = 9;
        MAX_WORLD_COLUMNS = 16;
        MAX_WORLD_ROWS = 40;
        FPS = 60;
        PLAYER_SCREEN_Y = SCREEN_HEIGHT * 3 / 4;
        TILE_SIZE = screenWidth / MAX_SCREEN_COLUMNS;
        this.initBackground();
        this.joyStick = new JoyStick();
        this.map = new Map(MAX_WORLD_COLUMNS, MAX_WORLD_ROWS, TILE_SIZE,this);
        this.player = new Player(this, joyStick);
        //this.pianoGenerator = new PianoGenerator(this);
        this.pianos = new ArrayList<>();
        this.gd = gd;

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
            BACKGROUND = ImageIO.read(GamePanel.class.getResource("/gamePanel/bg.jpg"));
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
    private void updateCamera() {

        int targetCameraX = player.worldX - SCREEN_WIDTH / 2;
        int targetCameraY = player.worldY - PLAYER_SCREEN_Y / 2 - 200;

        targetCameraX = Math.max(0, Math.min(targetCameraX, map.mapWidth * map.fieldSize - SCREEN_WIDTH));
        targetCameraY = Math.max(0, Math.min(targetCameraY, map.mapHeight * map.fieldSize - SCREEN_HEIGHT));

        cameraX += (targetCameraX - cameraX) * smooth;
        cameraY += (targetCameraY - cameraY) * smooth;
    }

    private void update(double delta) {
        if(player.worldY <= TILE_SIZE/2){
            gameWon = true;
            stopGameThread();
            new Victory(SCREEN_WIDTH/4,SCREEN_HEIGHT/4);
        }
        player.update();
        for (Piano piano : pianos) {
            piano.update();
        }

        for (Piano piano : pianos) {
            piano.update();
        }

        pianos.removeIf(piano -> piano.getY() > SCREEN_HEIGHT);

        if (pianos.size() < 2) {
            int screenX = (int) (Math.random() * SCREEN_WIDTH);
            boolean canAdd = true;
            for (Piano piano : pianos) {
                if (Math.abs(screenX - piano.getCollisionArea().x) < SCREEN_WIDTH - TILE_SIZE * 2) {
                    canAdd = false;
                    break;
                }
            }

            if (canAdd) {
                pianos.add(new Piano(screenX, -1 * (rand.nextInt(300)), TILE_SIZE * 2, TILE_SIZE * 2));
            }
        }

        for (Piano piano : pianos) {
            if (player.checkCollisionWithPiano(piano) && !gameWon) {
                stopGameThread();
                new GameOver(SCREEN_WIDTH/4, SCREEN_HEIGHT/4);
                break;
            }
        }
        updateCamera();
    }

    public void stopGameThread() {
        gameThread = null;
        gameRunning = false;
        gd.setFullScreenWindow(null);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(BACKGROUND, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        map.draw(g2d);

        player.draw(g2d);
        for (Piano piano : pianos) {
            piano.draw(g2d);
        }
    }
}
