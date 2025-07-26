package fr.traqueur.morebees.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ToolsArgument implements ArgumentConverter<ToolsManager.Tool>, TabCompleter<CommandSender> {

    @Override
    public ToolsManager.Tool apply(String s) {
        try {
            return ToolsManager.Tool.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return Arrays.stream(ToolsManager.Tool.values()).map(ToolsManager.Tool::name).collect(Collectors.toList());
    }
}
