package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * This Class was created by marcel on 28-9-2015
 * Time of creation : 16:42
 */
public class ProfileManagerState extends GameState {

    public ProfileManagerState(GameStateController gsc) {
        this.gsc = gsc;

        this.il = new ImageLoader();
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, "ProfileManagerState was initiated");
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, UI.WIDTH,UI.HEIGHT);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            gsc.setState(GameStateController.PROFILESTATE);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void receive(String s) {

    }
}
