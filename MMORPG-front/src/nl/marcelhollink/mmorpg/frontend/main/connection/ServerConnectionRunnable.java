package nl.marcelhollink.mmorpg.frontend.main.connection;

import nl.marcelhollink.mmorpg.frontend.main.observers.SocketObserver;
import nl.marcelhollink.mmorpg.frontend.main.observers.SocketSubject;
import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;
import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.MenuState;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerConnectionRunnable implements Runnable, SocketSubject{

    private static ServerConnectionRunnable instance;

    private boolean active = true;

    private static Scanner input;
    private static PrintWriter output;

    private ArrayList<SocketObserver> observers;
    private String data;

    public ServerConnectionRunnable(Socket clientSocket) {
        this.observers = new ArrayList<>();
        instance = this;

        try {
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e){
            e.printStackTrace();
        }
        Logger.log(Logger.level.DEBUG, "serverConnectionRunnable created");
    }

    public static ServerConnectionRunnable getObserverSubject(){
        return instance;
    }

    @Override
    public void run() {
        try {
            while (active) {
                if (input.hasNextLine()) {
                    data = input.nextLine();
                    Logger.log(Logger.level.DEBUG, "Recieved ["+data+"]");
                    notifyObservers();

                    if(data.contains("/serverDisconnected")) {
                        Logger.log(Logger.level.DEBUG, "Recieved ["+data+"]");
                        UI.getFrame().getPanel().getGsc().setState(GameStateController.SERVERDISCONNECTEDSTATE);
                    }

                }
            }
            output.close();
        }catch (Exception e){
            active = false;
            System.out.println("Socket is closed");
            e.printStackTrace();
        }
    }

    public void send(String s){
        output.println(s);
    }

    @Override
    public void register(SocketObserver o) {
        observers.add(o);
    }

    @Override
    public void unregister(SocketObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        Logger.log(Logger.level.TRACE,"Notified "+observers.size()+" observers");
        for(SocketObserver observer : observers){
            observer.update(data);
        }
    }
}
