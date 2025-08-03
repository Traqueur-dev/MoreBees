package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.managers.BeeManager;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand extends Command<@NotNull BeePlugin> {

    public ReloadCommand(BeePlugin plugin) {
        super(plugin, "reload");

        this.setPermission("morebees.command.reload");
        this.setDescription(Messages.RELOAD_COMMAND_DESC.raw());
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        this.getPlugin().reloadConfig();
        BeeManager beeManager = this.getPlugin().getManager(BeeManager.class);
        if (beeManager != null) {
            this.getPlugin().getRecipesAPI().unregisterRecipes();
            beeManager.registerRecipes();
        }
        Messages.RELOAD_SUCCESS.send(sender);
    }
}
