package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.connection.ServerConnectionRunnable;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.observers.SocketObserver;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

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
        this.il = new ImageLoader();

        filler =  il.getImage("/FantasyWorld.jpg");
        sign = il.getImage("/sign.png");

        final long start = System.currentTimeMillis();
        final int timeout = 10000;
        new Thread(() -> {
            while (splashing) {
                if (start + timeout < start) timedOut = true;
            }
        }).start();


    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() + " was initiated");
        logo = il.getImage("/logo.png");

        ServerConnectionRunnable.getObserverSubject().register(this);
        if (splashing) {
            ClientSocket.getInstance().send("/connect");
        }
    }

    @Override
    public void updateLogic() { }

    @Override
    public void draw(Graphics2D g) {
        if (!splashing) {
            //draw Background
            g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
            g.drawImage(filler, 0, 0, UI.WIDTH, UI.HEIGHT, null);

            g.drawImage(logo,UI.WIDTH/2-300, 50,null);

            g.drawImage(sign, (UI.WIDTH / 2) - 140, 190, 300, 200, null);
            g.drawImage(sign, (UI.WIDTH / 2) - 140, 240, 300, 200, null);
            g.drawImage(sign, (UI.WIDTH / 2) + 140, 290, -300, 200, null);

            g.setFont(UI.MAIN_FONT);

            for (int i = 0; i < options.length; i++) {
                if (i == currentChoice) {
                    g.setColor(UI.MAIN_COLOR);
                } else {
                    g.setColor(UI.DISABLED_COLOR);
                }
                g.drawString(options[i], StringCenter.center(options[i], g), 300 + i * 50);
            }

            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(Color.WHITE);
            g.drawString(
                    UI.BUILD,
                    (int) ((UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                    (UI.HEIGHT - 10)
            );
        } else {
            if(hasTimedOut()){
                g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
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
                g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
                g.drawImage(logo, UI.WIDTH/2-300, UI.HEIGHT/2-200, null);
            }
        }
    }


    private void select(){
        if(currentChoice == 0){
            // Login
            gsc.setState(GameStateController.LOGINSTATE);
            ServerConnectionRunnable.getObserverSubject().unregister(this);
        }
        if(currentChoice == 1){
            // Register
            gsc.setState(GameStateController.REGISTERSTATE);
            ServerConnectionRunnable.getObserverSubject().unregister(this);
        }
        if(currentChoice == 2){
            // Quit
            try {
                ClientSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                ClientSocket.getInstance().getServer().close();
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
                    ClientSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                    ClientSocket.getInstance().getServer().close();
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
            UI.serverID = s.split(" ")[1];
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
