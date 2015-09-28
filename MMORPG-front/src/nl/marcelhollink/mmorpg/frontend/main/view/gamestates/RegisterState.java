package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import static java.awt.event.KeyEvent.*;

public class RegisterState extends GameState {

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
            usernamePos     = new Point((UI.WIDTH/2)+100,100),
            passwordPos     = new Point((UI.WIDTH/2)+100,200),
            firstNamePos    = new Point((UI.WIDTH/2)+100,300),
            lastNamePos     = new Point((UI.WIDTH/2)+100,400),
            ibanPos         = new Point((UI.WIDTH/2)+100,500),
            submitPos       = new Point((UI.WIDTH/2)+100,600);

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
        Logger.log(Logger.level.INFO, "RegisterState was initiated");

        il = new ImageLoader();
        background = il.getImage("/FantasyWorld3.jpg");
        sign = il.getImage("/signNoArrow.png");
        logo = il.getImage("/logo.png");

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
    }

    @Override
    public void update() {
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
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
        g.drawImage(background, 0, 0, UI.WIDTH, UI.HEIGHT, null);

        g.drawImage(logo,UI.WIDTH/4-250, 50,null);

        g.setFont(UI.font);

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
                UI.BUILD,
                (int)( (UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                (UI.HEIGHT-10)
        );
    }

    private void drawTextField(boolean hovered, String id, String text, Point position, Graphics2D g){
        if(hovered) g.setColor(UI.mainColor);
        else g.setColor(UI.disabledColor);
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

                                 //regiUser : - username, - first name, - last name, - iban, - password
            UI.clientSocket.send("/regiUser " + username + " " + firstName + " " + lastName + " " + iban + " " + password);
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
    public void receive(String data) {
        if(data.contains("/registerSccs")){
            gsc.setState(GameStateController.LOGINSTATE);
        }
        if(data.contains("/registerR-or")) {
            registerErrorMessage = data.split("'")[1];
        }
    }

}
