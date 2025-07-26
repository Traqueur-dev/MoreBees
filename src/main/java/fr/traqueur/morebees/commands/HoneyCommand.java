package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.util.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class HoneyCommand extends Command<@NotNull BeePlugin> {
    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public HoneyCommand(BeePlugin plugin) {
        super(plugin, "honey");

        this.setPermission("morebees.command.honey");
        this.setDescription(Messages.HONEY_COMMAND_DESC.raw());

        this.addArgs("player", Player.class, "beetype", BeeType.class, "block", Boolean.class);
        this.addOptionalArgs("amount", Integer.class, (sender, args) -> List.of("1", "8", "16", "32", "64"));
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player targetPlayer = arguments.get("player");
        BeeType beeType = arguments.get("beetype");
        boolean block = arguments.get("block");
        Optional<Integer> amountOpt = arguments.getOptional("amount");
        int amount = amountOpt.orElse(1);

        if(amount < 1 || amount > 64) {
            Messages.COMMAND_AMOUNT_INVALID.send(sender, Formatter.all("amount", amount, "max-amount", 64));
            return;
        }

        ItemStack honey = beeType.honey(1, block);
        if(amount > honey.getMaxStackSize()) {
            Messages.COMMAND_AMOUNT_INVALID.send(sender, Formatter.all("amount", amount, "max-amount", honey.getMaxStackSize()));
            return;
        }

        honey.setAmount(amount);
        targetPlayer.getInventory().addItem(honey).forEach((slot, item) -> {
            Item dropped = targetPlayer.getWorld().dropItem(targetPlayer.getLocation(), item);
            dropped.setOwner(targetPlayer.getUniqueId());
        });
        Messages.HONEY_COMMAND_SUCCESS.send(sender, Formatter.all("player", targetPlayer.getName(), "amount", amount, "beetype", beeType.displayName()));
    }
}
