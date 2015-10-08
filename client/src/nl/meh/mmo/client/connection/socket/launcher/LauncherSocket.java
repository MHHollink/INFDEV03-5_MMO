package nl.meh.mmo.client.connection.socket.launcher;

import nl.meh.mmo.client.Main;
import nl.meh.mmo.client.connection.socket.game.GameServerConnectionRunnable;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.GameStateController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class LauncherSocket {

    private static LauncherSocket instance;

    private Socket server;
    private GameServerConnectionRunnable runnable;

    public static LauncherSocket getInstance() {
        if(instance==null){
            Logger.log(Logger.level.ERROR, "Tried to get GameServerSocket instance but did not exist");
            System.exit(-1);
        }
        return instance;
    }

    public static void createInstance(String ip, int port) {
        if(instance==null){
            instance = new LauncherSocket(ip,port);
            Logger.log(Logger.level.INFO, "Created GameServerSocket instance on ["+ip+":"+port+"]");
        } else {
            Logger.log(Logger.level.INFO, "Tried to create GameServerSocket instance but already exist, skipping...");
        }
    }

    private LauncherSocket(String ip, int port){
        try {
            if (Main.LOCAL) {
                server = new Socket(InetAddress.getLocalHost(), port);
                Logger.log(Logger.level.DEBUG, "Connected to local server");
            } else {
                server = new Socket(ip, port);
                Logger.log(Logger.level.DEBUG, "Connected to open server on ["+ip+":"+port+"]");
            }

            runnable = new GameServerConnectionRunnable(server);
            Logger.log(Logger.level.TRACE, "Created GameServerConnectionRunnable");
            new Thread(runnable).start();
            Logger.log(Logger.level.TRACE, "Started GameServerConnectionRunnable");
        }
        catch (IOException e) {
            Logger.log(Logger.level.ERROR, "Server connection time out");
            GameStateController.getInstance().setState(GameStateController.SERVEROFFLINESTATE);
        }
    }

    /**
     * Send a String to the Server via the GameServerConnectionRunnable
     * @param s commando string
     */
    public void send(String s) {
        runnable.send(s);
    }

    public Socket getServer() {
        return server;
    }

}
