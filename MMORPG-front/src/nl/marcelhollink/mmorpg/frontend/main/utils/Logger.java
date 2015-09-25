package nl.marcelhollink.mmorpg.frontend.main.utils;

import nl.marcelhollink.mmorpg.frontend.main.UI;

import java.util.Date;

public class Logger {

    public enum level {
        ALL,
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
        NONE
    }

    public static void log(level level, String text) {
        if (UI.LOGLEVEL != Logger.level.NONE) {
            if (level.ordinal() >= UI.LOGLEVEL.ordinal()) {
                Date d = new Date();
                String date = d.toLocaleString();

                System.out.println(date + " >> " + level + ": " + text);
            }
        }
    }

}
