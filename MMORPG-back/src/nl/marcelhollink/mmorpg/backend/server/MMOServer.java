package nl.marcelhollink.mmorpg.backend.server;

import nl.marcelhollink.mmorpg.backend.server.database.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
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
    final boolean[] active = {true};

    public MMOServer() {
        this.commandLineScanner = new Scanner(System.in);



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

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (active[0]) {
                    String command = commandLineScanner.nextLine();

                    if(command.contains("stop")) {
                        Logger.log(Logger.level.INFO,"Server is stopping...");

                        clients.forEach(MMOClient::disconnect);

                        Logger.log(Logger.level.INFO, "Saving server state");
                        setServerActiveState(IP+":"+PORT,false);

                        try {
                            server.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        active[0] = false;
                        System.exit(0);

                    }
                }
            }
        }).start();


        try {
            server = new ServerSocket(PORT);
            clients = new ArrayList<>();

            Logger.log(Logger.level.INFO, "Server has started");
            setServerActiveState(IP+":"+PORT,true);

            while(active[0]){
                Socket clientSocket = server.accept();
                MMOClient client = new MMOClient(this, clientSocket, clients.size());

                clients.add(client);
                new Thread(client).start();
            }
        }
        catch (IOException e) {
            Logger.log(Logger.level.WARN, "Server stopped! "+e.getMessage());
        }
    }

    public void setServerActiveState(String serverIp, boolean state) {
        Logger.log(Logger.level.INFO, "Trying to set server activeState to "+state);
        Session session = sf.openSession();
        session.beginTransaction();

        Server openingServer = session.get(Server.class, serverIp);
        openingServer.setActive(state);
        session.save(openingServer);
        session.getTransaction().commit();
        session.close();
        Logger.log(Logger.level.DEBUG, "Server activeState is now "+state);
    }
}
