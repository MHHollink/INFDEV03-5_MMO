package nl.meh.mmo.client.connection.socket.launcher;

import nl.meh.mmo.client.util.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * LauncherSocketCheck pokes a socket server and checks if the socket is open. It has a 1 second timeout.
 *
 * If no connection could be astablised within 1 second. the socketserver is seen as closed and a false is returned.
 * If a connection has been created in 1 second, the server is open an the a true is been returned.
 *      It also disconnects directly after connecting!
 */
public class LauncherSocketCheck {

    private static boolean serverAvailable;

    public static boolean isServerAvailable(String ip, int port){
        new LauncherSocketCheck(ip, port);
        Logger.log(Logger.level.DEBUG, String.valueOf(serverAvailable));
        return serverAvailable;
    }

    private LauncherSocketCheck(String ip, int port){
        try {

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), 1000);
            socket.close();

            Logger.log(Logger.level.DEBUG, "Server on ["+ip+":"+port+"] is open");

            serverAvailable = true;
        }
        catch (IOException e) {
            Logger.log(Logger.level.ERROR, "Server connection time out");

            serverAvailable = false;
        }
    }

}
