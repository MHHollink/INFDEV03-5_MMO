package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ServerConnectionRunnable;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.observers.SocketObserver;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This Class was created by marcel on 24-9-2015
 * Time of creation : 13:05
 */
public class ShoppingState extends GameState implements SocketObserver {


    public ShoppingState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");

        ServerConnectionRunnable.getObserverSubject().register(this);
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, UI.WIDTH,UI.HEIGHT);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            gsc.setState(GameStateController.PROFILESTATE);
            ServerConnectionRunnable.getObserverSubject().unregister(this);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String s) {

    }
}
