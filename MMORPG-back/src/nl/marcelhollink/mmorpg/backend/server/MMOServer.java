package nl.marcelhollink.mmorpg.backend.server;

import nl.marcelhollink.mmorpg.backend.server.database.model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

@SuppressWarnings("FieldCanBeLocal")
public class MMOServer {


    // 92.108.159.52:25565
    private static final String IP = "92.108.159.52";
    private static final int PORT = 25565;

    private static ServerSocket server;
    ArrayList<MMOClient> clients;

    SessionFactory sf;
    Configuration conf;

    private Scanner commandLineScanner;

    public MMOServer() {
        this.commandLineScanner = new Scanner(System.in);
        boolean active = true;
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.WARNING);

        this.conf = new Configuration();
        conf.configure();
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        this.sf = conf
                .addAnnotatedClass(nl.marcelhollink.mmorpg.backend.server.database.model.Character.class)
                .addAnnotatedClass(Server.class)
                .addAnnotatedClass(ServerContainsCharacter.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserOwnsCharacter.class)
                .buildSessionFactory(ssr);

        try {
            server = new ServerSocket(PORT);
            clients = new ArrayList<>();

            Logger.log(Logger.level.INFO, "Server has started");

            while(active){
                Socket clientSocket = server.accept();
                MMOClient client = new MMOClient(this, clientSocket, clients.size());

                clients.add(client);
                new Thread(client).start();

//                TODO : DOES NOT WORK
                if(commandLineScanner.hasNextLine()){
                    System.out.println("nexts");
                    String command = commandLineScanner.nextLine();

                    if(command.contains("stop")){
                        active = false;

                        clients.forEach(MMOClient::disconnect);
                    }
                }
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
