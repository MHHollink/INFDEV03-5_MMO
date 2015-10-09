package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * This class is the Shopping class.
 *
 * The class shows 3 things that can be bought and sends the amounts to the server when entered.
 */
public class ShoppingState extends GameState implements SocketObserver {


    private int select = 0;
    private String[] selectables = {
            "money",
            "days",
            "slots"
    };
    int a;


    public ShoppingState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");

        GameServerConnectionRunnable.getInstance().register(this);
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0,0, Main.WIDTH,Main.HEIGHT);

        for (int i = 0; i < selectables.length; i++) {
            if(i==select) g.setColor(Main.MAIN_COLOR);
            else g.setColor(Main.DISABLED_COLOR);
            g.drawString(selectables[i], StringCenter.center(selectables[select],g)-(Main.WIDTH/selectables.length)*(i-1), 100);
        }

        String q = "";
        switch (select) {
            case 0:
                q = "how much money do you like to insert in you game?";
                break;
            case 1:
                q = "how much month would you like to buy?";
                break;
            case 2:
                q = "how much slots would you like to purchase?";
                break;
            default:
                    q="";
                break;
        }
        g.setColor(Main.MAIN_COLOR);
        g.drawString(q, StringCenter.center(q,g),175);
        g.drawString(String.valueOf(a), StringCenter.center(String.valueOf(a),g),225);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            gsc.setState(GameStateController.PROFILESTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }
        if (k == KeyEvent.VK_ENTER) {
            switch (select) {
                case 0:
                    GameServerSocket.getInstance().send("/shop money "+a);
                    break;
                case 1:
                    GameServerSocket.getInstance().send("/shop days "+a);
                    break;
                case 2:
                    GameServerSocket.getInstance().send("/shop characters " + a);
                    break;
                default:
                    break;
            }
        }

        if (k == KeyEvent.VK_LEFT) {
            a=0;
            select++;
            if(select > selectables.length-1) select = 0;
        }
        if (k == KeyEvent.VK_RIGHT) {
            a=0;
            select--;
            if(select < 0) select = selectables.length-1;
        }

        switch (select) {
            case 0:
                if (k == KeyEvent.VK_UP) {
                    a = a + 5;
                }
                if (k == KeyEvent.VK_DOWN && a > 0) {
                    a = a - 5;
                }

                break;
            case 1:
                if (k == KeyEvent.VK_UP ) {
                    if(a == 3) a = 12;
                    if(a == 2) a = 3;
                    if(a == 1) a = 2;
                    if(a == 0) a = 1;
                }
                if (k == KeyEvent.VK_DOWN) {
                    if(a == 2) a = 1;
                    if(a == 3) a = 2;
                    if(a == 12) a = 3;
                }

                break;
            case 2:
                if (k == KeyEvent.VK_UP &&
                        ProfileState.user.getCharacterSlots() + ProfileState.user.getCharacters().size() + a < 6) {
                    a++;
                }
                if (k == KeyEvent.VK_DOWN && a > 0) {
                    a--;
                }
                break;
            default:break;
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String s) {
        if(s.contains("/shopSuccessful")) {
            GameServerConnectionRunnable.getInstance().unregister(this);
            a = 0;
            gsc.setState(GameStateController.PROFILESTATE);

        } else {
            // TODO !!
        }
    }
}
