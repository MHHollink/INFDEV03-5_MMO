package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.model.Player;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.util.StringCenter;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ProfileState extends GameState implements SocketObserver {

    private static int cheatCode = 0;

    public static Player user;

    private boolean left = true; // left is preset, not shown while top...
    private boolean right = false;
    private boolean bottom = false;
    private boolean top = true;

    BufferedImage filler;


    public ProfileState(GameStateController gsc) {
        this.gsc = gsc;
        user = new Player(null, null, null, 0, null, 0, 0, null);

        filler = ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_TWO);
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");
        GameServerConnectionRunnable.getInstance().register(this);

        GameServerSocket.getInstance().send("/request userDetails " + user.getUsername());
        GameServerSocket.getInstance().send("/request charactersFor " + user.getUsername());
    }

    @Override
    public void updateLogic() {

    }

    @Override
    public void draw(Graphics2D g) {
        g.clearRect(0, 0, Main.WIDTH, Main.HEIGHT);

        g.drawImage(filler, 0, 0, Main.WIDTH, Main.HEIGHT, null);

        g.setColor(Main.DISABLED_COLOR);
        // VERTICAL LINE SEPARATOR
        g.fillRect(Main.WIDTH/2, 80, 3, Main.HEIGHT-160);

        // HORIZONTAL LINE SEPARATOR
        g.fillRect(0, 80, Main.WIDTH, 3);
        g.fillRect(0, Main.HEIGHT - 80, Main.WIDTH, 3);

        g.setColor(new Color(135, 181, 184, 220));
        if (left && !bottom && !top) g.fillRect(0, 81, Main.WIDTH / 2, Main.HEIGHT - 159);
        if (right && !bottom && !top) g.fillRect(Main.WIDTH / 2 + 2, 81, Main.WIDTH / 2 - 2, Main.HEIGHT - 159);
        if (bottom) g.fillRect(0, Main.HEIGHT - 78, Main.WIDTH, 80);
        if (top) g.fillRect(0, 0, Main.WIDTH, 80);

        g.setColor(Main.MAIN_COLOR);
        g.setFont(Main.MAIN_FONT);
        if(left && !bottom && !top) { // IF LEFT
            g.drawString("Name:", 15, 125);
            g.drawString(user.getFirstName() + " " + user.getLastName(),
                    ((Main.WIDTH / 4) - Main.WIDTH / 2) + StringCenter.center(user.getFirstName() + " " + user.getLastName(), g),
                    160
            );
            g.drawString("Balance:", 15, 250);
            g.drawString(String.valueOf(user.getBalance()), 310, 250);
            g.drawString("Last transaction:", 15, 290);
            g.drawString(user.getLastPayment() + "", 310, 290);
            g.drawString("Days left to play:", 15, 330);
            g.drawString(user.getMonthsPayedFor() + "", 310, 330);
            g.drawString("Banking account:", 15, 420);
            g.drawString("" + user.getIban(),
                    ((Main.WIDTH / 4) - Main.WIDTH / 2) + StringCenter.center("" + user.getIban(), g),
                    460
            );

            g.drawString("Characters", Main.WIDTH/4 + StringCenter.center("Characters",g), Main.HEIGHT/2);
        }
        if (right && !bottom && !top) { // IF RIGHT

            g.drawString("Free character slots",Main.WIDTH/2 + 15 , 125);
            g.drawString(String.valueOf(user.getCharacterSlots()),Main.WIDTH - 25 - ((int) g.getFontMetrics().getStringBounds(user.getCharacterSlots()+"",g).getWidth()), 125);

            g.drawString("Characters", Main.WIDTH/2 + 15, 200);
            for (int i = 0; i < user.getCharacters().size(); i++) {
                g.drawString(user.getCharacters().get(i).getCharacterName(), Main.WIDTH/2 + 15, 240 + 40*i);
                g.drawString("Level: ", Main.WIDTH - 240, 240 +40 * i);

                int ttl = user.getCharacters().get(i).getTotalLevel();
                g.drawString(
                        String.valueOf(ttl),
                        Main.WIDTH - 25 - ((int) g.getFontMetrics().getStringBounds(ttl+"",g).getWidth()),
                        240 + 40*i
                );
            }

            g.drawString("Profile", StringCenter.center("Profile", g) - Main.WIDTH / 4, Main.HEIGHT / 2);
        }
        if(bottom || top){ // IF BOTTOM ORM TOP
            g.drawString("Characters", Main.WIDTH/4 + StringCenter.center("Characters",g), Main.HEIGHT/2);
            g.drawString("Profile", StringCenter.center("Profile", g) - Main.WIDTH / 4, Main.HEIGHT / 2);
        }
        g.drawString("Enter the Shop!", StringCenter.center("Enter the Shop!",g), Main.HEIGHT-30);
        g.drawString("Play!", StringCenter.center("Play!",g), 40);
    }

    @Override
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ESCAPE) {
            try {
                GameServerSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                GameServerSocket.getInstance().getServer().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        if (k == KeyEvent.VK_LEFT && !bottom && !top) {
            left = true; right = false;}
        if (k == KeyEvent.VK_RIGHT && !bottom && !top) {
            left = false; right = true;}
        if (k == KeyEvent.VK_DOWN && !top) bottom = true;
        if (k == KeyEvent.VK_DOWN && top) top = false;
        if (k == KeyEvent.VK_UP && !bottom) top = true;
        if (k == KeyEvent.VK_UP && bottom) bottom = false;

        if (k == KeyEvent.VK_ENTER) {
            select();
        }
    }

    private void select(){
        if(bottom) {
            gsc.setState(GameStateController.SHOPPINGSTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        } else if (top) {
            // NO GAME IMPLEMENTED

        } else if (right) {
            gsc.setState(GameStateController.CHARACTERCREATIONSTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        } else if (left) {
//            gsc.setState(GameStateController.PROFILEMANAGERSTATE);
//            GameServerConnectionRunnable.getInstance().unregister(this);
        }
    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_U && cheatCode == 0){
            cheatCode++;
            return;
        } else if (cheatCode != 0) {
            // Add more letters to the cheatcode...
            switch (cheatCode){
                case 1:if(k==KeyEvent.VK_P){cheatCode++;return;}
                case 2:if(k==KeyEvent.VK_D){cheatCode++;return;}
                case 3:if(k==KeyEvent.VK_A){cheatCode++;return;}
                case 4:if(k==KeyEvent.VK_T){cheatCode++;return;}
                case 5:if(k==KeyEvent.VK_E){cheatCode++;return;}
                case 6:if(k==KeyEvent.VK_M){cheatCode++;return;}
                case 7:if(k==KeyEvent.VK_Y){cheatCode++;return;}
                case 8:if(k==KeyEvent.VK_M){cheatCode++;return;}
                case 9:if(k==KeyEvent.VK_O){cheatCode++;return;}
                case 10:if(k==KeyEvent.VK_N){cheatCode++;return;}
                case 11:if(k==KeyEvent.VK_E){cheatCode++;return;}
                case 12:if(k==KeyEvent.VK_Y){cheatCode++;return;}
                case 13:if(k==KeyEvent.VK_INSERT){
                    user.setBalance(user.getBalance()+20);
                    GameServerSocket.getInstance().send("/UserCheatCodePlusTwentyBalanceInsert");
                    cheatCode=0;
                    return;
                }
            }
        }
        cheatCode = 0;
    }

    @Override
    public void update(String data) {
        if (data.contains("/userDetails")) {
            Logger.log(Logger.level.INFO, "user data received");
            String[] args = data.split(" ");
            user = new Player(
                    args[1],
                    args[2],
                    args[3],
                    Double.parseDouble(args[4]),
                    args[5],
                    Integer.parseInt(args[6]),
                    Integer.parseInt(args[7]),
                    args[8]
            );
        }
        if(data.contains("/characterDetails")){
            Logger.log(Logger.level.INFO, "character data received");
            String[] args = data.split(" ");
            nl.meh.mmo.client.model.Character character = new nl.meh.mmo.client.model.Character(
                    args[1],
                    args[2],
                    Integer.parseInt(args[3])
            );

            character.setAllLevels(
                    Integer.parseInt(args[4]),
                    Integer.parseInt(args[5]),
                    Integer.parseInt(args[6]),
                    Integer.parseInt(args[7]),
                    Integer.parseInt(args[8]),
                    Integer.parseInt(args[9]),
                    Integer.parseInt(args[10]),
                    Integer.parseInt(args[11])
                );
            ProfileState.user.addCharacters(character);
        }

    }

}
