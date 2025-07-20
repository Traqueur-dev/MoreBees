package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.BeePlugin;
import fr.traqueur.morebees.Messages;
import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.util.Formatter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SpawnCommand extends Command<@NotNull BeePlugin> {

    public SpawnCommand(BeePlugin plugin) {
        super(plugin, "spawn");

        this.setPermission("morebees.command.spawn");
        this.setDescription(Messages.SPAWN_COMMAND_DESC.raw());

        this.addArgs("beetype", BeeType.class);
        this.addOptionalArgs("baby", Boolean.class);
        this.setGameOnly(true);
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        BeeType beeType = arguments.get("beetype");
        Player player = (Player) sender;
        Optional<Boolean> babyOptional = arguments.getOptional("baby");
        boolean baby = babyOptional.orElse(false);

        BeeManager beeManager = getPlugin().getManager(BeeManager.class);

        beeManager.spawnBee(player.getLocation(), beeType, CreatureSpawnEvent.SpawnReason.COMMAND, baby);
        Messages.SPAWN_COMMAND_SUCCESS.send(sender, Formatter.format("beetype", beeType.displayName()));
    }
}
