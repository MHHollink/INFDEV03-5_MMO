package nl.marcelhollink.mmorpg.frontend.main.view;

import nl.marcelhollink.mmorpg.frontend.main.UI;

import javax.swing.*;

import static nl.marcelhollink.mmorpg.frontend.main.UI.isDebug;

public class GameFrame extends JFrame {

    public GameFrame() {
        super();
        setContentPane(new GamePanel());

        setTitle(UI.TITLE);
        if (isDebug()) {
            setTitle(UI.TITLE +" - "+ UI.BUILD);
        }

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setResizable(false);
        pack();
        setVisible(true);

        setLocationRelativeTo(null);
    }
}
