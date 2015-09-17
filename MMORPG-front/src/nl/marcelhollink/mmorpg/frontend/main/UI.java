package nl.marcelhollink.mmorpg.frontend.main;

import nl.marcelhollink.mmorpg.frontend.main.view.GameFrame;

public class UI {



    private final static boolean debug = true;

    public static final String TITLE = "MMORPG";
    public static int WIDTH = 1010;
    public static int HEIGHT = (int) ((9.0f/16.0f)*WIDTH);

    public static final String BUILD = "build 0.1-a1";

    public static void main(String[] args) {
        new GameFrame();
    }

    public static boolean isDebug() {
        return debug;
    }
}
