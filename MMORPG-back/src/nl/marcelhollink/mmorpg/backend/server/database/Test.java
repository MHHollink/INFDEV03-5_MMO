package nl.marcelhollink.mmorpg.backend.server.database;

import nl.marcelhollink.mmorpg.backend.server.Logger;
import nl.marcelhollink.mmorpg.backend.server.database.model.*;
import nl.marcelhollink.mmorpg.backend.server.database.model.Character;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;

/**
 * Created by Marcel on 26-9-2015.
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
                .addAnnotatedClass(nl.marcelhollink.mmorpg.backend.server.database.model.Character.class)
                .addAnnotatedClass(Server.class)
                .addAnnotatedClass(ServerContainsCharacter.class)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(UserOwnsCharacter.class)
                .buildSessionFactory(ssr);

        registerUser(new String[]{"null", "Evestar01", "Marcel", "Hollink", "NL01INGB123412", "test"});
        registerChar(new String[]{"null", "Mjollnir1994", "Qulan", "Male"});
        registerChar(new String[]{"null", "Mjollnir1994", "Eve1994", "Female"});
        registerChar(new String[]{"null", "Evestar01", "Evestar01", "Female"});
    }

    private void registerChar(String[] args) {
        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(Character.class, args[2])!=null){
            Logger.log(Logger.level.WARN, "tried to save an existing character!");
        } else {
            if(session.get(User.class, args[1])!=null){
                Logger.log(Logger.level.INFO, "saving character");
                User user = session.get(User.class, args[1]);

                Character character = new Character();

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

        if(session.get(User.class, args[1])!=null){
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
