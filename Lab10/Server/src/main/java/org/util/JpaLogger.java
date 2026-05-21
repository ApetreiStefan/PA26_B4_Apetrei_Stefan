package org.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JpaLogger {
    private static final Logger logger = Logger.getLogger("JpaLogger");

    static {
        try {
            // Create logs directory if it doesn't exist
            File logsDir = new File("logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }
            
            // Configure FileHandler to write to logs/jpa.log
            FileHandler fileHandler = new FileHandler("logs/jpa.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Could not initialize file logging: " + e.getMessage());
        }
    }

    public static void info(String message) {
        System.out.println("[JPA-INFO] " + message);
        logger.info(message);
    }

    public static void error(String message, Throwable throwable) {
        System.err.println("[JPA-ERROR] " + message + (throwable != null ? ": " + throwable.getMessage() : ""));
        if (throwable != null) {
            logger.log(Level.SEVERE, message, throwable);
        } else {
            logger.severe(message);
        }
    }
}
