package nl.marcelhollink.mmorpg.frontend.main.utils;

import nl.marcelhollink.mmorpg.frontend.main.UI;

public class L {

    public enum level {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public static void log(level level, String text){
        if(UI.isDebug()) System.out.println(level+": "+text);
    }


}
