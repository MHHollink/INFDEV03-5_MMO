package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * This Class was created by marcel on 28-9-2015
 * Time of creation : 13:05
 */
public class ServerOfflineState extends GameState {

    String s = "The server is currently not online,",
           n = "please try again later.";

    private BufferedImage filler;
    private BufferedImage sign;
    private BufferedImage logo;

    public ServerOfflineState(GameStateController gsc) {
        this.gsc = gsc;
        this.il = new ImageLoader();

        filler = il.getImage("/FantasyWorld.jpg");
        sign = il.getImage("/signNoArrow.png");
        logo = il.getImage("/logo.png");
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, UI.WIDTH,UI.HEIGHT);
        g.drawImage(filler,0,0,UI.WIDTH,UI.HEIGHT,null);


        g.drawImage(sign,175,375,UI.WIDTH-350,100,null);

        g.setFont(UI.font);
        g.setColor(UI.mainColor);

        g.drawString(s, StringCenter.center(s, g),UI.HEIGHT/4+UI.HEIGHT/2 -9);
        g.drawString(n, StringCenter.center(n,g),UI.HEIGHT/4+UI.HEIGHT/2 +9);

        g.drawImage(logo,UI.WIDTH/2-300, 50,null);
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            Logger.log(Logger.level.DEBUG, "Exiting cause no connection could be establish server");
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void receive(String s) {

    }
}