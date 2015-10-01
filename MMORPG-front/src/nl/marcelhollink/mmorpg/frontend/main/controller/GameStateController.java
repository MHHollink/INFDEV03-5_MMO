package nl.marcelhollink.mmorpg.frontend.main.controller;

import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * This class is the GameStateController
 *
 * It is created in the GamePanel. The GamePanel will catch events, send them to the GSC which in turn sends them to the currentState.
 */
public class GameStateController {

    private static GameStateController instance;

    public static GameStateController getInstance() {
        if(instance == null) {
            Logger.log(Logger.level.INFO, "Tried to get GameStateController instance but did not exist, creating one...");
            instance = new GameStateController();
        }
        return instance;
    }

    private ArrayList<GameState> gameStates;
    private int currentState;

    public static final int
            SERVERDISCONNECTEDSTATE = 0,
            SERVEROFFLINESTATE = 1,
            MENUSTATE = 2,
            LOGINSTATE = 3,
            REGISTERSTATE = 4,
            PROFILESTATE = 5,
            //PROFILEMANAGERSTATE = 6,
            CHARACTERCREATIONSTATE = 7,
            SHOPPINGSTATE = 8;

    /**
     * Constructs the GSC, Initiate the GameStates-ArrayList and puts all states in it.
     * Sets current state to MenuState.
     */
    private GameStateController() {
        Logger.log(Logger.level.INFO, "GameStateController has been constructed");
        gameStates = new ArrayList<>();

        gameStates.add(new ServerDisconnectedState(this));
        gameStates.add(new ServerOfflineState(this));
        gameStates.add(new MenuState(this));
        gameStates.add(new LoginState(this));
        gameStates.add(new RegisterState(this));
        gameStates.add(new ProfileState(this));
        gameStates.add(null); //gameStates.add(new ProfileManagerState(this));
        gameStates.add(new CharacterCreationState(this));
        gameStates.add(new ShoppingState(this));

        setState(MENUSTATE);
    }

    /**
     * Sets the current state to the new currentState, then calls init();
     *
     * @param state new currentState
     */
    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    /**
     * Updates the currentStates logic
     */
    public void update(){
        gameStates.get(currentState).updateLogic();
    }

    /**
     * draws the currentState on the GamePanel
     * @param g Graphics2D object
     */
    public void draw(Graphics2D g){
        gameStates.get(currentState).draw(g);
    }

    /**
     * Sends a keyPressEvent to the currentState
     * @param k keycode
     */
    public void keyPressed(int k){
        gameStates.get(currentState).keyPressed(k);
    }

    /**
     * Sends a keyReleasedEvent to the currentState
     * @param k keycode
     */
    public void keyReleased(int k){
        gameStates.get(currentState).keyReleased(k);
    }

    /**
     * Gets the List of gameStates in the GameStateController
     * @return ArrayList of GameState.class objects
     */
    public ArrayList<GameState> getGameStates() {
        return gameStates;
    }

}
