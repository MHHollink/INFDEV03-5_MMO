package nl.marcelhollink.mmorpg.backend.server;

import nl.marcelhollink.mmorpg.backend.server.database.model.*;
import nl.marcelhollink.mmorpg.backend.server.database.model.Character;
import org.hibernate.Query;
import org.hibernate.Session;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings({"UnnecessaryLocalVariable", "FieldCanBeLocal"})
public class MMOClient implements Runnable{

    private final int connectionID;

    private final MMOServer server;

    private Socket clientSocket;
    private boolean active = true;

    private Scanner input;
    private PrintWriter output;

    private String clientPrefix;

    public MMOClient(MMOServer server, Socket clientSocket, int connectionID) {
        this.server = server;
        this.clientSocket = clientSocket;
        this.connectionID = connectionID;

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
                        output.println("/mainCurrentlyOnline "+server.clients.size());
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
        server.clients.remove(this);
    }

    private void buyMoreCharacterSlots(String[] args){
        Session session = server.sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        if(user.getBalance() > 6){
            user.setSlots(user.getSlots()+1);
            session.update(user);
            session.getTransaction().commit();
            output.println("/shopSuccessful");
        } else {
            output.println("/shopError notEnoughMoney");
        }

        session.close();
    }

    private void requestCharacterDetails(String[] args) {
        Logger.log(Logger.level.INFO, clientPrefix + " requested user details for user [" + args[2] + "]");

        Session session = server.sf.openSession();
        session.beginTransaction();

        Query query = session.createQuery("from UserOwnsCharacter where user_username = :username");
        query.setParameter("username", clientPrefix);

        ArrayList<UserOwnsCharacter> userOwnsCharacters = (ArrayList<UserOwnsCharacter>) query.list();
        for (UserOwnsCharacter uoc : userOwnsCharacters) {
            Character character = session.get(Character.class, uoc.getCharacter().getCharacterName());
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

    private String updateBalance(String[] args) {
        String response = "";

        Session session = server.sf.openSession();
        session.beginTransaction();

        User user = session.get(User.class, clientPrefix);
        user.setBalance(user.getBalance()+Double.parseDouble(args[2]));

        session.save(user);
        session.getTransaction().commit();
        session.close();

        response = "updated user balance with "+args[2];

        return response;
    }

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

    private String registerUser(String[] args) {
        String response = "";

        Session session = server.sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
           Logger.log(Logger.level.INFO,clientPrefix+" tried to save an existing user!");

            response = "/registerError 'Username Already exists";
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
            response = "/registerSuccessful";
        }

        session.close();
        return response;
    }

    private String registerCharacter(String[] args) {
        String response = "";

        Session session = server.sf.openSession();
        session.beginTransaction();

        if(session.get(Character.class, args[2])!=null){
            Logger.log(Logger.level.WARN, clientPrefix + " tried to save over an existing character!");
            response += "/createCharacter error alreadyExists";
        } else {
            if(session.get(User.class, args[1])!=null){
                User user = session.get(User.class, args[1]);

                if(user.getSlots() < 1) {
                    return "/createCharacter error noSlotsAvailable";
                }

                Logger.log(Logger.level.INFO, clientPrefix + " saving character");
                Character character = new Character();

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
                response += "/createCharacter successful";
            } else {
                Logger.log(Logger.level.ERROR, 404, "username not found");
            }
        }
        session.close();
        return response;
    }

    private String login(String[] args) {
        String response = "";

        Logger.log(Logger.level.INFO, clientPrefix + " tries to login");

        Session session = server.sf.openSession();
        session.beginTransaction();

        if(session.get(User.class, args[1])!=null){
            User user = session.get(User.class, args[1]);
            if (user.getPassword().equals(args[2])){
                response = "/loginSuccessful";
                Logger.log(Logger.level.INFO, clientPrefix + " logged in as " + args[1]);
                Logger.log(Logger.level.INFO, clientPrefix + " is now known as " + args[1]);
                clientPrefix = args[1];
            } else {
                Logger.log(Logger.level.INFO, clientPrefix + " logging in with wrong identification");
                response = "/loginFail incorrect";
            }
        } else {
            Logger.log(Logger.level.INFO, clientPrefix + " username was not found when logging in");
            response = "/loginFail notFound";
        }

        session.close();
        return response;
    }

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

    private String paymentSuccess(User user, double balance, double payed, int days, Session session){
        user.setBalance(balance - payed);
        user.setDaysLeft(user.getDaysLeft() + days);
        session.update(user);
        session.getTransaction().commit();
        session.close();
        Logger.log(Logger.level.INFO, clientPrefix + " bought a " + days + "days subscription");
        return "/buyMonthSuccessful "+days;
    }

    public void disconnect(){
        output.println("/serverDisconnected");
        active = false;
    }

}
