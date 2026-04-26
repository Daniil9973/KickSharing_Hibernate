package ru.shalaginov.util;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.ConsoleHandler;

public class Logs {

    private static final Logger logger = Logger.getLogger("AppLogger");

    static {
        try {
            java.nio.file.Path logDir = java.nio.file.Paths.get("logs");
            if (!java.nio.file.Files.exists(logDir)) {
                java.nio.file.Files.createDirectory(logDir);
            }

            FileHandler fh = new FileHandler("logs/app.log", true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);


            Logger rootLogger = Logger.getLogger("");
            for (var handler : rootLogger.getHandlers()) {
                if (handler instanceof ConsoleHandler) {
                    rootLogger.removeHandler(handler);
                }
            }

        } catch (Exception e) {

        }
    }


    public static void error(String message, Exception e) {
        if (e != null) {
            logger.severe(message + " | " + e.getMessage());
        } else {
            logger.severe(message);
        }
    }


    public static void info(String message) {
        logger.info(message);
    }
}