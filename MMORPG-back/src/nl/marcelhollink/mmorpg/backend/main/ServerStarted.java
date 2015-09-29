package nl.marcelhollink.mmorpg.backend.main;

import nl.marcelhollink.mmorpg.backend.server.MMOServer;

import java.util.logging.Level;

public class ServerStarted {
    public static void main(String[] args) {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.ALL);
        new MMOServer();
    }
}
