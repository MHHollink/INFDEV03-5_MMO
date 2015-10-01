package nl.marcelhollink.mmorpg.frontend.main.view;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.GameState;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel  implements Runnable, KeyListener {

    // game thread
    private Thread thread;
    private boolean running;
    private int FPS = 60;
    private long targetTime = 1000/FPS;

    // image
    private BufferedImage image;
    private Graphics2D g;

    public GamePanel() {
        super();
        Logger.log(Logger.level.INFO, "GamePanel has been constructed");

        setPreferredSize(new Dimension(UI.WIDTH, UI.HEIGHT));
        setMaximumSize(new Dimension(UI.WIDTH, UI.HEIGHT));
        setMinimumSize(new Dimension(UI.WIDTH, UI.HEIGHT));

        setFocusable(true);

        setSize(UI.WIDTH, UI.HEIGHT);
    }

    public void addNotify() {
        super.addNotify();
        if(thread == null){
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init() {
        Logger.log(Logger.level.INFO, "Initializing GamePanel");
        image = new BufferedImage(UI.WIDTH, UI.HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();

        running = true;
    }

    public void run() {
        init();

        long start;
        long elapsed;
        long wait;

        Logger.log(Logger.level.INFO, "Started running the gameLoop");
        while(running){
            start = System.nanoTime();

            update();
            draw();
            drawToScreen();

            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            if( wait < 0) { wait = 5; }

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        GameStateController.getInstance().update();
    }

    private void draw() {
        GameStateController.getInstance().draw(g);
    }

    private void drawToScreen() {
        Graphics g2 = getGraphics();
        g2.drawImage(image, 0, 0, UI.WIDTH, UI.HEIGHT, null);
        g2.dispose();
    }

    public void keyTyped(KeyEvent key) { }
    public void keyPressed(KeyEvent key) {
        GameStateController.getInstance().keyPressed(key.getKeyCode());
    }
    public void keyReleased(KeyEvent key) {
        GameStateController.getInstance().keyReleased(key.getKeyCode());
    }
}
