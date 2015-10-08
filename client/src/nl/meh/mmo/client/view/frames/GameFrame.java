package nl.meh.mmo.client.view.frames;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.GameStateController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class GameFrame extends JFrame{
    public GameFrame() {
        super();
        Logger.log(Logger.level.INFO, "GameFrame has been constructed");

        setContentPane(new GamePanel());

        setTitle(Main.TITLE);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.log(Logger.level.INFO, "game being closed");
                try {
                    GameServerSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                    GameServerSocket.getInstance().getServer().close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });

        setResizable(false);
        pack();
        setVisible(true);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

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

            setPreferredSize(new Dimension(Main.WIDTH, Main.HEIGHT));
            setMaximumSize(new Dimension(Main.WIDTH, Main.HEIGHT));
            setMinimumSize(new Dimension(Main.WIDTH, Main.HEIGHT));

            setFocusable(true);

            setSize(Main.WIDTH, Main.HEIGHT);
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
            image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
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
            g2.drawImage(image, 0, 0, Main.WIDTH, Main.HEIGHT, null);
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
}
