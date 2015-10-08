package nl.meh.mmo.server.main;

import nl.meh.mmo.server.server.MMOServer;

import java.util.logging.Level;

/**
 *
 * This is the startup point of the application.
 *
 * What is done here is :
 *  - Set hibernate logging to severe
 *  - Create a new  MMOServer
 *
 * @author Marcel Hollink
 * @version 1.0.0.1
 * @since 2015, 10 october
 */
public class Main {
    public static void main(String[] args) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        new MMOServer();
    }
}
