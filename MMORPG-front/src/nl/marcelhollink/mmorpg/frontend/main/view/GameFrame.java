package nl.marcelhollink.mmorpg.frontend.main.view;

import nl.marcelhollink.mmorpg.frontend.main.UI;
import nl.marcelhollink.mmorpg.frontend.main.connection.ClientSocket;
import nl.marcelhollink.mmorpg.frontend.main.utils.Logger;

import javax.swing.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

public class GameFrame extends JFrame {

    private GamePanel panel;

    public GameFrame() {
        super();
        Logger.log(Logger.level.INFO, "GameFrame has been constructed");

        panel = new GamePanel();
        setContentPane(panel);

        setTitle(UI.TITLE);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                Logger.log(Logger.level.INFO, "game being closed");
                try {
                        ClientSocket.getInstance().send("/disconnectMeFromMMORPGServer");
                        ClientSocket.getInstance().getServer().close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                } catch (NullPointerException ignore){ }
                System.exit(0);
            }

            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
            public void windowOpened(WindowEvent e) {}
        });

        setResizable(false);
        pack();
        setVisible(true);

        setLocationRelativeTo(null);
    }

    public void setNewSize(int w, int h){
        setSize(w,h);
    }

    public GamePanel getPanel() {
        return panel;
    }
}
