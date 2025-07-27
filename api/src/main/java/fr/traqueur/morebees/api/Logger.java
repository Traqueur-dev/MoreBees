package fr.traqueur.morebees.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Logger class for MoreBees API.
 * This class provides methods to log messages with different severity levels
 * and supports ANSI color codes for console output.
 */
public class Logger {

    /**
     * The logger instance used for logging messages.
     * It should be initialized using the init method before use.
     */
    private static org.slf4j.Logger LOGGER;
    /**
     * Debug mode flag. If true, debug messages will be logged.
     * This should be set during initialization.
     */
    private static boolean DEBUG = false;

    /**
     * ANSI color codes mapping for converting MiniMessage tags to ANSI escape codes.
     * This map is used to replace MiniMessage tags with their corresponding ANSI codes.
     */
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

    /**
     * Initializes the logger with the provided SLF4J logger instance and debug mode.
     *
     * @param logger The SLF4J logger instance to use for logging.
     * @param debug  If true, enables debug logging.
     */
    public static void init(org.slf4j.Logger logger, boolean debug) {
        LOGGER = logger;
        DEBUG = debug;
    }

    /**
     * Logs a debug message if debug mode is enabled.
     * @param message The message to log.
     * @param args Optional arguments to format the message.
     */
    public static void debug(String message, Object... args) {
        if (DEBUG) {
            info(message, args);
        }
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     * @param args    Optional arguments to format the message.
     */
    public static void info(String message, Object... args) {
        log(Level.INFO, message, args);
    }

    /**
     * Logs a success message, typically used to indicate successful operations.
     *
     * @param message The success message to log.
     * @param args    Optional arguments to format the message.
     */
    public static void success(String message, Object... args) {
        log(Level.INFO, "<green>" + message + "<reset>", args);
    }

    /**
     * Logs a warning message, typically used to indicate potential issues.
     *
     * @param message The warning message to log.
     * @param args    Optional arguments to format the message.
     */
    public static void warning(String message, Object... args) {
        log(Level.WARN, "<yellow>" + message + "<reset>", args);
    }

    /**
     * Logs an error message, typically used to indicate failures or critical issues.
     *
     * @param message The error message to log.
     * @param args    Optional arguments to format the message.
     */
    public static void severe(String message, Object... args) {
        log(Level.ERROR, "<red>" + message + "<reset>", args);
    }

    /**
     * Logs an informational message with an exception.
     *
     * @param message    The message to log.
     * @param exception  The exception to log.
     * @param args       Optional arguments to format the message.
     */
    public static void severe(String message, Exception exception, Object... args) {
        log("<red>" + message + "<reset>", exception, args);
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

    private static void log(String message, Exception exception, Object... args) {
        ensureInitialized();
        String formatted = convertMiniMessageToAnsi(message);
        LOGGER.error(formatted, args, exception);
    }

    /**
     * Ensures that the logger is initialized before any logging operations.
     * This method should be called before using any logging methods to prevent NullPointerExceptions.
     */
    private static void ensureInitialized() {
        if (LOGGER == null) {
            throw new IllegalStateException("Logger is not initialized. Call Logger.init() first.");
        }
    }

    /**
     * Converts a MiniMessage formatted string to an ANSI formatted string.
     * This method replaces MiniMessage tags with their corresponding ANSI escape codes.
     *
     * @param message The MiniMessage formatted string.
     * @return The ANSI formatted string.
     */
    private static String convertMiniMessageToAnsi(String message) {
        String ansiMessage = message;
        for (Map.Entry<String, String> entry : ANSI_COLORS.entrySet()) {
            ansiMessage = ansiMessage.replace(entry.getKey(), entry.getValue());
        }
        ansiMessage += ANSI_COLORS.get("<reset>");
        return ansiMessage;
    }

    /**
     * Enum representing the logging levels.
     * This is used to differentiate between info, warning, and error messages.
     */
    private enum Level {
        INFO, WARN, ERROR
    }
}
