package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.connection.ServerConnectionRunnable;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.observers.SocketObserver;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;

public class LoginState extends GameState implements SocketObserver{

    char[] alphanumerics = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };

    private BufferedImage background;
    private BufferedImage sign;
    private BufferedImage logo;

    private String username = "";
    private boolean editUsername = true;

    private String password = "";
    private String passwordLength = "";
    private boolean editPassword = false;

    private boolean hoverSubmit = false;

    private Point usernamePos = new Point(UI.WIDTH/2 -150,270);
    private Point passwordPos = new Point(UI.WIDTH/2 -150,345);

    private boolean alertError;
    private String errorString;


    public LoginState(GameStateController gsc) {
        this.gsc = gsc;

    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");

        il = new ImageLoader();
        background = il.getImage(ImageLoader.FANTASY_WORLD_TWO);
        sign = il.getImage(ImageLoader.NO_ARROWED_SIGN);
        logo = il.getImage(ImageLoader.LOGO);

        Arrays.sort(alphanumerics);

        alertError = false;

        ServerConnectionRunnable.getObserverSubject().register(this);
    }

    @Override
    public void updateLogic() {
        if(username.length() > 0) {
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //draw Background
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
        g.drawImage(background, 0, 0, UI.WIDTH, UI.HEIGHT, null);

        g.drawImage(logo,UI.WIDTH/2-300, 50,null);

        g.setFont(UI.MAIN_FONT);

        g.drawImage(sign, UI.WIDTH/2-200,235,400,85,null);
        g.drawImage(sign, UI.WIDTH/2+200,310,-400,85,null);
        g.drawImage(sign, UI.WIDTH/2-200,385,400,85,null);

        drawTextField(editUsername, "Username", username, usernamePos, g);
        drawTextField(editPassword,"Password",passwordLength,passwordPos,g);

        if(hoverSubmit) g.setColor(UI.MAIN_COLOR);
        else g.setColor(UI.DISABLED_COLOR);
        g.drawString("Login", passwordPos.x + 107, passwordPos.y + 92);

        g.setColor(Color.RED);
        if(alertError)
            g.drawString(errorString, StringCenter.center(errorString,g), UI.HEIGHT-50);



        g.setFont(new Font("Arial",Font.ITALIC,12));
        g.setColor(Color.WHITE);
        g.drawString(
                UI.BUILD,
                (int)( (UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                (UI.HEIGHT-10)
        );
    }

    private void drawTextField(boolean hovered, String id, String text, Point position, Graphics2D g){
        if(hovered) g.setColor(UI.MAIN_COLOR);
        else g.setColor(UI.DISABLED_COLOR);
        g.drawString(id, StringCenter.center(id,g), position.y);
        g.drawString(text, position.x + 7, position.y + 34);
    }


    @Override
    public void keyPressed(int k) {

        // IF escape is pressed
        if (k == VK_ESCAPE ) {
            username = "";
            password = "";
            passwordLength = "";
            alertError = false;

            gsc.setState(GameStateController.MENUSTATE);
            ServerConnectionRunnable.getObserverSubject().unregister(this);
        }
        // IF tab is pressed OR Enter is pressed WHILE editing username of password
        if (k == VK_UP || k == VK_DOWN || (k == VK_ENTER && !hoverSubmit)) {

            if (editPassword && k == VK_ENTER) {
                hoverSubmit = true;
                editPassword = false;
                return;
            } else if (hoverSubmit) {
                editUsername = true;
                hoverSubmit = false;
                return;
            }

            editPassword = !editPassword;
            editUsername = !editUsername;
        }

        if (k == VK_ENTER && hoverSubmit) {

            ClientSocket.getInstance().send("/attemptedLogin " + username + " " + password);

        }

        // IF backspace is pressed and Username/Password has at least one Character.
        if (k == VK_BACK_SPACE) {
            if (editUsername && username.length()>0) username = username.substring(0, username.length()-1);
            if (editPassword && password.length()>0) {
                password = password.substring(0, password.length()-1);
                passwordLength = passwordLength.substring(0,passwordLength.length()-1);
            }

        // ELSE IF the value is within the Alphanumerics
        } else if (Arrays.binarySearch(alphanumerics, (char) k) > 0) {
            String c = String.valueOf(Character.toChars(k));

            if (editUsername && username.length() < 12) username = username.concat(c);
            if (editPassword && password.length() < 12) {
                password = password.concat(c);
                passwordLength = passwordLength.concat("*");
            }
        }

        // IF any numPad key is pressed
        if(new ArrayList<Integer>(){
            {
                add(VK_NUMPAD0); add(VK_NUMPAD1); add(VK_NUMPAD2); add(VK_NUMPAD3); add(VK_NUMPAD4);
                add(VK_NUMPAD5); add(VK_NUMPAD6); add(VK_NUMPAD7); add(VK_NUMPAD8); add(VK_NUMPAD9);
            }
        }.contains(k)) {
            if (editUsername && username.length() < 13) username = username.concat(String.valueOf(k - VK_NUMPAD0));
            if (editPassword && password.length() < 13) {
                password = password.concat(String.valueOf(k - VK_NUMPAD0));
                passwordLength = passwordLength.concat("*");
            }
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String data) {
        if(data.contains("/loginSuccessful")){
            Logger.log(Logger.level.INFO, "User has logged in with " + username);
            ProfileState.user.setUsername(username);
            alertError = false;
            gsc.setState(GameStateController.PROFILESTATE);
            ServerConnectionRunnable.getObserverSubject().unregister(this);
        } else if (data.contains("/loginFail") && data.contains("incorrect")) {
            username = ""; password = ""; passwordLength = "";
            alertError = true;
            errorString = "The password entered was incorrect";
        } else if (data.contains("/loginFail") && data.contains("notFound")) {
            username = ""; password = ""; passwordLength = "";
            alertError = true;
            errorString = "this username does not exist";
        } else if (data.contains("/loginFail") && data.contains("alreadyLoggedIn")) {
            username = ""; password = ""; passwordLength = "";
            alertError = true;
            errorString = "Your account seems to be logged in already";
        }

    }

}
