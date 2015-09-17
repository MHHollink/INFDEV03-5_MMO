package nl.marcelhollink.mmorpg.frontend.main.controller;

import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.*;

import java.awt.*;
import java.util.ArrayList;

public class GameStateController {
    private ArrayList<GameState> gameStates;
    private int currentState;

    public static final int
            MENUSTATE = 0,
            LOGINSTATE = 1,
            REGISTERSTATE = 2,
            PROFILESTATE = 3,
            AVATAROVERVIEWSTATE = 4,
            AVATARMANAGER = 5;

    public GameStateController() {
        gameStates = new ArrayList<GameState>();

        gameStates.add(new MenuState(this));
        gameStates.add(new LoginState(this));
        gameStates.add(new RegisterState(this));
        gameStates.add(new ProfileState(this));
        gameStates.add(new AvatarOverviewState(this));
        gameStates.add(new AvatarManagerState(this));

        setState(MENUSTATE);
    }

    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }

    public void update(){
        gameStates.get(currentState).update();
    }

    public void draw(Graphics2D g){
        gameStates.get(currentState).draw(g);
    }

    public void keyPressed(int k){
        gameStates.get(currentState).keyPressed(k);
    }

    public void keyReleased(int k){
        gameStates.get(currentState).keyReleased(k);
    }
}
