package nl.meh.mmo.client.view.frames;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.connection.socket.launcher.LauncherSocketCheck;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * This is the game launcher. It is started in the Main.class
 * The Launcher is the start point for the application, this frame holds all playable servers.
 * When selecting one -> LauncherFrame wil close and GameFrame will open
 */
public class LauncherFrame extends JFrame{

    public LauncherFrame(){
        super();
        Logger.log(Logger.level.INFO, "LauncherFrame has been constructed");

        setContentPane(new LauncherPanel());

        setResizable(false);
        pack();
        setVisible(true);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private class LauncherPanel extends JPanel implements Runnable, KeyListener {

        private Thread thread;

        private boolean running;
        private int fps = 60;
        private long targetTime = 1000/fps;

        private BufferedImage image;
        private Graphics2D g;

        private BufferedImage filler, sign, logo;

        private int currentChoice;
        private String[] servers = {
                "North America",
                "Europe West",
                "Europe Nordic & East",
                "Brazil",

                "Latin America North",
                "Latin America South",
                "Oceania",
                "Russia"
        };
        private boolean[] serverAviable = {
                false,false,
                false,false,
                false,false,
                false,false
        };

        private LauncherPanel () {
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
            Logger.log(Logger.level.INFO, "Initializing LauncherPanel");
            image = new BufferedImage(Main.WIDTH, Main.HEIGHT, BufferedImage.TYPE_INT_RGB);
            g = (Graphics2D) image.getGraphics();


            logo = ImageLoader.getInstance().getImage("/logo.png");
            filler =  ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_ONE);
            sign = ImageLoader.getInstance().getImage(ImageLoader.NO_ARROWED_SIGN);

            running = true;

            serverAviable[0] = LauncherSocketCheck.isServerAvailable("92.111.181.188",25565);
            serverAviable[1] = LauncherSocketCheck.isServerAvailable("92.108.159.52",25565);
            serverAviable[2] = LauncherSocketCheck.isServerAvailable("77.170.50.230",25565);
            serverAviable[3] = LauncherSocketCheck.isServerAvailable("143.179.47.135",25565);
            serverAviable[4] = LauncherSocketCheck.isServerAvailable("92.111.181.188",25566);
            serverAviable[5] = LauncherSocketCheck.isServerAvailable("92.108.159.52",25566);
            serverAviable[6] = LauncherSocketCheck.isServerAvailable("77.170.50.230",25566);
            serverAviable[7] = LauncherSocketCheck.isServerAvailable("127.0.0.1",25565);
        }

        private void update() {

        }

        private void draw() {
            //draw Background
            g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
            g.drawImage(filler, 0, 0, Main.WIDTH, Main.HEIGHT, null);

            g.drawImage(logo,Main.WIDTH/2-300, 50,null);

            g.setFont(Main.MAIN_FONT);

            int xxx = 0;
            for (int i = 0; i < servers.length; i++) {
                if (i == currentChoice && serverAviable[i]) {
                    g.setColor(new Color(0, 255,0));
                } else if (i == currentChoice && !serverAviable[i]) {
                    g.setColor(new Color(255, 255, 0));
                } else if (serverAviable[i]) {
                    g.setColor(new Color(0, 255, 255));
                } else {
                    g.setColor(new Color(255, 0,0));
                }
                if(i > 3) {
                    //g.drawImage(sign, (Main.WIDTH / 4) - 195, 260 + xxx * 50, 400, 48, null);
                    g.drawString(servers[i], StringCenter.center(servers[i], g) + Main.WIDTH / 4, 295 + xxx * 50);
                } else {
                    //g.drawImage(sign, ((Main.WIDTH / 4) + (Main.WIDTH / 2)) - 195, 260 + xxx * 50, 400, 48, null);
                    g.drawString(servers[i], StringCenter.center(servers[i], g) - Main.WIDTH / 4, 295 + xxx * 50);
                }
                if(xxx != 3) {
                    xxx++;
                } else {
                    xxx = 0;
                }

            }

            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(Color.WHITE);
            g.drawString(
                    Main.BUILD,
                    (int) ((Main.WIDTH - 10) - g.getFontMetrics().getStringBounds(Main.BUILD, g).getWidth()),
                    (Main.HEIGHT - 10)
            );
        }

        private void drawToScreen() {
            Graphics g2 = getGraphics();
            g2.drawImage(image, 0, 0, Main.WIDTH, Main.HEIGHT, null);
            g2.dispose();
        }

        @Override
        public void run() {
            init();

            long start;
            long elapsed;
            long wait;

            Logger.log(Logger.level.INFO, "Started running the launcher");
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

        @Override
        public void keyPressed(KeyEvent ke) {
            int k = ke.getKeyCode();
            if (k == KeyEvent.VK_ENTER) {
                select();
            }

            if (k == KeyEvent.VK_UP) {
                currentChoice--;
                if (currentChoice == -1) {
                    currentChoice = servers.length - 1;
                }
            }
            if (k == KeyEvent.VK_DOWN) {
                currentChoice++;
                if (currentChoice == servers.length) {
                    currentChoice = 0;
                }
            }
        }

        private void select(){
            switch (currentChoice) {
                case 0:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 1:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 2:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 3:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 4:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 5:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 6:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
                case 7:
                    if(serverAviable[currentChoice]) startNewFrame("127.0.0.1", 25565);
                    break;
            }
        }

        private void startNewFrame(String ip, int port){
            GameServerSocket.createInstance(ip, port);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.dispose();
            new GameFrame();
        }

        public void keyTyped(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
    }
}
