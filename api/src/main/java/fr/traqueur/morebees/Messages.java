package fr.traqueur.morebees;

import fr.traqueur.morebees.util.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public enum Messages {

    ;

    private String message;

    Messages(String message) {
        this.message = message;
    }

    public void send(CommandSender sender, Formatter... formatters) {
       String formattedMessage = this.message;
        for (Formatter formatter : formatters) {
            formattedMessage = formatter.handle(BeePlugin.getPlugin(BeePlugin.class), formattedMessage);
        }
        sender.sendMessage(formattedMessage);
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
