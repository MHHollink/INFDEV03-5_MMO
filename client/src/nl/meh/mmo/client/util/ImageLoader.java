package nl.meh.mmo.client.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class ImageLoader {

    public static final String FANTASY_WORLD_ONE    = "/FantasyWorld.jpg";
    public static final String FANTASY_WORLD_TWO    = "/FantasyWorld2.jpg";
    public static final String FANTASY_WORLD_TREE   = "/FantasyWorld3.jpg";
    public static final String LOGO                 = "/logo.png";
    public static final String ARROWED_SIGN         = "/sign.png";
    public static final String NO_ARROWED_SIGN      = "/signNoArrow.png";

    private static ImageLoader instance;

    public static ImageLoader getInstance() {
        if (instance == null){
            instance = new ImageLoader();
        }

        return instance;
    }

    public BufferedImage getImage(String path){
        try {
            return ImageIO.read(
                    getClass().getResourceAsStream(path)
            );
        }
        catch (Exception e){
            Logger.log(Logger.level.ERROR, "Loading image failed: ["+e.getMessage()+"]");
        }

        return null;
    }
}
