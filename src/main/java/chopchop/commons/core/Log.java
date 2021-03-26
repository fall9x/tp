// Log.java

package chopchop.commons.core;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import chopchop.commons.util.FileUtil;

/**
 * This functions as a simple wrapper class for the Java Logger class,
 * providing proper formatting methods (instead of needing to call String.format
 * each time like a plebeian).
 */
public class Log {

    private static final int maximumFileCount = 5;
    private static final int maximumLogSize = 2 * (1 << 20);
    private static final String logFileLocation = "data/logs/chopchop-%g%u.log";

    private static Level defaultLogLevel = Level.INFO;
    private static StreamHandler fileHandler;
    private static StreamHandler consoleHandler;

    private final Logger logger;

    /**
     * Constructs a new logger for the given class.
     */
    public Log(Class<?> cls) {
        var lg = Logger.getLogger(cls.getSimpleName());
        for (var h : lg.getHandlers()) {
            lg.removeHandler(h);
        }

        if (Log.fileHandler == null || Log.consoleHandler == null) {
            Log.init(new Config());
        }

        lg.addHandler(Log.fileHandler);
        lg.addHandler(Log.consoleHandler);
        lg.setUseParentHandlers(false);

        // no idea why it's done like this, but this is what AB3 does so i'm just following it.
        this.logger = Logger.getLogger(cls.getSimpleName());
    }

    /**
     * Logs a debug message; equivalent to Level.FINE
     */
    public void debug(String fmt, Object... args) {
        this.logger.fine(String.format(fmt, args));
    }

    /**
     * Logs a normal message; equivalent to Level.INFO
     */
    public void log(String fmt, Object... args) {
        this.logger.info(String.format(fmt, args));
    }

    /**
     * Logs a warning message; equivalent to Level.WARN
     */
    public void warn(String fmt, Object... args) {
        this.logger.warning(String.format(fmt, args));
    }

    /**
     * Logs an error messsage; equivalent to Level.SEVERE
     */
    public void error(String fmt, Object... args) {
        this.logger.severe(String.format(fmt, args));
    }



    /**
     * Initialises the logging subsystem, and sets up the handlers as appropriate.
     */
    public static void init(Config config) {
        Log.defaultLogLevel = config.getLogLevel();
        System.out.printf("Default logging level: %s\n", Log.defaultLogLevel);

        if (Log.fileHandler != null && Log.consoleHandler != null) {
            Log.fileHandler.setLevel(Log.defaultLogLevel);
            Log.consoleHandler.setLevel(Log.defaultLogLevel);

            return;
        }

        consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Log.defaultLogLevel);
        consoleHandler.setFormatter(new LogFormatter());

        try {
            FileUtil.createParentDirsOfFile(Paths.get(logFileLocation));

            fileHandler = new FileHandler(logFileLocation, maximumLogSize, maximumFileCount,
                /* append: */ true);
            fileHandler.setLevel(Log.defaultLogLevel);
            fileHandler.setFormatter(new LogFormatter());
        } catch (IOException e) {
            System.err.printf("Failed to open log file: %s\n", e.toString());
            fileHandler = new DummyHandler();
        }
    }

    public static class LogFormatter extends Formatter {
        @Override
        public String format(LogRecord rec) {

            var timestamp = new SimpleDateFormat("dd/MM HH:mm:ss.SSS").format(new Date(rec.getMillis()));

            return String.format("[%s] %s (%s): %s\n",
                timestamp,
                levelToString(rec.getLevel()),
                rec.getLoggerName(),
                rec.getMessage()
            );
        }

        private String levelToString(Level level) {
            if (level == Level.FINE) {
                return "dbg";
            } else if (level == Level.INFO) {
                return "log";
            } else if (level == Level.WARNING) {
                return "wrn";
            } else if (level == Level.SEVERE) {
                return "err";
            } else {
                return level.toString();
            }
        }
    }





    public static class DummyHandler extends StreamHandler {
    }
}
