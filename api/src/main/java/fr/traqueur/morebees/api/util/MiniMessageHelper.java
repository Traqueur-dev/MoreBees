package fr.traqueur.morebees.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

/**
 * A utility class for parsing and unparsing MiniMessage strings.
 * This class provides methods to convert MiniMessage formatted strings
 * into Adventure Components and vice versa.
 */
public class MiniMessageHelper {

    /**
     * The MiniMessage instance used for parsing and unparsing.
     */
    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    /**
     * Parses a MiniMessage formatted string into an Adventure Component.
     *
     * @param message the MiniMessage formatted string to parse
     * @return the parsed Adventure Component
     */
    public static Component parse(String message) {
        return MINI_MESSAGE.deserialize(message);
    }

    /**
     * Unparses an Adventure Component into a MiniMessage formatted string.
     *
     * @param component the Adventure Component to unparse
     * @return the MiniMessage formatted string
     */
    public static String unparse(@NotNull Component component) {
        return  MINI_MESSAGE.serialize(component);
    }
}
