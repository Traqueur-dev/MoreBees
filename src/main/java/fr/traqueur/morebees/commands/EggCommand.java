package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class EggCommand extends Command<@NotNull BeePlugin> {
    /**
     * The constructor of the command.
     *
     * @param plugin The plugin that owns the command.
     */
    public EggCommand(BeePlugin plugin) {
        super(plugin, "egg");

        this.setPermission("morebees.command.egg");
        this.setDescription(Messages.EGG_COMMAND_DESC.raw());

        this.addArgs("player", Player.class, "beetype", BeeType.class);
        this.addOptionalArgs("amount", Integer.class, (sender, args) -> List.of("1", "8", "16", "32", "64"));
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        Player targetPlayer = arguments.get("player");
        BeeType beeType = arguments.get("beetype");
        Optional<Integer> amountOpt = arguments.getOptional("amount");
        int amount = amountOpt.orElse(1);

        if(amount < 1 || amount > 64) {
            Messages.COMMAND_AMOUNT_INVALID.send(sender, Formatter.all("amount", amount, "max-amount", 64));
            return;
        }

        ItemStack egg = beeType.egg();
        if(amount > egg.getMaxStackSize()) {
            Messages.COMMAND_AMOUNT_INVALID.send(sender, Formatter.all("amount", amount, "max-amount", egg.getMaxStackSize()));
            return;
        }

        egg.setAmount(amount);
        Util.giveItem(targetPlayer, egg);
        Messages.EGG_COMMAND_SUCCESS.send(sender, Formatter.all("player", targetPlayer.getName(), "amount", amount, "beetype", beeType.displayName()));
    }
}
