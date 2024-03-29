package nl.meh.mmo.client.view.states;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.socket.game.GameServerSocket;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.util.ImageLoader;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.GameStateController;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;

/**
 * This state handles the registration and validation of the registration field.
 *
 * It sends all the values needed to create an account to the server and receives a success of error.
 *
 * on success the client is send to the login state / on error the user gets a message on what is wrong.
 */
public class RegisterState extends GameState implements SocketObserver{

    char[] alphanumerics = new char[]{
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            '1','2','3','4','5','6','7','8','9','0'
    };

    private BufferedImage background;
    private BufferedImage sign;
    private BufferedImage logo;

    private String
            username = "",
            password = "",
            passwordLength = "",
            firstName = "",
            lastName = "",
            iban = "";

    private Point
            usernamePos     = new Point((Main.WIDTH/2)+100,100),
            passwordPos     = new Point((Main.WIDTH/2)+100,200),
            firstNamePos    = new Point((Main.WIDTH/2)+100,300),
            lastNamePos     = new Point((Main.WIDTH/2)+100,400),
            ibanPos         = new Point((Main.WIDTH/2)+100,500);

    private boolean
            editUsername    = true,
            editPassword    = false,
            editFirstName   = false,
            editLastName    = false,
            editIban        = false;

    private boolean
            usernameLengthIncorrect     = false,
            passwordLengthIncorrect     = false,
            firstNameLengthIncorrect    = false,
            lastNameLengthIncorrect     = false,
            ibanLengthIncorrect         = false;

    private String registerErrorMessage = "";

    public RegisterState(GameStateController gsc) {
        this.gsc = gsc;
    }

    @Override
    public void init() {
        Logger.log(Logger.level.INFO, getClass().getSimpleName() +" was initiated");

        background = ImageLoader.getInstance().getImage(ImageLoader.FANTASY_WORLD_TREE);
        sign = ImageLoader.getInstance().getImage(ImageLoader.NO_ARROWED_SIGN);
        logo = ImageLoader.getInstance().getImage(ImageLoader.LOGO);

        username = "";
        password = "";
        passwordLength = "";
        firstName = "";
        lastName = "";
        iban = "";

        editUsername = true;
        editPassword = false;
        editFirstName = false;
        editLastName = false;
        editIban = false;

        usernameLengthIncorrect = false;
        passwordLengthIncorrect = false;
        firstNameLengthIncorrect = false;
        lastNameLengthIncorrect = false;
        ibanLengthIncorrect = false;

        Arrays.sort(alphanumerics);

        GameServerConnectionRunnable.getInstance().register(this);
    }

    @Override
    public void updateLogic() {
        if(username.length() > 0) {
            username = username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
        }
        if(firstName.length() > 0) {
            firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        }
        if(lastName.length() > 0) {
            lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        }
    }

    @Override
    public void draw(Graphics2D g) {
        //draw Background
        g.clearRect(0,0, Main.WIDTH, Main.HEIGHT);
        g.drawImage(background, 0, 0, Main.WIDTH, Main.HEIGHT, null);

        g.drawImage(logo,Main.WIDTH/4-250, 50,null);

        g.setFont(Main.MAIN_FONT);

        drawTextField(editUsername,"Username",username,usernamePos,g);
        drawTextField(editPassword,"Password",passwordLength,passwordPos,g);
        drawTextField(editFirstName,"First Name",firstName,firstNamePos,g);
        drawTextField(editLastName,"Last Name",lastName, lastNamePos, g);
        drawTextField(editIban,"IBAN",iban,ibanPos,g);

        g.setColor(Color.RED);
        if(usernameLengthIncorrect) g.drawString("5 < 12",usernamePos.x+170, usernamePos.y );
        if(passwordLengthIncorrect) g.drawString("5 < 12",passwordPos.x+170, passwordPos.y );
        if(firstNameLengthIncorrect)g.drawString("3 < 24",firstNamePos.x+170,firstNamePos.y);
        if(lastNameLengthIncorrect) g.drawString("3 < 24",lastNamePos.x+170, lastNamePos.y );
        if(ibanLengthIncorrect)     g.drawString("!= 18" ,ibanPos.x+170,     ibanPos.y     );
        g.drawString(registerErrorMessage,usernamePos.x+230, usernamePos.y);

        g.setFont(new Font("Arial",Font.ITALIC,12));
        g.setColor(Color.WHITE);
        g.drawString(
                Main.BUILD,
                (int)( (Main.WIDTH - 10) - g.getFontMetrics().getStringBounds(Main.BUILD, g).getWidth()),
                (Main.HEIGHT-10)
        );
    }

