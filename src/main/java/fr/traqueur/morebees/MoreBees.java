package fr.traqueur.morebees;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import fr.traqueur.commands.spigot.CommandManager;
import fr.traqueur.morebees.commands.MoreBeesRootCommand;
import fr.traqueur.morebees.commands.arguments.BeeTypeArgument;
import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.serialization.BeeTypeDataTypeImpl;
import fr.traqueur.morebees.settings.Settings;
import org.jetbrains.annotations.NotNull;

public final class MoreBees extends BeePlugin {

    private static final YamlConfigurationProperties CONFIGURATION_PROPERTIES =  YamlConfigurationProperties.newBuilder()
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .build();

    private CommandManager<@NotNull BeePlugin> commandManager;
    private Settings settings;

    public void onEnable() {

        long startTime = System.currentTimeMillis();

        this.saveDefault("config.yml", Settings.class, Settings.DEFAULT.get());
        this.settings = YamlConfigurations.load(this.getDataPath().resolve("config.yml"), Settings.class, CONFIGURATION_PROPERTIES);
        Logger.init(this.getSLF4JLogger(), this.settings.debug());

        this.saveDefaultConfig();
        this.reloadConfig();

        Hooks.initAll(this);

        BeeTypeDataTypeImpl.init(this);

        this.registerManager(BeeManager.class, new BeeManagerImpl());

        this.commandManager = new CommandManager<>(this);
        commandManager.setDebug(this.settings.debug());
        commandManager.setLogger(new fr.traqueur.commands.api.logging.Logger() {
            @Override
            public void error(String message) {
                Logger.severe(message);
            }

            @Override
            public void info(String message) {
                Logger.info(message);
            }
        });

        commandManager.registerConverter(BeeType.class, new BeeTypeArgument(this));

        commandManager.registerCommand(new MoreBeesRootCommand(this));


        Logger.success("MoreBees has been enabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    public void onDisable() {
        long startTime = System.currentTimeMillis();

        Logger.success("MoreBees has been disabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public CommandManager<@NotNull BeePlugin> getCommandManager() {
        return commandManager;
    }

    @Override
    public void saveDefaultConfig() {
        this.saveDefault("messages.yml", Messages.Config.class, Messages.DEFAULT);
    }

    private <T> void saveDefault(String path, Class<T> clazz, T instance) {
        if (!this.getDataPath().resolve(path).toFile().exists()) {
            YamlConfigurations.save(this.getDataPath().resolve(path), clazz, instance, CONFIGURATION_PROPERTIES);
        }
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.settings = YamlConfigurations.load(this.getDataPath().resolve("config.yml"), Settings.class, CONFIGURATION_PROPERTIES);
        Messages.Config messages = YamlConfigurations.load(this.getDataPath().resolve("messages.yml"), Messages.Config.class, CONFIGURATION_PROPERTIES);
        Messages.init(messages);
    }
}
