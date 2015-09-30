package nl.marcelhollink.mmorpg.frontend.main.connection;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import sun.rmi.runtime.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The ClientSocket is the instantiation class for the ServerConnection.
 *
 * The class will create a socket connection with the server and gives it to the ServerConnectionRunnable
 */
public class ClientSocket {

    private static ClientSocket instance;

    private Socket server;
    private ServerConnectionRunnable runnable;

    public static ClientSocket getInstance() {
        if(instance==null){
            Logger.log(Logger.level.INFO, "Tried to get ClientSocket instance but did not exist, creating one...");
            instance = new ClientSocket(UI.SERVER_IP,UI.SERVER_PORT);
            Logger.log(Logger.level.INFO, "Created ClientSocket instance");
        }
        return instance;
    }

    public static void createInstance() {
        if(instance==null){
            instance = new ClientSocket(UI.SERVER_IP,UI.SERVER_PORT);
            Logger.log(Logger.level.INFO, "Created ClientSocket instance");
        } else {
            Logger.log(Logger.level.INFO, "Tried to create ClientSocket instance but already exist, skipping...");
        }
    }

    private ClientSocket(String ip, int port){
        try {
            if (UI.LOCAL) {
                server = new Socket(InetAddress.getLocalHost(), port);
                Logger.log(Logger.level.DEBUG, "Connected to local server");
            } else {
                server = new Socket(ip, port);
                Logger.log(Logger.level.DEBUG, "Connected to open server on "+ip);
            }

            runnable = new ServerConnectionRunnable(server);
            Logger.log(Logger.level.TRACE, "Created ServerConnectionRunnable");
            new Thread(runnable).start();
            Logger.log(Logger.level.TRACE, "Started ServerConnectionRunnable");
        }
        catch (IOException e) {
            Logger.log(Logger.level.ERROR, "Server connection time out [" + e.getMessage() +"]");
            UI.getFrame().getPanel().getGsc().setState(GameStateController.SERVEROFFLINESTATE);
        }
    }

    /**
     * Send a String to the Server via the ServerConnectionRunnable
     * @param s commando string
     */
    public void send(String s) {
        runnable.send(s);
    }

    public Socket getServer() {
        return server;
    }
}
