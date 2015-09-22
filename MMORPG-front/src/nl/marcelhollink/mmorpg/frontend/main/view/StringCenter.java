package nl.marcelhollink.mmorpg.frontend.main.view;

import nl.marcelhollink.mmorpg.frontend.main.UI;

import java.awt.*;


public class StringCenter {

    public static int center(String str, Graphics2D g){
        int stringLen = (int)
                g.getFontMetrics().getStringBounds(str, g).getWidth();
        return UI.WIDTH / 2 - stringLen / 2;
    }
}
