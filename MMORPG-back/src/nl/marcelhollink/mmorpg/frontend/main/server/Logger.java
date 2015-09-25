package nl.marcelhollink.mmorpg.frontend.main.server;

import java.util.Date;

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
}
