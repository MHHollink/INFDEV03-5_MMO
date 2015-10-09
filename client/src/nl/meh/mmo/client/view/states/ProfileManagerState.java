package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;

@Deprecated
public class ProfileManagerState extends GameState implements SocketObserver {

    public ProfileManagerState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() + " was initiated");
        GameServerConnectionRunnable.getInstance().register(this);
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, Main.WIDTH, Main.HEIGHT);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            gsc.setState(GameStateController.PROFILESTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String s) {

    }
}
