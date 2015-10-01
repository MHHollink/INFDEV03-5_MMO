//package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;
//
//import nl.marcelhollink.mmorpg.frontend.main.UI;
//import nl.marcelhollink.mmorpg.frontend.main.connection.ServerConnectionRunnable;
//import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
//import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
//import nl.marcelhollink.mmorpg.frontend.main.observers.SocketObserver;
//import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
//import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.GameState;
//
//import java.awt.*;
//import java.awt.event.KeyEvent;
//import java.util.ArrayList;
//
//
//public class ProfileManagerState extends GameState implements SocketObserver {
//
//    public ProfileManagerState(GameStateController gsc) {
//        this.gsc = gsc;
//        this.il = new ImageLoader();
//    }
//
//    @Override
//    public void init() {
//
//        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");
//        ServerConnectionRunnable.getObserverSubject().register(this);
//    }
//
//    @Override
//    public void updateLogic() {
//
//    }
//
//    @Override
//    public void draw(Graphics2D g) {
//        g.clearRect(0,0, UI.WIDTH,UI.HEIGHT);
//    }
//
//    @Override
//    public void keyPressed(int k) {
//        if (k == KeyEvent.VK_ESCAPE) {
//            gsc.setState(GameStateController.PROFILESTATE);
//            ServerConnectionRunnable.getObserverSubject().unregister(this);
//        }
//    }
//
//    @Override
//    public void keyReleased(int k) {
//
//    }
//
//    @Override
//    public void update(String s) {
//
//    }
//}
