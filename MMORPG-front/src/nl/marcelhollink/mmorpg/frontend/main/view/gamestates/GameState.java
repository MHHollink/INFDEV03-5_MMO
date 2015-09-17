package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;

public abstract class GameState {
    protected GameStateController gsc;

    public abstract void init();
    public abstract void update();
    public abstract void draw(java.awt.Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);
}
