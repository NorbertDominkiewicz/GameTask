package app.gui;

import java.awt.event.*;

public class JoyStick implements KeyListener{
    public boolean keyAPressed, keyDPressed, keyWPressed;

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) keyAPressed = true;
        if (key == KeyEvent.VK_D) keyDPressed = true;
        if (key == KeyEvent.VK_W) keyWPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) keyAPressed = false;
        if (key == KeyEvent.VK_D) keyDPressed = false;
        if (key == KeyEvent.VK_W) keyWPressed = false;
    }

    @Override public void keyTyped(KeyEvent e) {}

}