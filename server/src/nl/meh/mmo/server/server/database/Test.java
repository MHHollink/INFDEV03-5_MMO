package nl.meh.mmo.server.server.database;

import nl.meh.mmo.server.server.Logger;
import nl.meh.mmo.server.server.database.model.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import sun.rmi.runtime.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;
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

    ArrayList<String> userList;
    ArrayList<String> serverList;

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
                .addAnnotatedClass(CharacterSkills.class)
                .buildSessionFactory(ssr);

        userList = new ArrayList<>();
        serverList = new ArrayList<>();

        //runTestSetupScenaro();
        runTestScenario();

        sf.close();
    }

    private String timerFormat(long millis){
        long second = (millis / 1000) % 60;
        long minute = (millis / (1000 * 60)) % 60;
        long hour = (millis / (1000 * 60 * 60)) % 24;

        return String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
    }

    private void runTestSetupScenaro() {
        long end;
        long start;

        /**
         * SERVERS
         */
        Logger.log(Logger.level.WARN, "STARTED CREATION OF 10K USER BASE");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            Logger.log(Logger.level.INFO, "server {" + i + "}");
            String ip = UUID.randomUUID().toString().replaceAll("-", "").substring(0,11);
            registerServer(ip);

            serverList.add(ip);
        }
        end = System.currentTimeMillis();
        Logger.log(Logger.level.INFO, "ENDED CREATION OF SERVERS");
        String creationTimeServer = timerFormat(end - start);
        Logger.log(Logger.level.INFO, "TOTAL CREATION TOOK ["+creationTimeServer+"]");

        try { Thread.sleep(7500); } catch (InterruptedException ignored) { }

        /**
         * USERS
         */
        Logger.log(Logger.level.WARN, "STARTED CREATION OF 10K USER BASE");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        start = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            Logger.log(Logger.level.INFO, "user {" + i + "}");
            String username = UUID.randomUUID().toString().replaceAll("-", "").substring(0,11);
            registerUser(new String[]{"null", username, "TestUser", "TestUser", "TESTUSERBANKINGACC", "test"}, username);
            userList.add(username);
        }
        end = System.currentTimeMillis();
        Logger.log(Logger.level.INFO, "ENDED CREATION OF USER BASE");
        String creationTimeUser = timerFormat(end - start);
        Logger.log(Logger.level.INFO, "TOTAL CREATION TOOK ["+creationTimeUser+"]");

        try { Thread.sleep(7500); } catch (InterruptedException ignored) { }

        /**
         * CHARACTERS
         */
        Logger.log(Logger.level.WARN, "STARTED CREATION OF 10K+ CHARACTERS");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        start = System.currentTimeMillis();
        for (int i = 0; i < userList.size(); i++) {
            Logger.log(Logger.level.INFO, "user {" + i + "}");
            String characterName = UUID.randomUUID().toString().replaceAll("-", "").substring(0,11);

            String gender = "";
            if(new Random().nextBoolean()) gender = "fe";
            gender +="male";

            registerCharacter(new String[]{"null", userList.get(i), characterName, gender}, userList.get(i));
            if(new Random().nextInt(9) > 8 && i!=0) i--;
        }
        end = System.currentTimeMillis();
        Logger.log(Logger.level.INFO, "ENDED CREATION OF CHARACTERS");
        String creationTimeCharacter = timerFormat(end - start);
        Logger.log(Logger.level.INFO, "TOTAL CREATION TOOK ["+creationTimeCharacter+"]");

        try { Thread.sleep(7500); } catch (InterruptedException ignored) { }


    }

    private void runTestScenario() {
        long end;
        long start;
        /**
         * CHECK OF EACH SERVER HOW MANY PLAYS THEY HAVE
         */
        Session session = sf.openSession();
        session.beginTransaction();
        Logger.log(Logger.level.WARN, "STARTED REQUESTING AMOUNT OF USERS ON A SERVER");
        try { Thread.sleep(1000); } catch (InterruptedException ignored) { }

        start = System.currentTimeMillis();
        ArrayList<Server> servers = (ArrayList<Server>) session.createCriteria(Server.class).list();
        for (Server server : servers) {
            int result = (int)((long) session.createQuery("select count(*) from ServerContainsCharacter where address = '" + server.getAddress() +"'").uniqueResult());
            Logger.log(Logger.level.INFO, server.getAddress() +" has "+result+" amount of characters");
        }
        end = System.currentTimeMillis();
        Logger.log(Logger.level.INFO, "TOTAL REQUESTING TOOK ["+timerFormat(end - start)+"]");
        try { Thread.sleep(7500); } catch (InterruptedException ignored) { }
    }

    private void registerServer(String ip) {
        Session session = sf.openSession();
        session.beginTransaction();

        Server server = new Server(ip, ip, "nowhere", 100, 0, false);

        session.save(server);
        session.getTransaction().commit();
        session.close();
        Logger.log(Logger.level.INFO,"Created a server in the db.");
    }

    /**
     * The method that controls the buying of more playable days for a user.
     *
     * outputs successful or error, depending on the players balance
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/shop}
     *          [1] - string {days}
     *          [2] - int {ie. 1}
     */
    private void buyMoreCharacterSlots(String[] args, String clientPrefix){
        Session session = sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        if(user.getBalance() > 6 * Integer.parseInt(args[2])){
            user.setSlots(user.getSlots() + Integer.parseInt(args[2]));
            session.update(user);
            session.getTransaction().commit();
            //output.println("/shopSuccessful");
        } else {
            //output.println("/shopError notEnoughMoney");
        }

        session.close();
    }

    /**
     * The method that gets the characters data requested for the user.
     *
     * outputs [if users has +1 amount of characters] each character and his details *name gender levels, ect.
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/request}
     *          [1] - string {charactersFor}
     *          [2] - string {'username'}
     */
    private void requestCharacterDetails(String[] args, String clientPrefix) {
        Logger.log(Logger.level.INFO, clientPrefix + " requested user details for user [" + args[2] + "]");

        Session session = sf.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from UserOwnsCharacter where user_username = :username");
        query.setParameter("username", clientPrefix);

        //noinspection unchecked
        ArrayList<UserOwnsCharacter> userOwnsCharacters = (ArrayList<UserOwnsCharacter>) query.list();
        for (UserOwnsCharacter uoc : userOwnsCharacters) {
            nl.meh.mmo.server.server.database.model.Character character = session.get(nl.meh.mmo.server.server.database.model.Character.class, uoc.getCharacter().getCharacterName());
            CharacterSkills skills = session.get(CharacterSkills.class, character.getCharacterName());

            String response = "/characterDetails ";

            response = response.concat(character.getCharacterName()+" ");
            response = response.concat(character.getGender()+" ");
            response = response.concat(character.getBalance()+" ");

            response = response.concat(skills.getAttack()+" ");
            response = response.concat(skills.getDefence()+" ");
            response = response.concat(skills.getStrength()+" ");
            response = response.concat(skills.getHitpoints()+" ");
            response = response.concat(skills.getMining()+" ");
            response = response.concat(skills.getWoodcutting()+" ");
            response = response.concat(skills.getSmithing()+" ");
            response = response.concat(skills.getBartering()+" ");

            //output.println(response);
            Logger.log(Logger.level.DEBUG, "Send to " + clientPrefix + " : " + response);
        }
    }

    /**
     * Updates the balance of the user
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/shop}
     *          [1] - string {money}
     *          [2] - string {ie. 20}
     * @return
     *      the command that has to be send back to the user
     */
    private String updateBalance(String[] args, String clientPrefix) {
        String response = "/shopSuccessful ";

        Session session = sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        user.setBalance(user.getBalance()+Double.parseDouble(args[2]));

        session.save(user);
        session.getTransaction().commit();
        session.close();

        response += "updated user balance with "+args[2];

        return response;
    }

    /**
     * The method that gets the userdata for the requested user.
     *
     * @param clientPrefix
     *          usermame of the requested user
     * @return
     *          returns the command that has to be send to the user
     */
    private String requestUserDetail(String clientPrefix) {
        Logger.log(Logger.level.INFO, clientPrefix + " requested user details for user [" + clientPrefix + "]");
        String response = "/userDetails ";

        Session session = sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);

        response = response.concat(user.getUsername()+" ");
        response = response.concat(user.getFName()+" ");
        response = response.concat(user.getLName()+" ");
        response = response.concat(user.getBalance()+" ");
        response = response.concat(user.getLastPayment()+" ");
        response = response.concat(user.getDaysLeft()+" ");
        response = response.concat(user.getSlots()+" ");
        response = response.concat(user.getIban()+" ");

        session.close();
        return response;
    }

    /**
     * The method that is used for creating a new user record in the DB
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/registerUser}
     *          [1] - string {username}
     *          [2] - string {first name}
     *          [3] - string {last name}
     *          [4] - string {iban}
     *          [5] - string {password}
     *
     * @return
     *          The string with the success or error message that has to be send to the user.
     */
    private String registerUser(String[] args, String clientPrefix) {
        String response = "/register";

        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            Logger.log(Logger.level.INFO,clientPrefix+" tried to save an existing user!");

            response += "Error 'Username Already exists";
        } else {
            Logger.log(Logger.level.INFO, clientPrefix + " started the creation of a user");

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

            Logger.log(Logger.level.INFO, clientPrefix + " saved a new user in the database");
            response += "Successful";
        }

        session.close();
        return response;
    }

    /**
     * The method that is used for creating a new character record in the DB
     * It also creates a record in the userOwnsCharacter table and deletes one of the users free slots
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/registerCharacter}
     *          [1] - string {username}
     *          [2] - string {character name}
     *          [3] - string {gender of the character}
     *
     * @return
     *          The string with the success or error message that has to be send to the user.
     */
    private String registerCharacter(String[] args, String clientPrefix) {
        String response = "/createCharacter ";

        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(nl.meh.mmo.server.server.database.model.Character.class, args[2])!=null){
            Logger.log(Logger.level.WARN, clientPrefix + " tried to save over an existing character!");
            response += "error alreadyExists";
        } else {
            if(session.get(User.class, args[1])!=null){
                User user = session.get(User.class, args[1]);

                if(user.getSlots() < 1) {
                    response += "error noSlotsAvailable";
                }

                Logger.log(Logger.level.INFO, clientPrefix + " saving character");
                nl.meh.mmo.server.server.database.model.Character character = new nl.meh.mmo.server.server.database.model.Character();

                character.setCharacterName(args[2]);
                character.setGender(args[3]);
                character.setBalance(150);

                session.save(character);

                Logger.log(Logger.level.INFO, clientPrefix + " creating user --|< charracter relationship");
                UserOwnsCharacter uoc = new UserOwnsCharacter();

                uoc.setUsername(user);
                uoc.setCharacter(character);

                session.save(uoc);

                ServerContainsCharacter scc = new ServerContainsCharacter();
                Server address = session.get(Server.class, serverList.get(new Random().nextInt(serverList.size())));

                scc.setAddress(address);
                scc.setCharacter(character);

                session.save(scc);

                Logger.log(Logger.level.INFO, clientPrefix + " creating character_skills entry in database");
                CharacterSkills cs = new CharacterSkills(
                        character,
                        7, // total of all levels
                        3, // total of combat levels (attack, defence, strength
                        4, // total of all skill levels (others...)
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1,
                        1
                );
                session.save(cs);

                Logger.log(Logger.level.INFO, "removing one free characterSlot from the users");
                user.setSlots(user.getSlots() - 1);
                session.update(user);

                session.getTransaction().commit();
                response += "successful";
            } else {
                Logger.log(Logger.level.ERROR, 404, "username not found");
            }
        }
        session.close();
        return response;
    }

    /**
     * Login method that handles the user login
     * checks if user exists and of password is correct
     *
     * the method replaces the clientprefix as 'client 0' to the users username if login is successful
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/login}
     *          [1] - string {username}
     *          [2] - string {password}

     * @return
     */
    private String login(String[] args, String clientPrefix) {
        String response = "/login";

        Logger.log(Logger.level.INFO, clientPrefix + " tries to login");

        Session session = sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            User user = session.get(User.class, args[1]);

            if (user.getPassword().equals(args[2])){
                response += "Successful";
                Logger.log(Logger.level.INFO, clientPrefix + " logged in as " + args[1]);
                Logger.log(Logger.level.INFO, clientPrefix + " is now known as " + args[1]);
                clientPrefix = args[1];
            } else {
                Logger.log(Logger.level.INFO, clientPrefix + " logging in with wrong identification");
                response += "Fail incorrect";
            }
        } else {
            Logger.log(Logger.level.INFO, clientPrefix + " username was not found when logging in");
            response += "Fail notFound";
        }

        session.close();
        return response;
    }

    /**
     * The method that controls the buying of more playable days for a user.
     *
     * outputs successful or error, depending on the players balance
     *
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/shop}
     *          [1] - string {days}
     *          [2] - int {1 or 2 or 3 or 12}
     */
    private String buyMoreDays(String[] args, String clientPrefix){
        int months = Integer.parseInt(args[2]);

        Logger.log(Logger.level.INFO, clientPrefix + " is trying to buy " + months + " more months");

        Session session = sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        double balance = user.getBalance();

        switch (months) {
            case 1:
                // One more months
                if ( balance - 5 > 0) {
                    // payment successful
                    return paymentSuccess(user, balance, 5.0, 31, session);
                }
                return "/shopError notEnoughMoney "+(balance-5);
            case 2:
                // Two more months
                if ( balance - 8 > 0) {
                    // payment successful
                    return paymentSuccess(user, balance, 8.0, 61, session);
                }
                return "/shopError notEnoughMoney "+(balance-8);
            case 3:
                // Three more months
                if ( balance - 10 > 0) {
                    // payment successful
                    return paymentSuccess(user, balance, 10.0, 92, session);
                }
                return "/shopError notEnoughMoney "+(balance-10);
            case 12:
                // One whole year
                if ( balance - 35 > 0) {
                    // payment successful
                    return paymentSuccess(user,balance,35.0,365,session);
                }
                return "/shopError notEnoughMoney "+(balance-35);
        }

        // This should never happen!!
        return "/shopError 'not the right amounts of months was entered'";
    }

    /**
     * This function updates the users data in the database table
     *
     * @param user username
     * @param balance current balance
     * @param payed the payed amount of cash
     * @param days the amount of days purchased
     * @param session the database transaction session
     * @return
     *      the success string with the amount of days
     */
    private String paymentSuccess(User user, double balance, double payed, int days, Session session){
        user.setBalance(balance - payed);
        user.setDaysLeft(user.getDaysLeft() + days);
        session.update(user);
        session.getTransaction().commit();
        session.close();
        Logger.log(Logger.level.INFO, user.getUsername() + " bought a " + days + "days subscription");
        return "/shopSuccessful "+days;
    }
    }
