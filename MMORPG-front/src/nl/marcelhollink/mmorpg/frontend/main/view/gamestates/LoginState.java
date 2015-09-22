package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.utils.L;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;

public class LoginState extends GameState {

    char[] alphanumerics = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };

    private Image background;

    String username = "";
    boolean editUsername = true;

    String password = "";
    String passwordLength = "";
    boolean editPassword = false;

    private boolean hoverSubmit = false;

    Point usernamePos = new Point(100,270);
    Point passwordPos = new Point(100,340);

    private boolean alertFalseIdentification;
    private boolean alertNotFoundIdentification;

    public LoginState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        L.log(L.level.INFO, "Login was initiated");

        il = new ImageLoader();
        background = il.getImage("/login_pika.png");

        Arrays.sort(alphanumerics);

        alertFalseIdentification = false;
        alertNotFoundIdentification = false;
    }

    @Override
    public void update() {
        if(username.length() > 0) {
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //draw Background
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
        g.drawImage(background, UI.WIDTH/4, UI.HEIGHT/6, null);

        g.setColor(UI.mainColor);
        g.setFont(UI.titleFont);
        int start = StringCenter.center(UI.TITLE, g)-UI.WIDTH/4;
        g.drawString(UI.TITLE, start, UI.TITLEPOS.y);
        for (int i = 0; i < 4; i++) {
            g.drawLine(start,UI.TITLEPOS.y+10+i, (int) (g.getFontMetrics().getStringBounds(UI.TITLE,g).getWidth()+start),UI.TITLEPOS.y+10+i);
        }

        // draw credits
        g.setFont(new Font("Arial", Font.PLAIN, 21));
        int csLength = (int)
                g.getFontMetrics().getStringBounds("by Marcel Hollink", g).getWidth();
        g.drawString("by Marcel Hollink", (start+csLength),UI.TITLEPOS.y+35);

        g.setFont(UI.font);

        drawTextField(editUsername,"Username",username,usernamePos,g);
        drawTextField(editPassword,"Password",passwordLength,passwordPos,g);

        if(hoverSubmit) g.setColor(UI.mainColor);
        else g.setColor(UI.disabledColor);
        g.fillRoundRect(passwordPos.x, passwordPos.y + 70, 250, 30, 12, 12);
        g.setColor(Color.BLACK);
        g.drawString("Login", passwordPos.x + 82, passwordPos.y + 92);

        if(alertFalseIdentification) {
            g.setColor(Color.RED);
            g.drawString("WRONG USERNAME OR PASSWORD", 50, 50);
        }
        if(alertNotFoundIdentification) {
            g.setColor(Color.RED);
            g.drawString("USERNAME DOES NOT EXIST", 50, 50);
        }

        g.setFont(new Font("Arial",Font.ITALIC,12));
        g.setColor(Color.WHITE);
        g.drawString(
                UI.BUILD,
                (int)( (UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                (UI.HEIGHT-10)
        );
    }

    private void drawTextField(boolean hovered, String id, String text, Point position, Graphics2D g){
        if(hovered) g.setColor(UI.mainColor);
        else g.setColor(UI.disabledColor);
        g.drawString(id, position.x, position.y);
        g.drawRoundRect(position.x, position.y+10, (int) g.getFontMetrics(UI.titleFont).getStringBounds(UI.TITLE, g).getWidth(), 30, 12, 12);
        g.drawString(text, position.x + 7, position.y + 34);
    }


    @Override
    public void keyPressed(int k) {

        // IF escape is pressed
        if (k == VK_ESCAPE ) {
            username = "";
            password = "";
            passwordLength = "";
            alertFalseIdentification = false;
            alertNotFoundIdentification = false;

            gsc.setState(GameStateController.MENUSTATE);
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

            UI.clientSocket.send("/attemptedLogin "+username+" "+password);

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
    public void receive(String data) {
        if(data.contains("/loginSccs")){
            L.log(L.level.DEBUG, "User has logged in with "+username);
            gsc.setState(GameStateController.PROFILESTATE);
        } else if (data.contains("/loginFail") && data.contains("incorrect")) {
            username = ""; password = ""; passwordLength = "";
            alertFalseIdentification = true;
        } else if (data.contains("/loginFail") && data.contains("notFound")) {
            alertNotFoundIdentification = true;
        }
    }
}
