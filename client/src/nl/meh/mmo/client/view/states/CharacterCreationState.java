package nl.meh.mmo.client.view.states;


import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_RIGHT;

public class CharacterCreationState extends GameState implements SocketObserver{

    nl.meh.mmo.client.model.Character characterToCreate = new nl.meh.mmo.client.model.Character("","FEMALE",0);

    private BufferedImage filler;
    private BufferedImage sign;

    private String errorString = "";
    private boolean alertError = false;


    public CharacterCreationState(GameStateController gsc) {
        this.gsc = gsc;

        filler = ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_TREE);
        sign = ImageLoader.getInstance().getImage(ImageLoader.NO_ARROWED_SIGN);
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() + " was initiated");
        GameServerConnectionRunnable.getInstance().register(this);
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, Main.WIDTH,Main.HEIGHT);
        g.drawImage(filler, 0,0,Main.WIDTH,Main.HEIGHT,null);

        g.drawImage(sign,175,375,Main.WIDTH-350,100,null);

        g.setFont(Main.MAIN_FONT);
        g.setColor(Main.MAIN_COLOR);

        g.drawString(characterToCreate.getCharacterName(), StringCenter.center(characterToCreate.getCharacterName(), g),Main.HEIGHT/4+Main.HEIGHT/2 -9);
        g.drawString("<- "+characterToCreate.getGender()+" ->", StringCenter.center("<- " + characterToCreate.getGender() + " ->", g), Main.HEIGHT / 4 + Main.HEIGHT / 2 + 9);

        if(alertError)
            g.drawString(errorString, StringCenter.center(errorString, g), Main.HEIGHT-80);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {

            gsc.setState(GameStateController.PROFILESTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }

        if (k == KeyEvent.VK_ENTER) {
            alertError = false;
            if(characterToCreate.getCharacterName().length() < 3 ) {
                alertError = true;
                errorString = "Your character name must be at least of length 3";
                return;
            }

            GameServerSocket.getInstance().send("/registerMMOCharacter " + ProfileState.user.getUsername() +" "+characterToCreate.getCharacterName()+" "+characterToCreate.getGender());
        }

        if(k == KeyEvent.VK_LEFT || k == VK_RIGHT) {
            if (characterToCreate.getGender().equalsIgnoreCase("male")) characterToCreate.setGender("female");
            else characterToCreate.setGender("male");
        }

        // IF backspace is pressed and Username/Password has at least one Character.
        if (k == VK_BACK_SPACE) {
            if (characterToCreate.getCharacterName().length()>0) characterToCreate.setCharacterName(characterToCreate.getCharacterName().substring(0, characterToCreate.getCharacterName().length() - 1));
        // ELSE IF the value is within the Alphanumerics
        } else if ((65 <= k && k <= 90) && (characterToCreate.getCharacterName().length() <= 12)) {
            String c = String.valueOf(Character.toChars(k));
            characterToCreate.setCharacterName(characterToCreate.getCharacterName() + c);
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String s) {
        if(s.contains("/createCharacter")) {
            if(s.contains("successful")) {
                GameStateController.getInstance().setState(GameStateController.PROFILESTATE);
                GameServerConnectionRunnable.getInstance().unregister(this);
            }
            if(s.contains("error")) {
                String[] args = s.split(" ");
                String cause = args[args.length-1];

                if(cause.equals("alreadyExists")){
                    alertError = true;
                    errorString = "This character name is already in use";
                } else if(cause.equals("noSlotsAvailable")){
                    // should not happen...
                    alertError = true;
                    errorString = "It seems that you have used all you slots! buy more in the store";
                }

            }
        }
    }
}
