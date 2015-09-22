package nl.marcelhollink.mmorpg.frontend.main.server;

import nl.marcelhollink.mmorpg.frontend.main.server.database.model.*;
import nl.marcelhollink.mmorpg.frontend.main.server.database.model.Character;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

@SuppressWarnings({"UnnecessaryLocalVariable", "FieldCanBeLocal"})
public class MMOClient implements Runnable{

    private final int connectionID;

    private final MMOServer server;

    private Socket clientSocket;
    private boolean active = true;

    private Scanner input;
    private PrintWriter output;

    SessionFactory sf;
    Configuration conf;

    private String clientPrefix;

    public MMOClient(MMOServer server, Socket clientSocket, int connectionID) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.connectionID = connectionID;

        this.clientPrefix = "Client "+connectionID+" ";

        this.conf = new Configuration();
        conf.configure();
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        this.sf = conf
                .addAnnotatedClass(Character.class)
                .addAnnotatedClass(Server.class)
                .addAnnotatedClass(ServerContainsCharacter.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserOwnsCharacter.class)
                    .buildSessionFactory(ssr);

        System.out.println(clientPrefix +"created...");
    }

    @Override
    public void run() {
        try{
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (active) {
                if (input.hasNextLine()) {
                    String data = input.nextLine();
                    String[] args = data.split(" ");
                    if(data.contains("/connect")){
                        System.out.println(clientPrefix +"connected...");
                        output.println("/serverID Netherlands-NL01");
                        output.println("/currentlyOnline you are currently online with "+server.clients.size()+"others!");
                        output.println("/exitSplash");
                    }

                    if(data.contains("/disconnectMeFromMMORPGServer")) {
                        this.clientSocket.close();
                        active = false;
                    }

                    if(data.contains("/regiUser")){
                        output.println(registerUser(args));
                    }
                    if(data.contains("/regiChar")){
                        output.println(registerCharacter(args));
                    }
                    if(data.contains("/attemptedLogin")){
                        output.println(login(args));
                    }
                }

            }
            output.close();

        }catch (Exception e){
            active = false;
            System.out.println(clientPrefix +"had en error!");
        }
        System.out.println(clientPrefix +"has disconnected");
        server.clients.remove(this);
    }

    private String registerUser(String[] args) {
        String response = "";

        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            System.out.println(clientPrefix+"tried to save an existing user!");

            response = "/registerR-or 'Username Already exists";
        } else {
            System.out.println(clientPrefix+"started the creation of a user");

            User user = new User();
            user.setUsername(args[1]);
            user.setFName(args[2]);
            user.setLName(args[3]);
            user.setIban(args[4]);
            user.setPassword(args[5]);

            user.setBalance(0);
            user.setSlots(1);

            user.setLastPayment("");
            user.setPayedUntil("");

            session.save(user);
            session.getTransaction().commit();

            System.out.println(clientPrefix+"saved a new user in the database");
            response = "/registerSccs";
        }
        return response;
    }

    private String registerCharacter(String[] args) {
        String response = "";

        // TODO : REGISTER USERS

        return response;
    }

    private String login(String[] args) {
        String response = "";

        System.out.println(clientPrefix+"tries to login");

        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            User user = session.get(User.class, args[1]);
            if (user.getPassword().equals(args[2])){
                response = "/loginSccs";
                System.out.println(clientPrefix+"logged in as "+args[1]);
                System.out.println(clientPrefix+"is now known as "+args[1]);
                clientPrefix = args[1]+": ";
            } else {
                System.out.println(clientPrefix+"logging in with wrong identification");
                response = "/loginFail incorrect";
            }
        } else {
            System.out.println(clientPrefix+"username was not found when logging in");
            response = "/loginFail notFound";
        }

        return response;
    }

}
