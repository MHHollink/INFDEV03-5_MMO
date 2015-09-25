package nl.marcelhollink.mmorpg.frontend.main.graphics;

import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;

/**
 * This class is used to help loading images into the game.
 */
public class ImageLoader {

    /**
     *
     * @param path the path in the recourse folder where the image is located
     * @return BufferImage gotten from the path
     * @throws Exception when path is incorrect
     */
    public BufferedImage getImage(String path){
        try {
            return ImageIO.read(
                    getClass().getResourceAsStream(path)
            );
        }
        catch (Exception e){
            Logger.log(Logger.level.ERROR, Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
