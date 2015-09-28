package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AvatarManagerState extends GameState{
    public AvatarManagerState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, "AvatarManagerState was initiated");
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

        if (k == KeyEvent.VK_Y) {
            UI.clientSocket.send("/regiChar "+ProfileState.user.getUsername()+" mjollnir male");

        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void receive(String s) {

    }

}
