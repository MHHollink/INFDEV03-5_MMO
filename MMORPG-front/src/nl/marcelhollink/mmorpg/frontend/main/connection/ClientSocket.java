package nl.marcelhollink.mmorpg.frontend.main.connection;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * The ClientSocket is the instantiation class for the ServerConnection.
 *
 * The class will create a socket connection with the server and gives it to the ServerConnectionRunnable
 */
public class ClientSocket {

    final int port;
    final String ip;

    Socket server;
    ServerConnectionRunnable runnable;

    public ClientSocket(String ip, int port){
        this.ip = ip;
        this.port = port;

        try {
            if (UI.LOCAL) {
                server = new Socket(InetAddress.getLocalHost(), port);
            } else {
                server = new Socket(ip, port);
            }

            runnable = new ServerConnectionRunnable(server);
            new Thread(runnable).start();
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
