package nl.marcelhollink.mmorpg.frontend.main.view.gamestates;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.graphics.ImageLoader;

import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private int currentChoice = 0;
    private String[] options = {
            "Login / Register",
            "Quit"
    };


    public static Color titleColor;
    public static Font titleFont;
    public static Font font;

    ImageLoader il;
    private Image background;

    public MenuState(GameStateController gsc) {
        this.gsc = gsc;

        titleColor = new Color(255, 71, 38);
        titleFont = new Font("Copperplate Gothic Bold", Font.PLAIN, 64);

        font = new Font("Copperplate Gothic Light", Font.PLAIN, 27);
    }

    @Override
    public void init() {
        il = new ImageLoader();

        background = il.getImage("/main.png");
    }

    @Override
    public void update() {  }

    @Override
    public void draw(Graphics2D g) {
        //draw Background
        g.clearRect(0,0, UI.WIDTH, UI.HEIGHT);
        g.drawImage(background, 0, 0, UI.WIDTH, UI.HEIGHT, null);

        // draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        int stringLen = (int)
                g.getFontMetrics().getStringBounds(UI.TITLE, g).getWidth();
        int start = UI.WIDTH/2 - stringLen/2;
        g.drawString(UI.TITLE, start, 150);
        for (int i = 0; i < 4; i++) {
            g.drawLine(start,160+i,UI.WIDTH-start,160+i);
        }

        // draw credits
        g.setFont(new Font("Arial", Font.PLAIN, 21));
        int creditStringLenght = (int)
                g.getFontMetrics().getStringBounds("by Marcel Hollink", g).getWidth();
        g.drawString("by Marcel Hollink", start+stringLen-creditStringLenght,190);

        // draw menu options
        g.setColor(new Color(70, 22, 17, 147));
        g.fillRoundRect(UI.WIDTH / 3, 300, UI.WIDTH / 3, options.length * 40, 25, 25);
        g.setColor(new Color(255, 71, 38));
        g.drawRoundRect(UI.WIDTH / 3, 300, UI.WIDTH / 3, options.length * 40, 25, 25);
        g.drawRoundRect(UI.WIDTH / 3 -1, 300 -1, UI.WIDTH / 3, options.length * 40, 25, 25);

        g.setFont(font);
        for (int i = 0; i < options.length; i++) {
            if(i == currentChoice) {
                g.setColor(new Color(255, 71, 38));
            }
            else {
                g.setColor(new Color(70, 22, 17));
            }
            stringLen = (int)
                    g.getFontMetrics().getStringBounds(options[i], g).getWidth();
            start = UI.WIDTH / 2 - stringLen / 2;
            g.drawString(options[i], start, 330 + i*38);
        }

        g.setFont(new Font("Arial",Font.ITALIC,12));
        g.setColor(Color.WHITE);
        g.drawString(
                UI.BUILD,
                (int)( (UI.WIDTH - 10) - g.getFontMetrics().getStringBounds(UI.BUILD, g).getWidth()),
                (UI.HEIGHT-10)
        );
    }

    private void select(){
        if(currentChoice == 0){
            // Start
            gsc.setState(GameStateController.LOGINSTATE);
        }
        if(currentChoice == 1){
            System.exit(0);
        }
    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ENTER){
            select();
        }

        if(k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1){
                currentChoice = options.length-1;
            }
        }
        if(k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == options.length){
                currentChoice = 0;
            }
        }
        if(k == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(int k) {    }
}
