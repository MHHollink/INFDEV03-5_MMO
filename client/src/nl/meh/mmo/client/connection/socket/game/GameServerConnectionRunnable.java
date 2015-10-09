package nl.meh.mmo.client.connection.socket.game;

import nl.meh.mmo.client.connection.observer.SocketObserver;
import nl.meh.mmo.client.connection.observer.SocketSubject;
import nl.meh.mmo.client.util.Logger;
import nl.meh.mmo.client.view.GameStateController;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GameServerConnectionRunnable implements Runnable, SocketSubject{

    private static GameServerConnectionRunnable instance;

    private boolean active = true;

    private static Socket socket;
    private static Scanner input;
    private static PrintWriter output;

    private ArrayList<SocketObserver> observers;
    private String data;

    public GameServerConnectionRunnable(Socket clientSocket) {
        this.observers = new ArrayList<>();
        instance = this;

        try {
            socket = clientSocket;
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e){
            e.printStackTrace();
        }
        Logger.log(Logger.level.DEBUG, "serverConnectionRunnable created");
    }

    public static GameServerConnectionRunnable getInstance(){
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
                        GameStateController.getInstance().setState(GameStateController.SERVERDISCONNECTEDSTATE);
                    }

                }
            }
            output.close();
            input.close();
            socket.close();
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
        Logger.log(Logger.level.TRACE, "Notified " + observers.size() + " observers");
        for(SocketObserver observer : observers){
            observer.update(data);
        }
    }
}
