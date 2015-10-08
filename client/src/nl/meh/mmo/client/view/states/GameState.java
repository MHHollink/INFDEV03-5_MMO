package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.view.GameStateController;

public abstract class GameState {
    protected GameStateController gsc;

    public abstract void init();
    public abstract void updateLogic();
    public abstract void draw(java.awt.Graphics2D g);
    public abstract void keyPressed(int k);
    public abstract void keyReleased(int k);

}
