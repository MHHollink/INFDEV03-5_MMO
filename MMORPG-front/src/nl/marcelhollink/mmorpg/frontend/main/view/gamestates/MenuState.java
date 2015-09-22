package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.utils.L;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.IOException;

public class MenuState extends GameState {

    private int currentChoice = 0;
    private String[] options = {
            "Login",
            "Register",
            "Quit"
    };

    private Image background;

    private static boolean splashing;
    private Image splash;

    public MenuState(GameStateController gsc) {
        this.gsc = gsc;
        splashing = true;
    }

    @Override
    public void init() {
        L.log(L.level.INFO, "MainState was initiated");

        il = new ImageLoader();

        background = il.getImage("/login_pika.png");
        splash = il.getImage("/splash.png");
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics2D g) {
        if (!splashing) {
            //draw Background
            g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
            g.drawImage(background, UI.WIDTH / 4, UI.HEIGHT / 6, null);

            // draw title
            g.setColor(UI.mainColor);
            g.setFont(UI.titleFont);
            int start = StringCenter.center(UI.TITLE, g)-UI.WIDTH/4;
            g.drawString(UI.TITLE, start, UI.TITLEPOS.y);
            for (int i = 0; i < 4; i++) {
                g.drawLine(start,UI.TITLEPOS.y+10+i, (int) (g.getFontMetrics().getStringBounds(UI.TITLE,g).getWidth()+start),UI.TITLEPOS.y+10+i);
            }

            // draw credits
            g.setFont(new Font("Arial", Font.PLAIN, 21));
            int csLength = (int)
                    g.getFontMetrics().getStringBounds("by Marcel Hollink", g).getWidth();
            g.drawString("by Marcel Hollink", (start+csLength),UI.TITLEPOS.y+35);

            g.setFont(UI.font);

            for (int i = 0; i < options.length; i++) {
                if (i == currentChoice) {
                    g.setColor(UI.mainColor);
                } else {
                    g.setColor(UI.disabledColor);
                }
                g.drawString(options[i], StringCenter.center(options[i], g) - UI.WIDTH / 4, 300 + i * 38);
            }

            g.setFont(new Font("Arial", Font.ITALIC, 12));
            g.setColor(Color.WHITE);
            g.drawString(
                    UI.BUILD,
                    (int) ((UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                    (UI.HEIGHT - 10)
            );

        } else {
            //draw Background
            g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);
            g.drawImage(splash, (UI.WIDTH / 2)-100, (UI.HEIGHT / 2)-100, null);

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
    public void keyReleased(int k) {    }

    @Override
    public void receive(String s) {

    }

    public static void stopSplash(){
        splashing = false;
    }
}
