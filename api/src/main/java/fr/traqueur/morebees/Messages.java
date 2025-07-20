package fr.traqueur.morebees;

import fr.traqueur.morebees.util.Formatter;
import fr.traqueur.morebees.util.MiniMessageHelper;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public enum Messages {

    COMMAND_HELP_TITLE("<green>%plugin% %version% <gray>by <gold>%authors%<gray> - Commands List"),
    COMMAND_SYNTAX("<white>%usage% <gray>► <gold>%description%"),

    RELOAD_COMMAND_DESC("<gray>Reloads the plugin configuration."),
    RELOAD_SUCCESS("<green>Configuration reloaded successfully!"),

    EGG_COMMAND_DESC("<gray>Gives you a bee egg."),
    EGG_COMMAND_AMOUNT_INVALID("<red>Invalid amount for %amount%! Please choose a number between 1 and %max-amount%."),
    EGG_COMMAND_SUCCESS("<green>Successfully given %amount% %beetype% egg(s) to %player%!"),;

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public String raw() {
        return this.message;
    }

    public void send(CommandSender sender, Formatter... formatters) {
       String formattedMessage = this.message;
        for (Formatter formatter : formatters) {
            formattedMessage = formatter.handle(BeePlugin.getPlugin(BeePlugin.class), formattedMessage);
        }
        sender.sendMessage(MiniMessageHelper.parse(formattedMessage));
    }

    public static final Config DEFAULT = Config.defaultConfig();

    public static void init(Config config) {
        for (Messages value : Messages.values()) {
            String key = Messages.snakeToLowerKebab(value.name());
            if (config.messages.containsKey(key)) {
                value.message = config.messages.get(key);
            } else {
                Logger.warning("Missing message for key: " + key + ", using default: " + value.message);
            }
        }
    }

    public record Config(Map<String, String> messages) {

        public static Config defaultConfig() {
           Map<String, String> messages = new HashMap<>();
            for (Messages value : Messages.values()) {
                messages.put(Messages.snakeToLowerKebab(value.name()), value.message);
            }
            return new Config(messages);
        }
    }

    private static String snakeToLowerKebab(String str) {
        return str.replace('_', '-').toLowerCase();
    }

}
