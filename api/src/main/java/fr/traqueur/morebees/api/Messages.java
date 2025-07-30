package fr.traqueur.morebees.api;

import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.command.CommandSender;

/**
 * Enum representing various messages used in the MoreBees plugin.
 * Each message can be formatted and sent to a CommandSender.
 */
public enum Messages implements Loadable {

    COMMAND_HELP_TITLE("<green>%plugin% %version% <gray>by <gold>%authors%<gray> - Commands List"),
    COMMAND_SYNTAX("<white>%usage% <gray>► <gold>%description%"),

    RELOAD_COMMAND_DESC("<gray>Reloads the plugin configuration."),
    RELOAD_SUCCESS("<green>Configuration reloaded successfully!"),

    COMMAND_AMOUNT_INVALID("<red>Invalid amount for %amount%! Please choose a number between 1 and %max-amount%."),

    EGG_COMMAND_DESC("<gray>Gives you a bee egg."),
    EGG_COMMAND_SUCCESS("<green>Successfully given %amount% %beetype% egg(s) to %player%!"),

    SPAWN_COMMAND_DESC("<gray>Spawn a bee at the player location."),
    SPAWN_COMMAND_SUCCESS("<green>Successfully spawned a %beetype% bee at your location!"),

    HONEY_COMMAND_DESC("<gray>Gives you honey from a bee type."),
    HONEY_COMMAND_SUCCESS("<green>Successfully given %amount% honey from %beetype% to %player%!"),

    EMPTY_BEE_JAR("<red>Empty"),
    EMPTY_BEE_BOX("<red>Empty"),
    BEE_JAR_CONTENT("%beetype%"),
    BEE_BOX_CONTENT("%beetype% x%amount%"),

    TOOL_FULL("<red>Your %tool% is already full!"),

    TOOL_COMMAND_DESC("<gray>Gives a tool to a player."),
    TOOL_COMMAND_SUCCESS("<green>Successfully given %tool% to %player%!"),

    PRODUCE_BLOCKS_NO("<red>No"),
    PRODUCE_BLOCKS_YES("<green>Yes"),

    UPGRADE_COMMAND_DESC("<gray>Gives an upgrade to a player."),
    UPGRADE_COMMAND_SUCCESS("<green>Successfully given %upgrade% to %player%!"),;

    /** The raw message string for this enum constant. */
    private final String message;

    /**
     * Constructs a Messages enum with the specified message.
     *
     * @param message The raw message string.
     */
    Messages(String message) {
        this.message = message;
    }

    /**
     * Returns the raw message string for this enum constant.
     *
     * @return The raw message string.
     */
    public String raw() {
        return this.message;
    }

    /**
     * Sends the formatted message to the specified CommandSender.
     *
     * @param sender The CommandSender to send the message to.
     * @param formatters Optional formatters to apply to the message.
     */
    public void send(CommandSender sender, Formatter... formatters) {
       String formattedMessage = this.message;
        for (Formatter formatter : formatters) {
            formattedMessage = formatter.handle(BeePlugin.getPlugin(BeePlugin.class), formattedMessage);
        }
        sender.sendMessage(MiniMessageHelper.parse(formattedMessage));
    }

}
