package nl.meh.mmo.server.server.database;

import nl.meh.mmo.server.server.Logger;
import nl.meh.mmo.server.server.database.model.Server;
import nl.meh.mmo.server.server.database.model.ServerContainsCharacter;
import nl.meh.mmo.server.server.database.model.User;
import nl.meh.mmo.server.server.database.model.UserOwnsCharacter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import nl.meh.mmo.server.server.database.*;

import java.util.logging.Level;

/**
 * This is a test class. It is used for testing the Server-DB connection and for peformance checks
 *
 * All methods are also in the MMOClient.class
 *
 */
public class Test {

    private SessionFactory sf;
    private Configuration conf;

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.ALL);

        this.conf = new Configuration();
        conf.configure();
        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();

        this.sf = conf
                .addAnnotatedClass(nl.meh.mmo.server.server.database.model.Character.class)
                .addAnnotatedClass(Server.class)
                .addAnnotatedClass(ServerContainsCharacter.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserOwnsCharacter.class)
                .buildSessionFactory(ssr);


//        Logger.log(Logger.level.WARN, "STARTED CREATION OF 10K USER BASE");
//        for (int i = 0; i < 10000; i++) {
//            Logger.log(Logger.level.INFO, "user {" + i + "}");
//            String username = UUID.randomUUID().toString().replaceAll("-", "").substring(0,11);
//            System.out.println(username);
//            registerUser(new String[]{"null", username, "TestUser", "TestUser", "TESTUSERBANKINGACC", "test"});
//        }

        sf.close();
    }

    private void registerChar(String[] args) {
        Session session = sf.openSession();
        session.beginTransaction();

        if (session.get(Character.class, args[2]) != null) {
            Logger.log(Logger.level.WARN, "tried to save an existing character!");
        } else {
            if (session.get(User.class, args[1]) != null) {
                Logger.log(Logger.level.INFO, "saving character");
                User user = session.get(User.class, args[1]);

                nl.meh.mmo.server.server.database.model.Character character = new nl.meh.mmo.server.server.database.model.Character();

                character.setCharacterName(args[2]);
                character.setGender(args[3]);
                character.setBalance(150);

                session.save(character);

                Logger.log(Logger.level.INFO, "creating user-<charracter relationship");
                UserOwnsCharacter uoc = new UserOwnsCharacter();

                uoc.setUsername(user);
                uoc.setCharacter(character);

                session.save(uoc);

                session.getTransaction().commit();
            } else {
                Logger.log(Logger.level.ERROR, 404, "username not found");
            }
        }
        session.close();
    }

    private void registerUser(String[] args) {

        Session session = sf.openSession();
        session.beginTransaction();

        if (session.get(User.class, args[1]) != null) {
            Logger.log(Logger.level.WARN, "tried to save an existing user!");
        } else {
            Logger.log(Logger.level.INFO, "started the creation of a user");

            User user = new User();
            user.setUsername(args[1]);
            user.setFName(args[2]);
            user.setLName(args[3]);
            user.setIban(args[4]);
            user.setPassword(args[5]);

            user.setBalance(0);
            user.setSlots(1);

            user.setLastPayment("NONE");
            user.setDaysLeft(0);

            session.save(user);
            session.getTransaction().commit();

            Logger.log(Logger.level.INFO, "saved a new user in the database");
        }

        session.close();
    }
}
