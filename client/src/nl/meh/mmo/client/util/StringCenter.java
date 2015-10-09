package nl.meh.mmo.client.util;

import nl.meh.mmo.client.Main;

import java.awt.*;

/**
 * StringCenter is used to center a string in the frame.
 */
public class StringCenter {

    public static int center(String str, Graphics2D g){
        int stringLen = (int)
                g.getFontMetrics().getStringBounds(str, g).getWidth();
        return Main.WIDTH / 2 - stringLen / 2;
    }

    private StringCenter(){} // No instances!
}
