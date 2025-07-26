package fr.traqueur.morebees.commands;

import fr.traqueur.commands.api.arguments.Arguments;
import fr.traqueur.commands.spigot.Command;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class MoreBeesRootCommand extends Command<@NotNull BeePlugin> {

    public MoreBeesRootCommand(BeePlugin plugin) {
        super(plugin, "morebees");

        this.addAlias("bees", "mb", "bee");
        this.setPermission("morebees.command.help");
        this.addSubCommand(
                new EggCommand(plugin),
                new HoneyCommand(plugin),
                new ReloadCommand(plugin),
                new SpawnCommand(plugin),
                new ToolCommand(plugin)
        );
    }

    @Override
    public void execute(CommandSender sender, Arguments arguments) {
        StringBuilder message = new StringBuilder();
        message.append(Messages.COMMAND_HELP_TITLE.raw()
                .replace("%plugin%", this.getPlugin().getPluginMeta().getName())
                .replace("%authors%", String.join(", ", this.getPlugin().getPluginMeta().getAuthors()))
                .replace("%version%", this.getPlugin().getPluginMeta().getVersion())
        ).append("\n");
        for (fr.traqueur.commands.api.models.Command<@NotNull BeePlugin, CommandSender> subcommand : this.getSubcommands()) {
            if(subcommand.getPermission().isEmpty() || sender.hasPermission(subcommand.getPermission())) {
                String usage = subcommand.getUsage();
                if (usage.isEmpty()) {
                    usage = subcommand.generateDefaultUsage(this.getPlugin().getCommandManager().getPlatform(), sender, this.getName());
                }
                String formattedSyntax = Messages.COMMAND_SYNTAX.raw().replace("%usage%", usage).replace("%description%", subcommand.getDescription());
                message.append(formattedSyntax).append("\n");
            }
        }
        String formattedMessage = message.toString().trim();
        sender.sendMessage(MiniMessageHelper.parse(formattedMessage));
    }
}
