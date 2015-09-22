package nl.marcelhollink.mmorpg.frontend.main.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

@SuppressWarnings("FieldCanBeLocal")
public class MMOServer {

    private static final int portNumber = 25565;
    public static final String PASSWORD = "6bdb81b4";

    private static ServerSocket server;
    ArrayList<MMOClient> clients;

    public MMOServer() {
        boolean active = true;
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);

        try {
            server = new ServerSocket(portNumber);
            clients = new ArrayList<MMOClient>();

            System.out.println("Server has started");

            while(active){
                Socket clientSocket = server.accept();
                MMOClient client = new MMOClient(this, clientSocket, clients.size());

                clients.add(client);
                new Thread(client).start();
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
