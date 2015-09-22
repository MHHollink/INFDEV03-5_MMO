package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;

public class ProfileState extends GameState {

    public ProfileState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            gsc.setState(GameStateController.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void receive(String s) {

    }
}
