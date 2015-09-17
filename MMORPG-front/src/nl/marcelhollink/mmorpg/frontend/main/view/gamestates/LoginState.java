package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;


public class LoginState extends GameState {

    char[] alphanumerics = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };

    ImageLoader il;
    private Image background;


    String usename = "";
    boolean editUsername = true;
    String password = "";
    boolean editPassword = false;

    public LoginState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        il = new ImageLoader();
        background = il.getImage("/login.png");

        Arrays.sort(alphanumerics);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(Graphics2D g) {
        //draw Background
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
        g.drawImage(background, 0, 0, UI.WIDTH, UI.HEIGHT, null);

        g.drawString(usename, 50, 50);
    }

    @Override
    public void keyPressed(int k) {
        if (k == VK_ESCAPE) {
            gsc.setState(GameStateController.MENUSTATE);
        }
        if (k == VK_BACK_SPACE) {
            if (editUsername && usename.length()>0) usename = usename.substring(0, usename.length()-1);
            if (editPassword && password.length()>0) password = password.substring(0, password.length()-1);
        } else if (Arrays.binarySearch(alphanumerics, (char) k) > 0) {
            String c = String.valueOf(Character.toChars(k));

            if (editUsername) usename = usename.concat(c);
            if (editPassword) password = password.concat(c);
        }
        if(new ArrayList<Integer>(){
            {
                add(VK_NUMPAD0); add(VK_NUMPAD1); add(VK_NUMPAD2); add(VK_NUMPAD3); add(VK_NUMPAD4);
                add(VK_NUMPAD5); add(VK_NUMPAD6); add(VK_NUMPAD7); add(VK_NUMPAD8); add(VK_NUMPAD9);
            }
        }.contains(k)) {
            if (editUsername) usename = usename.concat(String.valueOf(k - VK_NUMPAD0));
            if (editPassword) usename = password.concat(String.valueOf(k - VK_NUMPAD0));
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
