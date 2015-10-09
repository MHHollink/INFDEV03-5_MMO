package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.view.GameStateController;

/**
 * Abstract Gamestate is used to extend all different gamestates used in the gamestate manager.
 */
public abstract class GameState {
    protected GameStateController gsc;

    /**
     * Init is invoked on the startup of the state.
     */
    public abstract void init();

    /**
     * Update logic is invoked every single game loop. it updates logic in the game.
     */
    public abstract void updateLogic();

    /**
     * Draw is invoked on every single run, it creates an image on the Graphics object to be drawn later in the JPanel
     *
      * @param g graphics object
     */
    public abstract void draw(java.awt.Graphics2D g);

    /**
     * Invoked on key press
     * @param k keycode of the key that has been pressed
     */
    public abstract void keyPressed(int k);

    /**
     * invoked on key release
     * @param k keycode of the key that has been released
     */
    public abstract void keyReleased(int k);

}
