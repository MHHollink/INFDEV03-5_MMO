package nl.meh.mmo.server.server;

import java.util.Date;

/**
 * This is the Logger. it formats all logs so they are clear and readable.
 * It handles time of the log and the log level.
 */
public class Logger {

    public enum level {
        TRACE,
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    public static void log(level level, String text) {
            Date d = new Date();
            String date = d.toLocaleString() ;

            System.out.println(date + " >> " + level + ": " + text);
    }

    public static void log(level level, int errorCode, String text) {
        Date d = new Date();
        String date = d.toLocaleString() ;

        System.out.println(date + " >> " + level + ": " + text);
    }
}
