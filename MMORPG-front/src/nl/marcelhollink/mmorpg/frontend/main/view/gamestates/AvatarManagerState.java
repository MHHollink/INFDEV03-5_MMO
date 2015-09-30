package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.connection.ServerConnectionRunnable;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

// TODO :: IMPLEMENT CREATION OF CHARACTER IF FREESLOTS, SELECTING CHARACTER IF AVALIABLE, DELETING CHARACTER
public class AvatarManagerState extends GameState{


    public AvatarManagerState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +"was initiated");
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
        }

        if (k == KeyEvent.VK_Y) {
            ClientSocket.getInstance().send("/registerMMOCharacter " + ProfileState.user.getUsername() + " odinson male");

        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
