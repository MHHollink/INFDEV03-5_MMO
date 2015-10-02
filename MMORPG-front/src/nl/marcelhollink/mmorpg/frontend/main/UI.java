package nl.marcelhollink.mmorpg.frontend.main;

import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.GameFrame;
import nl.marcelhollink.mmorpg.frontend.main.view.StringCenter;

import java.awt.*;

/**
 *
 * This class contains the psv main() for the project.
 * <p>
 *     The UI is initiated when the application is started. It wil directly create the GameFrame with a GamePanel in it.
 *
 *     Whilst loading the GameFrame a splash screen may appear.
 *     During this splash screen the ClientSocket will create a connection to the Server IP:Port.
 *     When the client receives a message back, the splash screen disappears and the user can Interact.
 *
 *     The GameStateController.currentState will be set to MenuState.
 *     This menu states holds 3 options : - LoginState, - RegisterState, - Exit.
 *
 *     These states hold their own information, methods and events.
 *
 * </p>
 *
 * <pre>
 *
 *  UI ---------------------------------> GameFrame
 *  |                                        |
 *  |                                        |
 *  # ---------> GameStateController         + --> GamePanel
 *  |                     |
 *  |                     + --> MenuState
 *  |                               |
 *  |                               + --> LoginState
 *  |                               |
 *  |                               + --> RegisterState
 *  |
 *  + ---------> ClientSocket ----------> ServerConnectionRunnable
 *
 *  </pre>
 *
 *  @author Marcel Hollink
 *  @since Sep 17, 2015
 *  @version 0.1-alpha
 */
public class UI {

    // LOG LEVEL USED DURING RUNTIME
    public static final Logger.level LOGLEVEL = Logger.level.DEBUG;

    // SERVER IS LOCALLY, ONLY FOR TESTING!!!
    public static final boolean LOCAL = true;

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

    public static final String SERVER_IP = "92.108.159.52";
    public static final int SERVER_PORT = 25565;

    // FRAME JFrame
    private static GameFrame frame;

    public static void main(String[] args) {
        ClientSocket.createInstance();

        WIDTH = (int) (WIDTH/SCALE);  // SCALE WINDOW LITTLE BIT DOWN IN WIDTH
        HEIGHT= (int) (HEIGHT/SCALE); // SCALE WINDOW LITTLE BIT DOWN IN HEIGHT

        frame = new GameFrame(); // CREATE THE FRAME
    }
}