    private void drawTextField(boolean hovered, String id, String text, Point position, Graphics2D g){
        if(hovered) g.setColor(Main.MAIN_COLOR);
        else g.setColor(Main.DISABLED_COLOR);
        g.drawImage(sign, position.x-40,position.y-40,350,90,null);
        g.drawString(id, position.x, position.y);
        g.drawString(text, position.x + 7, position.y + 34);
    }

    @Override
    public void keyPressed(int k) {
        if (k == VK_ESCAPE ) {
            gsc.setState(GameStateController.MENUSTATE);
        }

        if (k == KeyEvent.VK_ENTER) {
            // Validate false, give feedback...
            usernameLengthIncorrect = username.length() < 5 || username.length() > 12;
            passwordLengthIncorrect = password.length() < 5 || password.length() > 12;
            firstNameLengthIncorrect = firstName.length() < 3 || firstName.length() > 24;
            lastNameLengthIncorrect = lastName.length() < 3 || lastName.length() > 24;
            ibanLengthIncorrect = iban.length() != 18;

            if (usernameLengthIncorrect || passwordLengthIncorrect ||
                firstNameLengthIncorrect || lastNameLengthIncorrect ||
                ibanLengthIncorrect) { return; }

            GameServerSocket.getInstance().send("/registerMMOUser " + username + " " + firstName + " " + lastName + " " + iban + " " + password);
        }

        if( k == KeyEvent.VK_UP ) {
            if ( editPassword ) {
                editPassword = false; editUsername = true;
            } else if ( editFirstName ) {
                editFirstName = false; editPassword = true;
            } else if ( editLastName) {
                editLastName = false; editFirstName = true;
            } else if (editIban) {
                editIban = false; editLastName = true;
            }
        }
        if( k == KeyEvent.VK_DOWN) {
            if ( editUsername ){
                editUsername = false; editPassword = true;
            } else if ( editPassword ) {
                editPassword = false; editFirstName = true;
            } else if ( editFirstName ) {
                editFirstName = false; editLastName = true;
            } else if ( editLastName) {
                editLastName = false; editIban = true;
            }
        }

        // IF backspace is pressed and Username/Password has at least one Character.
        if (k == VK_BACK_SPACE) {
            if (editUsername && username.length()>0) username = username.substring(0, username.length()-1);
            if (editPassword && password.length()>0) {
                password = password.substring(0, password.length()-1);
                passwordLength = passwordLength.substring(0,passwordLength.length()-1);
            }
            if (editFirstName && firstName.length()>0) firstName = firstName.substring(0,firstName.length()-1);
            if (editLastName && lastName.length()>0) lastName = lastName.substring(0,lastName.length()-1);
            if (editIban && iban.length()>0) iban = iban.substring(0,iban.length()-1);

            // ELSE IF the value is within the Alphanumerics
        } else if (Arrays.binarySearch(alphanumerics, (char) k) > 0) {
            String c = String.valueOf(Character.toChars(k));

            if (editUsername && username.length() < 12) username = username.concat(c);
            if (editPassword && password.length() < 12) {
                password = password.concat(c);
                passwordLength = passwordLength.concat("*");
            }
            if (editFirstName && firstName.length() < 24) firstName = firstName.concat(c);
            if (editLastName && lastName.length() < 24) lastName = lastName.concat(c);
            if (editIban && iban.length() < 18) iban = iban.concat(c);

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
            if (editIban && iban.length() < 18) iban = iban.concat(String.valueOf(k - VK_NUMPAD0));
        }
    }

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void update(String data) {
        if(data.contains("/registerSuccessful")){
            gsc.setState(GameStateController.LOGINSTATE);
            GameServerConnectionRunnable.getInstance().unregister(this);
        }
        if(data.contains("/registerError")) {
            registerErrorMessage = data.split("'")[1];
        }
    }

}
