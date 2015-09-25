package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
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
public class MenuState extends GameState {

    private int currentChoice = 0;
    private String[] options = {
            "Login",
            "Register",
            "Quit"
    };

    private BufferedImage filler;
    private BufferedImage sign;

    private static boolean splashing;
    private BufferedImage splash;

    private boolean timedOut = false;

    public MenuState(GameStateController gsc) {
        this.gsc = gsc;
        splashing = true;

        il = new ImageLoader();

        filler =  il.getImage("/FantasyWorld.jpg");
        sign = il.getImage("/sign.png");

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
        Logger.log(Logger.level.INFO, "MainState was initiated");
        splash = il.getImage("/splash.jpg");
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics2D g) {
        if (!splashing) {
            //draw Background
            g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
            g.drawImage(filler, 0, 0, UI.WIDTH, UI.HEIGHT, null);

            // draw title
            g.setColor(UI.mainColor);
            g.setFont(UI.titleFont);
            int start = StringCenter.center(UI.TITLE, g);
            g.drawString(UI.TITLE, start, UI.TITLEPOINT.y);
            for (int i = 0; i < 4; i++) {
                g.drawLine(start, UI.TITLEPOINT.y + 10 + i, (int) (g.getFontMetrics().getStringBounds(UI.TITLE, g).getWidth() + start), UI.TITLEPOINT.y + 10 + i);
            }

            g.drawImage(sign, (UI.WIDTH / 2) - 140, 190, 300, 200, null);
            g.drawImage(sign, (UI.WIDTH / 2) - 140, 240, 300, 200, null);
            g.drawImage(sign, (UI.WIDTH / 2) + 140, 290, -300, 200, null);

            // draw credits
            g.setFont(new Font("Arial", Font.PLAIN, 21));
            int csLength = (int)
                    g.getFontMetrics().getStringBounds("by Marcel Hollink", g).getWidth();
            g.drawString("by Marcel Hollink", (start + csLength), UI.TITLEPOINT.y + 35);

            g.setFont(UI.font);

            for (int i = 0; i < options.length; i++) {
                if (i == currentChoice) {
                    g.setColor(UI.mainColor);
                } else {
                    g.setColor(UI.disabledColor);
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
                g.drawImage(splash, 0, 0, null);
            }
        }
    }


    private void select(){
        if(currentChoice == 0){
            // Login
            gsc.setState(GameStateController.LOGINSTATE);
        }
        if(currentChoice == 1){
            // Register
            gsc.setState(GameStateController.REGISTERSTATE);
        }
        if(currentChoice == 2){
            // Quit
            try {
                UI.clientSocket.send("/disconnectMeFromMMORPGServer");
                UI.clientSocket.getServer().close();
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
                    UI.clientSocket.send("/disconnectMeFromMMORPGServer");
                    UI.clientSocket.getServer().close();
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
    public void receive(String s) {

    }



    public static void stopSplash(){
        splashing = false;
    }

    public boolean hasTimedOut() {
        return timedOut;
    }
}
