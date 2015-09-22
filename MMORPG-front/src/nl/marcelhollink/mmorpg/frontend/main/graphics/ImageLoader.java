package nl.marcelhollink.mmorpg.frontend.main.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

public class ImageLoader {

    public BufferedImage getImage(String path){
        try {
            return ImageIO.read(
                    getClass().getResourceAsStream(path)
            );
        }
        catch (Exception e){
            e.printStackTrace();
            System.exit(-1);
        }

        return null;
    }
}
