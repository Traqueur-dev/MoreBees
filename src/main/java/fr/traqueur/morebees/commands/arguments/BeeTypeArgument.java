package fr.traqueur.morebees.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class BeeTypeArgument implements ArgumentConverter<BeeType>, TabCompleter<CommandSender> {

    private final BeePlugin plugin;

    public BeeTypeArgument(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public BeeType apply(String s) {
        return this.plugin.getSettings(GlobalSettings.class).getBeeType(s).orElse(null);
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return  this.plugin.getSettings(GlobalSettings.class).bees().stream().map(BeeType::type).collect(Collectors.toList());
    }
}
