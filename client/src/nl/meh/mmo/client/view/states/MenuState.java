package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 *     The MenuState is the state holding the Menu and Splash Screen
 *
 *     When the MenuState is created the boolean 'splashing' will be true.
 *     Once the server sends the signal that everything is in place, splashing turns to false.
 *
 *     The users has the ability to select one of the three options in the menu
 *
 *     Login will enter LoginState
 *     Register will enter RegisterState
 *     Quit will disconnect from the server, then Exit(0);
 * </p>
 */
public class MenuState extends GameState implements SocketObserver {

    private int currentChoice = 0;
    private String[] options = {
            "Login",
            "Register",
            "Quit"
    };

    private BufferedImage filler;
    private BufferedImage sign;

    private static boolean splashing = true;
    private BufferedImage logo;

    private boolean timedOut = false;
    private int currentOnline;

    public MenuState(GameStateController gsc) {
        this.gsc = gsc;


        filler = ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_ONE);
        sign = ImageLoader.getInstance().getImage(ImageLoader.ARROWED_SIGN);
        logo = ImageLoader.getInstance().getImage("/logo.png");

        final long start = System.currentTimeMillis();
        final int timeout = 10000;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (splashing) {
                    if (start + timeout < start) timedOut = true;
                }
            }
        }).start();


    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() + " was initiated");


        try {
            GameServerConnectionRunnable.getInstance().register(this);
            if (splashing) {
                GameServerSocket.getInstance().send("/connect");
            }
        }catch (NullPointerException e) {
            try {
                Thread.sleep(1000);
                GameServerSocket.getInstance().send("/connect");

            } catch (InterruptedException i) {
                i.printStackTrace();
            } catch (NullPointerException i) {
                GameStateController.getInstance().setState(GameStateController.SERVEROFFLINESTATE);
            }
        }
    }

    @Override
    public void updateLogic() { }

    @Override
    public void draw(Graphics2D g) {
        if (!splashing) {
            //draw Background
            g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
            g.drawImage(filler, 0, 0, Main.WIDTH, Main.HEIGHT, null);

            g.drawImage(logo,Main.WIDTH/2-300, 50,null);

            g.drawImage(sign, (Main.WIDTH / 2) - 140, 190, 300, 200, null);
            g.drawImage(sign, (Main.WIDTH / 2) - 140, 240, 300, 200, null);
            g.drawImage(sign, (Main.WIDTH / 2) + 140, 290, -300, 200, null);

            g.setFont(Main.MAIN_FONT);

            for (int i = 0; i < options.length; i++) {
                if (i == currentChoice) {
                    g.setColor(Main.MAIN_COLOR);
                } else {
                    g.setColor(Main.DISABLED_COLOR);
                }
                g.drawString(options[i], StringCenter.center(options[i], g), 300 + i * 50);
            }

            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(Color.WHITE);
            g.drawString(
                    Main.BUILD,
                    (int) ((Main.WIDTH - 10) - g.getFontMetrics().getStringBounds(Main.BUILD, g).getWidth()),
                    (Main.HEIGHT - 10)
            );
        } else {
            if(hasTimedOut()){
                g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
                g.drawString(
                        "Server has problems connecting you,",
                        StringCenter.center("Server has problems connecting you,", g),
                        100
                );
                g.drawString(
                        "Please try again later!",
                        StringCenter.center("Please try again later!", g),
                        100
                );
            } else {
                //draw Background
                g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);
                g.drawImage(logo, Main.WIDTH/2-300, Main.HEIGHT/2-200, null);
            }
        }
    }


    private void select(){
        if(currentChoice == 0){
            // Login
            gsc.setState(GameStateController.LOGINSTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }
        if(currentChoice == 1){
            // Register
            gsc.setState(GameStateController.REGISTERSTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }
        if(currentChoice == 2){
            // Quit
            try {
                GameServerSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                GameServerSocket.getInstance().getServer().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(!splashing) {
            if (k == KeyEvent.VK_ENTER) {
                select();
            }

            if (k == KeyEvent.VK_UP) {
                currentChoice--;
                if (currentChoice == -1) {
                    currentChoice = options.length - 1;
                }
            }
            if (k == KeyEvent.VK_DOWN) {
                currentChoice++;
                if (currentChoice == options.length) {
                    currentChoice = 0;
                }
            }
            if (k == KeyEvent.VK_ESCAPE) {
                try {
                    GameServerSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                    GameServerSocket.getInstance().getServer().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(int k) { }

    @Override
    public void update(String s) {
        if(s.contains("/mainServerID")){
            Main.serverID = s.split(" ")[1];
        }
        if(s.contains("/mainCurrentlyOnline")){
            currentOnline = Integer.parseInt(s.split(" ")[1]);
        }
        if(s.contains("/mainExitSplash")){
            stopSplash();
        }
    }

    public void stopSplash(){
        splashing = false;
    }

    public boolean hasTimedOut() {
        return timedOut;
    }
}
