package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.model.Player;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class ProfileState extends GameState {

    private static int cheatCode = 0;

    public static Player user;

    private boolean topLeft = true;
    private boolean topRight = false;
    private boolean bottom = false;

    BufferedImage filler;

    public ProfileState(GameStateController gsc) {
        this.gsc = gsc;
        user = new Player(null, null, null, 0, null, 0, 0, null);

        il = new ImageLoader();
        filler = il.getImage("/FantasyWorld2.jpg");
    }

    @Override
    public void init() {
        UI.clientSocket.send("/request userDetails "+user.getUsername());
        UI.clientSocket.send("/request charactersFor"+user.getUsername());
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0, 0, UI.WIDTH, UI.HEIGHT);

        g.drawImage(filler,0,0,UI.WIDTH,UI.HEIGHT,null);

        g.setColor(UI.disabledColor);
        // VERTICAL LINE SEPARATOR
        g.fillRect(UI.WIDTH/2, 0, 3, UI.HEIGHT-80);

        // HORIZONTAL LINE SEPARATOR
        g.fillRect(0, UI.HEIGHT-80, UI.WIDTH, 3);

        g.setColor(new Color(255,255,255,155));
        if (topLeft && !bottom) g.fillRect(0, 0, UI.WIDTH / 2, UI.HEIGHT - 80);
        if (topRight &&!bottom) g.fillRect(UI.WIDTH / 2 + 2, 0, UI.WIDTH / 2 - 2, UI.HEIGHT - 80);
        if (bottom) g.fillRect(0, UI.HEIGHT - 78, UI.WIDTH, 80);

        g.setColor(UI.mainColor);
        g.setFont(UI.titleFont);
        g.drawString(user.getUsername(),
                ((UI.WIDTH / 4) - UI.WIDTH / 2) + StringCenter.center(user.getUsername(), g),
                75
        );
        g.setFont(UI.font);
        g.drawString("Name:", 15, 125);
        g.drawString(user.getFirstName()+" "+user.getLastName(),
                ((UI.WIDTH / 4) - UI.WIDTH / 2) + StringCenter.center(user.getFirstName()+" "+user.getLastName(), g),
                160
        );
        g.drawString("Balance:",15,250);
        g.drawString(String.valueOf(user.getBalance()),310,250);
        g.drawString("Last transaction:",15,290);
        g.drawString(user.getLastPayment()+"",310,290);
        g.drawString("Days left to play:",15,330);
        g.drawString(user.getMonthsPayedFor()+"",310,330);
        g.drawString("Banking account:", 15, 420);
        g.drawString(""+user.getIban(),
                ((UI.WIDTH / 4) - UI.WIDTH / 2) + StringCenter.center(""+user.getIban(), g),
                460
        );

        g.drawString("Enter the Shop!", StringCenter.center("Enter the Shop!",g), UI.HEIGHT-30);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            try {
                UI.clientSocket.send("/disconnectMeFromMMORPGServer");
                UI.clientSocket.getServer().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        if (k == KeyEvent.VK_LEFT && !bottom) {topLeft = true; topRight = false;}
        if (k == KeyEvent.VK_RIGHT && !bottom) {topLeft = false; topRight = true;}
        if (k == KeyEvent.VK_DOWN) bottom = true;
        if (k == KeyEvent.VK_UP) bottom = false;

        if (k == KeyEvent.VK_ENTER) {
            select();
        }
    }

    private void select(){
        if(bottom) {
            gsc.setState(GameStateController.SHOPPINGSTATE);
        } else if (topRight) {
            gsc.setState(GameStateController.AVATARMANAGERSTATE);
        } else {
            // Must be topLeft
            gsc.setState(GameStateController.PROFILEMANAGERSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_U && cheatCode == 0){
            cheatCode++;
            return;
        } else if (cheatCode != 0) {
            // Add more letters to the cheatcode...
            switch (cheatCode){
                case 1:if(k==KeyEvent.VK_P){cheatCode++;return;}
                case 2:if(k==KeyEvent.VK_D){cheatCode++;return;}
                case 3:if(k==KeyEvent.VK_A){cheatCode++;return;}
                case 4:if(k==KeyEvent.VK_T){cheatCode++;return;}
                case 5:if(k==KeyEvent.VK_E){cheatCode++;return;}
                case 6:if(k==KeyEvent.VK_M){cheatCode++;return;}
                case 7:if(k==KeyEvent.VK_Y){cheatCode++;return;}
                case 8:if(k==KeyEvent.VK_M){cheatCode++;return;}
                case 9:if(k==KeyEvent.VK_O){cheatCode++;return;}
                case 10:if(k==KeyEvent.VK_N){cheatCode++;return;}
                case 11:if(k==KeyEvent.VK_E){cheatCode++;return;}
                case 12:if(k==KeyEvent.VK_Y){cheatCode++;return;}
                case 13:if(k==KeyEvent.VK_INSERT){
                    user.setBalance(user.getBalance()+20);
                    UI.clientSocket.send("/UserCheatCodePlusTwentyBalanceInsert");
                    cheatCode=0;
                    return;
                }
            }
        }
        cheatCode = 0;
    }

    @Override
    public void receive(String data) {
        if (data.contains("/userDetails")) {
            Logger.log(Logger.level.INFO, "data received");
            String[] args = data.split(" ");
            user = new Player(
                    args[1],
                    args[2],
                    args[3],
                    Double.parseDouble(args[4]),
                    args[5],
                    Integer.parseInt(args[6]),
                    Integer.parseInt(args[7]),
                    args[8]
            );
        }

    }

}
