package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ToolCommand extends Command<@NotNull BeePlugin> {
    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public ToolCommand(BeePlugin plugin) {
        super(plugin, "tool");

        this.setPermission("morebees.command.tool");
        this.setDescription(Messages.TOOL_COMMAND_DESC.raw());

        this.addArgs("player", Player.class, "tool", Tool.class);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player targetPlayer = arguments.get("player");

        Tool tool = arguments.get("tool");
        ItemStack stack = tool.itemStack(List.of());

        Util.giveItem(targetPlayer, stack);

        Messages.TOOL_COMMAND_SUCCESS.send(sender, Formatter.all("player", targetPlayer.getName(), "tool", tool.name()));
    }
}
