package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.util.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpgradeCommand extends Command<@NotNull BeePlugin> {
    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public UpgradeCommand(BeePlugin plugin) {
        super(plugin, "upgrade");

        this.setPermission("morebees.command.upgrade");
        this.setDescription(Messages.UPGRADE_COMMAND_DESC.raw());

        this.addArgs("player", Player.class, "upgrade", Upgrade.class);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player targetPlayer = arguments.get("player");
        Upgrade upgrade = arguments.get("upgrade");
        ItemStack stack = upgrade.build();

        targetPlayer.getInventory().addItem(stack).forEach((slot, item) -> {
            Item dropped = targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), item);
            dropped.setOwner(targetPlayer.getUniqueId());
        });

        Messages.UPGRADE_COMMAND_SUCCESS.send(sender, Formatter.all("player", targetPlayer.getName(), "upgrade", upgrade.id()));
    }
}
