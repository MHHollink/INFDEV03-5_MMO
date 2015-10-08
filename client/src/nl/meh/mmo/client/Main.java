package nl.meh.mmo.client;


import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.frames.LauncherFrame;

import java.awt.*;

/**
 *
 * This is the startup point of the application.
 *
 * What is done here is :
 *  - Sets main settings like : loglevel, if server is local, title, width, ect...
 *  - Starts the launcher
 *
 * @author Marcel Hollink
 * @version 1.0.0.1
 * @since 2015, 10 october
 */
public class Main {

    // LOG LEVEL USED DURING RUNTIME
    public static final Logger.level LOGLEVEL = Logger.level.DEBUG;

    // SERVER IS LOCALLY, ONLY FOR TESTING!!!
    public static boolean LOCAL = true;

    // VALUES FOR THE JFrame AND JPanel
    public static final String TITLE = "EQUILIBRIUM";
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    public static double SCALE = 1.25;

    // FONT AND COLORS
    public static final Color MAIN_COLOR = new Color(207, 65, 16);
    public static final Color DISABLED_COLOR = new Color(41, 3, 9);
    public static final Font BOLD_FONT = new Font("Copperplate Gothic Bold", Font.PLAIN, 64);
    public static final Font MAIN_FONT = new Font("Copperplate Gothic Light", Font.PLAIN, 27);

    // CURRENT BUILD
    public static final String BUILD = "build 0.1-alpha";

    // SERVER ID AND SOCKET
    public static String serverID;

    // IP of the server where the player is playing on.
    public static final String SERVER_IP = "92.108.159.52";
    public static final int SERVER_PORT = 25565;

    /**
     * Entry point of application
     *
     * @param args arguments given via console startup
     */
    public static void main(String[] args) {
        WIDTH  = (int) (WIDTH/SCALE);  // SCALE WINDOW LITTLE BIT DOWN IN WIDTH
        HEIGHT = (int) (HEIGHT/SCALE); // SCALE WINDOW LITTLE BIT DOWN IN HEIGHT

        new LauncherFrame(); // CREATE THE FRAME FOR THE LAUNCHER
    }
}
