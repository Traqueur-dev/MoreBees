package fr.traqueur.morebees.api;

import java.util.HashMap;
import java.util.Map;

public class Logger {

    private static org.slf4j.Logger LOGGER;
    private static boolean DEBUG = false;

    private static final Map<String, String> ANSI_COLORS = new HashMap<>();

    static {
        // Color tags to ANSI
        ANSI_COLORS.put("<red>", "\u001B[31m");
        ANSI_COLORS.put("<green>", "\u001B[32m");
        ANSI_COLORS.put("<yellow>", "\u001B[33m");
        ANSI_COLORS.put("<blue>", "\u001B[34m");
        ANSI_COLORS.put("<aqua>", "\u001B[36m");
        ANSI_COLORS.put("<gray>", "\u001B[37m");
        ANSI_COLORS.put("<white>", "\u001B[97m");
        ANSI_COLORS.put("<black>", "\u001B[30m");
        ANSI_COLORS.put("<dark_red>", "\u001B[31m");
        ANSI_COLORS.put("<dark_green>", "\u001B[32m");
        ANSI_COLORS.put("<dark_aqua>", "\u001B[36m");
        ANSI_COLORS.put("<dark_blue>", "\u001B[34m");
        ANSI_COLORS.put("<gold>", "\u001B[33m");
        ANSI_COLORS.put("<light_purple>", "\u001B[95m");
        ANSI_COLORS.put("<dark_purple>", "\u001B[35m");

        // Formatting
        ANSI_COLORS.put("<bold>", "\u001B[1m");
        ANSI_COLORS.put("<italic>", "\u001B[3m");
        ANSI_COLORS.put("<underlined>", "\u001B[4m");
        ANSI_COLORS.put("<strikethrough>", "\u001B[9m");
        ANSI_COLORS.put("<reset>", "\u001B[0m");
    }

    public static void init(org.slf4j.Logger logger, boolean debug) {
        LOGGER = logger;
        DEBUG = debug;
    }

    public static void debug(String message, Object... args) {
        if (DEBUG) {
            info(message, args);
        }
    }

    public static void info(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    public static void success(String message, Object... args) {
        log(Level.INFO, "<green>" + message + "<reset>", args);
    }

    public static void warning(String message, Object... args) {
        log(Level.WARN, "<yellow>" + message + "<reset>", args);
    }

    public static void severe(String message, Object... args) {
        log(Level.ERROR, "<red>" + message + "<reset>", args);
    }

    public static void severe(String message, Exception exception, Object... args) {
        log(Level.ERROR, "<red>" + message + "<reset>", exception, args);
    }

    private static void log(Level level, String message, Object... args) {
        ensureInitialized();
        String formatted = convertMiniMessageToAnsi(message);
        switch (level) {
            case INFO -> LOGGER.info(formatted, args);
            case WARN -> LOGGER.warn(formatted, args);
            case ERROR -> LOGGER.error(formatted, args);
        }
    }

    private static void log(Level level, String message, Exception exception, Object... args) {
        ensureInitialized();
        String formatted = convertMiniMessageToAnsi(message);
        switch (level) {
            case INFO -> LOGGER.info(formatted, args, exception);
            case WARN -> LOGGER.warn(formatted, args, exception);
            case ERROR -> LOGGER.error(formatted, args, exception);
        }
    }

    private static void ensureInitialized() {
        if (LOGGER == null) {
            throw new IllegalStateException("Logger is not initialized. Call Logger.init() first.");
        }
    }

    private static String convertMiniMessageToAnsi(String message) {
        String ansiMessage = message;
        for (Map.Entry<String, String> entry : ANSI_COLORS.entrySet()) {
            ansiMessage = ansiMessage.replace(entry.getKey(), entry.getValue());
        }
        ansiMessage += ANSI_COLORS.get("<reset>");
        return ansiMessage;
    }

    private enum Level {
        INFO, WARN, ERROR
    }
}
