package nl.marcelhollink.mmorpg.frontend.main.connection;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.utils.L;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket {

    final int port;
    final String ip;

    Socket server;
    ServerConnectionRunnable runnable;

    public ClientSocket(String ip, int port){
        this.ip = ip;
        this.port = port;

        try {
            if(UI.isDebug()){
                server = new Socket(InetAddress.getLocalHost(), port);
            } else {
                server = new Socket(ip, port);
            }

            runnable = new ServerConnectionRunnable(server);
            new Thread(runnable).start();
        } catch (IOException e) {
            e.printStackTrace();
            L.log(L.level.ERROR, "Server connection time out");
            System.exit(-1);
        }
    }

    public void send(String s) {
        runnable.send(s);
    }

    public Socket getServer() {
        return server;
    }
}
