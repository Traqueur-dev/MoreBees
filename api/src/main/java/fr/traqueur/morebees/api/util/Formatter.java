package fr.traqueur.morebees.api.util;

import fr.traqueur.morebees.api.BeePlugin;

import java.util.function.Function;

/**
 * A utility class for formatting strings with dynamic content.
 * It allows you to define patterns and their corresponding suppliers
 * that will replace the patterns in a given text.
 */
public class Formatter {

    /**
     * The pattern to be replaced in the text.
     */
    private final String pattern;
    /**
     * A function that provides the content to replace the pattern.
     * It takes a BeePlugin instance as an argument to access plugin-specific data.
     */
    private final Function<BeePlugin, String> supplier;

    /**
     * Formats the given text by replacing all occurrences of the defined patterns
     * with their corresponding content provided by the suppliers.
     *
     * @param text       The text to format.
     * @param formatters An array of Formatter instances defining the patterns and their suppliers.
     * @return The formatted text with all patterns replaced.
     */
    public static String format(String text, Formatter... formatters) {
        if (text == null) return null;
        for (Formatter formatter : formatters) {
            text = formatter.handle(BeePlugin.getPlugin(BeePlugin.class), text);
        }
        return text;
    }

    /**
     * Creates an array of Formatter instances from the provided pattern and supplier pairs.
     * Each pair consists of a String pattern and an Object supplier.
     *
     * @param objects An even number of arguments where each pair is a pattern followed by a supplier.
     * @return An array of Formatter instances.
     * @throws IllegalArgumentException if the number of arguments is odd or if the patterns are not Strings.
     */
    public static Formatter[] all(Object... objects) {
        if(objects.length % 2 != 0) {
            throw new IllegalArgumentException("You must provide an even number of arguments (pattern, supplier) pairs.");
        }
        Formatter[] formatters = new Formatter[objects.length / 2];
        for (int i = 0; i < objects.length; i += 2) {
            if (!(objects[i] instanceof String)) {
                throw new IllegalArgumentException("Each pattern must be a String and each supplier must be a Object.");
            }
            formatters[i / 2] = new Formatter((String) objects[i], objects[i + 1]);
        }
        return formatters;
    }

    /**
     * Creates a new Formatter instance with the specified pattern and supplier.
     * @param pattern the pattern to be replaced in the text
     * @param supplier the supplier that provides the content to replace the pattern
     */
    private Formatter(String pattern, Object supplier) {
        this.pattern = pattern;
        this.supplier = (api) -> supplier.toString();
    }

/**
     * Creates a new Formatter instance with the specified pattern and supplier function.
     * @param pattern the pattern to be replaced in the text
     * @param supplier the function that provides the content to replace the pattern
     */
    private Formatter(String pattern, Function<BeePlugin, String> supplier) {
        this.pattern = pattern;
        this.supplier = supplier;
    }

    /**
     * Factory method to create a Formatter instance with a String supplier.
     *
     * @param pattern  the pattern to be replaced in the text
     * @param supplier the supplier that provides the content to replace the pattern
     * @return a new Formatter instance
     */
    public static Formatter format(String pattern, Object supplier) {
        return new Formatter(pattern, supplier);
    }

    /**
     * Factory method to create a Formatter instance with a Function supplier.
     *
     * @param pattern  the pattern to be replaced in the text
     * @param supplier the function that provides the content to replace the pattern
     * @return a new Formatter instance
     */
    public static Formatter format(String pattern, Function<BeePlugin, String> supplier) {
        return new Formatter(pattern, supplier);
    }

    /**
     * Handles the replacement of the pattern in the given message with the content provided by the supplier.
     *
     * @param api     the BeePlugin instance to access plugin-specific data
     * @param message the message containing the pattern to be replaced
     * @return the message with the pattern replaced by the content from the supplier
     */
    public String handle(BeePlugin api, String message) {
        String content = this.supplier.apply(api);
        return message.replaceAll("%" + this.pattern + "%", content);
    }
}