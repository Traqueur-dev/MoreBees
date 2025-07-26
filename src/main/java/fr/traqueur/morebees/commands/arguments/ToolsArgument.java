package fr.traqueur.morebees.commands.arguments;

import fr.traqueur.commands.api.arguments.ArgumentConverter;
import fr.traqueur.commands.api.arguments.TabCompleter;
import fr.traqueur.morebees.api.models.Tool;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ToolsArgument implements ArgumentConverter<Tool>, TabCompleter<CommandSender> {

    @Override
    public Tool apply(String s) {
        try {
            return Tool.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public List<String> onCompletion(CommandSender sender, List<String> args) {
        return Arrays.stream(Tool.values()).map(Tool::name).collect(Collectors.toList());
    }
}
