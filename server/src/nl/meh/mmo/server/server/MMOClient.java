package nl.meh.mmo.server.server;

import nl.meh.mmo.server.server.database.model.CharacterSkills;
import nl.meh.mmo.server.server.database.model.User;
import nl.meh.mmo.server.server.database.model.UserOwnsCharacter;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * MMOClient is the class responsible for all the handling of incoming and outgoing commands from a single client/user
 */
public class MMOClient implements Runnable{

    // Server properties
    private final MMOServer server;
    private Socket clientSocket;
    private boolean active = true;
    private Scanner input;
    private PrintWriter output;

    // Client ID;
    private String clientPrefix;

    /**
     * MMOClient constructor, constructs a single client instance for each individual client
     *
     * @param server
     *          Server object, containing information about the server and its database
     * @param clientSocket
     *          Client socket, hold the information about the created client socket for input and output stream
     * @param connectionID
     *          The client number, used as indicator to see how many clients are connected,
     *          and which spot in the clients array the user is saved
     */
    public MMOClient(MMOServer server, Socket clientSocket, int connectionID) {
        this.server = server;
        this.clientSocket = clientSocket;

        this.clientPrefix = "Client "+connectionID+" ";

        Logger.log(Logger.level.INFO, clientPrefix + " created...");
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
                        Logger.log(Logger.level.INFO, clientPrefix + " connected...");
                        output.println("/mainServerID Netherlands-NL01");
                        output.println("/mainCurrentlyOnline "+server.getClients().size());
                        output.println("/mainExitSplash");
                    }

                    if(data.contains("/disconnectMeFromMMORPGServer")) {
                        this.clientSocket.close();
                        active = false;
                    }

                    if(data.contains("/registerMMOUser")){
                        output.println(registerUser(args));
                    }
                    if(data.contains("/registerMMOCharacter")){
                        output.println(registerCharacter(args));
                    }
                    if(data.contains("/attemptedLogin")){
                        output.println(login(args));
                    }
                    if(data.contains("/request")){
                        if(args[1].equals("userDetails")){
                            output.println(requestUserDetail(args));
                            Logger.log(Logger.level.INFO,clientPrefix+" received user details");
                        }
                        if(args[1].equals("charactersFor")) {
                            requestCharacterDetails(args);
                            Logger.log(Logger.level.INFO,clientPrefix+" received character details");
                        }
                    }
                    if(data.contains("/shop")){
                        if(args[1].equals("days")){
                            buyMoreDays(args);
                        }
                        if(args[1].equals("money")){
                            updateBalance(args);
                        }
                        if(args[1].equals("characters")){
                            buyMoreCharacterSlots(args);
                        }

                    }
                    if(data.contains("/UserCheatCodePlusTwentyBalanceInsert")){
                        Logger.log(Logger.level.WARN,clientPrefix+" has cheated!");
                        updateBalance(new String[]{"/shop", "money", "20"});
                    }
                }

            }
            output.close();

        }catch (Exception e){
            active = false;
            Logger.log(Logger.level.ERROR,clientPrefix +" had en error!");
            e.printStackTrace();
        }
        Logger.log(Logger.level.INFO, clientPrefix + " has disconnected");
        server.getClients().remove(this);
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
    private void buyMoreCharacterSlots(String[] args){
        Session session = server.sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        if(user.getBalance() > 6 * Integer.parseInt(args[2])){
            user.setSlots(user.getSlots()+Integer.parseInt(args[2]));
            session.update(user);
            session.getTransaction().commit();
            output.println("/shopSuccessful");
        } else {
            output.println("/shopError notEnoughMoney");
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
    private void requestCharacterDetails(String[] args) {
        Logger.log(Logger.level.INFO, clientPrefix + " requested user details for user [" + args[2] + "]");

        Session session = server.sf.openSession();
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

            output.println(response);
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
    private String updateBalance(String[] args) {
        String response = "/updateBalance successful";

        Session session = server.sf.openSession();
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
     * @param args
     *          the used as parameters of the command
     *          [0] - command itself {/request}
     *          [1] - string {userDetails}
     *          [2] - string {'username'}
     * @return
     *          returns the command that has to be send to the user
     */
    private String requestUserDetail(String[] args) {
        Logger.log(Logger.level.INFO, clientPrefix + " requested user details for user [" + args[2] + "]");
        String response = "/userDetails ";

        Session session = server.sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, args[2]);

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
    private String registerUser(String[] args) {
        String response = "/register";

        Session session = server.sf.openSession();
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
    private String registerCharacter(String[] args) {
        String response = "/createCharacter ";

        Session session = server.sf.openSession();
        session.beginTransaction();

        if(session.get(Character.class, args[2])!=null){
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
    private String login(String[] args) {
        String response = "/login";

        Logger.log(Logger.level.INFO, clientPrefix + " tries to login");

        Session session = server.sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            User user = session.get(User.class, args[1]);

            if (user.getPassword().equals(args[2])){
                for (int i = 0; i < server.getClients().size(); i++) {
                    if(server.getClients().get(i).clientPrefix.equals(args[1])) return "/loginFail alreadyLoggedIn";
                }
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
    private String buyMoreDays(String[] args){
        int months = Integer.parseInt(args[2]);

        Logger.log(Logger.level.INFO, clientPrefix + " is trying to buy " + months + " more months");

        Session session = server.sf.openSession();
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
        Logger.log(Logger.level.INFO, clientPrefix + " bought a " + days + "days subscription");
        return "/buyMonthSuccessful "+days;
    }

    /**
     * Disconnect the user from the server
     */
    public void disconnect(){
        output.println("/serverDisconnected");
        active = false;
    }

}
