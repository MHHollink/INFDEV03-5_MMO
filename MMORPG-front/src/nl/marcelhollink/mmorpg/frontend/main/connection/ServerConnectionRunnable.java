package nl.marcelhollink.mmorpg.frontend.main.connection;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.controller.GameStateController;
import nl.marcelhollink.mmorpg.frontend.main.utils.L;
import nl.marcelhollink.mmorpg.frontend.main.view.gamestates.MenuState;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerConnectionRunnable implements Runnable{

    private Socket clientSocket;
    private boolean active = true;

    private static Scanner input;
    private static PrintWriter output;

    public ServerConnectionRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try{
            input = new Scanner(clientSocket.getInputStream());
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        output.println("/connect");
        try {
            while (active) {
                if (input.hasNextLine()) {
                    final String data = input.nextLine();

                    if(data.contains("/serverID")){
                        UI.serverID = data.split(" ")[1];
                        L.log(L.level.INFO, "Connected to server "+UI.serverID);
                    }
                    if(data.contains("/currentlyOnline")){
                        L.log(L.level.INFO, data.substring(17));
                    }
                    if(data.contains("/exitSplash")){
                        MenuState.stopSplash();
                    }
                    if(data.contains("/register")) {
                        UI.getFrame().getPanel().getGsc().getGameStates().get(GameStateController.REGISTERSTATE).receive(data);
                    }
                    if(data.contains("/login")) {
                        UI.getFrame().getPanel().getGsc().getGameStates().get(GameStateController.LOGINSTATE).receive(data);
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
}
