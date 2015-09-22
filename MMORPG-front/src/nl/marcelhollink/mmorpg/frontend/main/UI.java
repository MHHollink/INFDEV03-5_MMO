package nl.marcelhollink.mmorpg.frontend.main;

import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.view.GameFrame;

import java.awt.*;

public class UI {


    private static boolean debug = true;

    public static final String TITLE = "HRORPG";
    public static final Point TITLEPOS = new Point(100,100) ;
    public static int WIDTH = 1280;
    public static int HEIGHT = 720;
    public static double SCALE = 1.25;

    public static Color mainColor = new Color(255, 255, 52);
    public static Color disabledColor = new Color(70, 70, 16);
    public static Font titleFont = new Font("Copperplate Gothic Bold", Font.PLAIN, 64);
    public static Font font = new Font("Copperplate Gothic Light", Font.PLAIN, 27);

    public static final String BUILD = "build 0.1-alpha";

    public static String serverID;
    public static ClientSocket clientSocket;
    private static GameFrame frame;

    public static void main(String[] args) {
        clientSocket = new ClientSocket("hidden", 25565);

        WIDTH = (int) (WIDTH/SCALE);
        HEIGHT= (int) (HEIGHT/SCALE);

        frame = new GameFrame();
    }

    public static boolean isDebug() {
        return debug;
    }

    public static GameFrame getFrame() {
        return frame;
    }
}
