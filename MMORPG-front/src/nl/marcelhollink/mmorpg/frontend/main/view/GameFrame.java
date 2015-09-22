package nl.marcelhollink.mmorpg.frontend.main.view;

import nl.marcelhollink.mmorpg.frontend.main.UI;

import javax.swing.*;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import static nl.marcelhollink.mmorpg.frontend.main.UI.isDebug;

public class GameFrame extends JFrame {

    private GamePanel panel;

    public GameFrame() {
        super();

        panel = new GamePanel();
        setContentPane(panel);

        setTitle(UI.TITLE);
        if (isDebug()) {
            setTitle(UI.TITLE +" - "+ UI.BUILD);
        }

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    UI.clientSocket.send("/disconnectMeFromMMORPGServer");
                    UI.clientSocket.getServer().close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
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
