package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * This Class was created by marcel on 28-9-2015
 * Time of creation : 12:21
 */
public class ServerDisconnectedState extends GameState {

    String s = "The server has been terminated,",
           n = "please log back in later.";

    BufferedImage logo;
    BufferedImage filler;
    BufferedImage sign;

    public ServerDisconnectedState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");

        filler = ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_ONE);
        sign = ImageLoader.getInstance().getImage(ImageLoader.NO_ARROWED_SIGN);
        logo = ImageLoader.getInstance().getImage(ImageLoader.LOGO);
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, Main.WIDTH,Main.HEIGHT);
        g.drawImage(filler,0,0,Main.WIDTH,Main.HEIGHT,null);


        g.drawImage(sign,175,375,Main.WIDTH-350,100,null);

        g.setFont(Main.MAIN_FONT);
        g.setColor(Main.MAIN_COLOR);

        g.drawString(s, StringCenter.center(s, g),Main.HEIGHT/4+Main.HEIGHT/2 -9);
        g.drawString(n, StringCenter.center(n,g),Main.HEIGHT/4+Main.HEIGHT/2 +9);

        g.drawImage(logo,Main.WIDTH/2-300, 50,null);
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            Logger.log(Logger.level.DEBUG, "Exiting after disconnected server");
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

}
