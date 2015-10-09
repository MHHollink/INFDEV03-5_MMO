package nl.meh.mmo.client.util;

import nl.meh.mmo.client.Main;

import java.util.Date;

/**
 * Custom logger. adds time and type of a log to a simple System.out.println
 */
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
        if (Main.LOGLEVEL != Logger.level.NONE) {
            if (level.ordinal() >= Main.LOGLEVEL.ordinal()) {
                Date d = new Date();
                String date = d.toLocaleString();

                System.out.println(date + " >> " + level + ": " + text);
            }
        }
    }

}

