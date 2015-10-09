package nl.meh.mmo.server.server;

import nl.meh.mmo.server.server.database.model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This is the MMOServer it self.
 *
 * It contains vital information about the server, its state and the connected clients.
 * Hibernate connection is made in this class and all sessions should start from this connection
 *
 * The class has 1 runnable running async of the main thread.
 *
 * This runnable catches if the user types stop in the command line.
 *      When this is done, all clients get the message that the server has been closed
 *      Then the main thread will be exited.
 *
 * The Main thread runs in a while active loop, accepting new clients and starting their own client thread.
 *
 */
public class MMOServer {

    // 92.108.159.52:25565
    private static String IP = "127.0.0.1"; // DEFAULT TO LOCALHOST
    private static int PORT = 25565;        // ALWAYS BIND TO PORT 25565

    // Server socket and connected clients arrayList
    private static ServerSocket server;
    private ArrayList<MMOClient> clients;

    // Hibernate configuration and session factory
    SessionFactory sf;
    Configuration conf;

    // Scanner to catch console input and the active state of the main thread.
    private Scanner commandLineScanner;
    final boolean[] active = {true};

    public MMOServer() {
        try {
            IP = getIp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.commandLineScanner = new Scanner(System.in);

        this.conf = new Configuration();
        conf.configure();
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        this.sf = conf
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(nl.meh.mmo.server.server.database.model.Character.class)
                .addAnnotatedClass(Server.class)
                .addAnnotatedClass(ServerContainsCharacter.class)
                .addAnnotatedClass(UserOwnsCharacter.class)
                .addAnnotatedClass(CharacterSkills.class)
                .buildSessionFactory(ssr);

        // Async thread holding the console scanner, trying to catch 'stop' in each loop.
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (active[0]) {
                    String command = commandLineScanner.nextLine();

                    if(command.contains("stop")) {
                        Logger.log(Logger.level.INFO, "Server is stopping...");

                        for (MMOClient client : clients) {
                            client.disconnect();
                        }

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
            Logger.log(Logger.level.INFO, "Players can now join on IP: "+IP+":"+PORT);
            setServerActiveState(IP + ":" + PORT, true);

            // main thread loop
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

    /**
     * When the server is started in the main thread, the database receives an update about the activity of the server.
     * The Method also handles the shutdown of the server. Setting its active state in the DB to false.
     *
     * @param serverIp
     *          What ip is used in this server, this parameter should be formatted as [IP:PORT]
     * @param state
     *          What is the new state of the server, active = true / disabled = false
     */
    public void setServerActiveState(String serverIp, boolean state) {
        Logger.log(Logger.level.INFO, "Trying to set server activeState to " + state);
        Session session = sf.openSession();
        session.beginTransaction();

        Server server = session.get(Server.class, serverIp);
        if(server == null) {
            Logger.log(Logger.level.WARN,"Server did not exist, creating one...");
            Logger.log(Logger.level.WARN,"Please make sure to configure the server in the database!");
            server = new Server(serverIp, "", "", 0, 0, state);

        }

        server.setActive(state);
        session.save(server);
        session.getTransaction().commit();
        session.close();
        Logger.log(Logger.level.DEBUG, "Server activeState is now " + state);
    }

    public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            return in.readLine();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<MMOClient> getClients() {
        return clients;
    }
}
