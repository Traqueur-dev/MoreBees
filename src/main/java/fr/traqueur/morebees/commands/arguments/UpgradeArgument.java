package fr.traqueur.morebees.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.settings.UpgradeSettings;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

public class UpgradeArgument implements ArgumentConverter<Upgrade>, TabCompleter<CommandSender> {

    private final BeePlugin plugin;

    public UpgradeArgument(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Upgrade apply(String s) {
        return this.plugin.getSettings(UpgradeSettings.class).getUpgrade(s).orElse(null);
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return  this.plugin.getSettings(UpgradeSettings.class).upgrades().stream().map(Upgrade::id).collect(Collectors.toList());
    }
}
