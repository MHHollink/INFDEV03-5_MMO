package nl.marcelhollink.mmorpg.backend.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;

@SuppressWarnings("FieldCanBeLocal")
public class MMOServer {


    // 92.108.159.52:25565
    private static final String IP = "92.108.159.52";
    private static final int PORT = 25565;

    private static ServerSocket server;
    ArrayList<MMOClient> clients;

    public MMOServer() {
        boolean active = true;
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);

        try {
            server = new ServerSocket(PORT);
            clients = new ArrayList<>();

            Logger.log(Logger.level.INFO, "Server has started");

            //noinspection ConstantConditions
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
