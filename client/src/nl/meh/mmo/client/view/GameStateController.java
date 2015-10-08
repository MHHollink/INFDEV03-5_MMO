package nl.meh.mmo.client.view;

import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.states.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * The gameStateController is the main controller of the application.
 *
 * The GSC connects the states to the game panel, method 'setState' is used to switch states.
 *
 * methods update, draw and all key events are send from the GameFrame via the GSC to the currently active state.
 */
public class GameStateController {

    // SINGLETON to ensure only one GSC is created
    private static GameStateController instance;
    public static GameStateController getInstance() {
        if(instance == null) {
            Logger.log(Logger.level.INFO, "Tried to get GameStateController instance but did not exist, creating one...");
            instance = new GameStateController();
        }
        return instance;
    }

    // List of all possible stated
    private ArrayList<GameState> gameStates;
    // Current state showing in the Game Frame
    private int currentState;

    // State names for each state
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
