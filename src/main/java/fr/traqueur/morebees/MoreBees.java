package fr.traqueur.morebees;

import de.exlll.configlib.NameFormatters;
import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import fr.traqueur.commands.spigot.CommandManager;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.managers.UpgradesManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.settings.BreedSettings;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.settings.Settings;
import fr.traqueur.morebees.api.settings.UpgradeSettings;
import fr.traqueur.morebees.commands.MoreBeesRootCommand;
import fr.traqueur.morebees.commands.arguments.BeeTypeArgument;
import fr.traqueur.morebees.commands.arguments.ToolsArgument;
import fr.traqueur.morebees.commands.arguments.UpgradeArgument;
import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.managers.BeeManagerImpl;
import fr.traqueur.morebees.managers.BeehiveManagerImpl;
import fr.traqueur.morebees.managers.ToolsManagerImpl;
import fr.traqueur.morebees.managers.UpgradesManagerImpl;
import fr.traqueur.morebees.recipes.MoreBeesHook;
import fr.traqueur.morebees.serialization.*;
import fr.traqueur.recipes.api.RecipesAPI;
import fr.traqueur.recipes.api.hook.Hook;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class MoreBees extends BeePlugin {

    private static final YamlConfigurationProperties CONFIGURATION_PROPERTIES =  YamlConfigurationProperties.newBuilder()
            .setNameFormatter(NameFormatters.LOWER_KEBAB_CASE)
            .build();

    private final Map<Class<? extends Settings>, Settings> settings = new HashMap<>();

    private RecipesAPI recipesAPI;
    private CommandManager<@NotNull BeePlugin> commandManager;

    public void onEnable() {

        long startTime = System.currentTimeMillis();

        this.saveDefault("config.yml", GlobalSettings.class, GlobalSettings.DEFAULT.get());
        GlobalSettings settings = YamlConfigurations.load(this.getDataPath().resolve("config.yml"), GlobalSettings.class, CONFIGURATION_PROPERTIES);
        this.settings.put(GlobalSettings.class, settings);

        Logger.init(this.getSLF4JLogger(), settings.debug());

        this.saveDefaultConfig();
        this.reloadConfig();

        Hooks.initAll(this);

        Hook.addHook(new MoreBeesHook(this));
        Bukkit.getScheduler().runTask(this, () -> {
            this.recipesAPI = new RecipesAPI(this, this.getSettings(GlobalSettings.class).debug(), true);
        });

        UpgradeDataTypeImpl.init(this);
        BeeTypeDataTypeImpl.init(this);
        BeehiveDataTypeImpl.init();
        ToolDataTypeImpl.init();
        BeeDataDataTypeImpl.init();

        this.registerManager(BeeManager.class, new BeeManagerImpl());
        this.registerManager(UpgradesManager.class, new UpgradesManagerImpl());
        this.registerManager(BeehiveManager.class, new BeehiveManagerImpl());
        this.registerManager(ToolsManager.class, new ToolsManagerImpl());

        this.commandManager = new CommandManager<>(this);
        commandManager.setDebug(settings.debug());
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
        commandManager.registerConverter(Tool.class, new ToolsArgument());
        commandManager.registerConverter(Upgrade.class, new UpgradeArgument(this));

        commandManager.registerCommand(new MoreBeesRootCommand(this));

        Logger.success("MoreBees has been enabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    public void onDisable() {
        long startTime = System.currentTimeMillis();

        Logger.success("MoreBees has been disabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    @Override
    public CommandManager<@NotNull BeePlugin> getCommandManager() {
        return commandManager;
    }

    @Override
    public <T extends Settings> T getSettings(Class<T> clazz) {
        if (settings.containsKey(clazz)) {
            return clazz.cast(settings.get(clazz));
        }
        throw new IllegalArgumentException("Class " + clazz.getName() + " does not exist");
    }

    @Override
    public void saveDefaultConfig() {
        this.saveDefault("messages.yml", Messages.Config.class, Messages.DEFAULT);
        this.saveDefault("breeds.yml", BreedSettings.class, BreedSettings.DEFAULT.get());
        this.saveDefault("upgrades.yml", UpgradeSettings.class, UpgradeSettings.DEFAULT.get());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.reloadConfig("config.yml", GlobalSettings.class);
        this.reloadConfig("breeds.yml", BreedSettings.class);
        this.reloadConfig("upgrades.yml", UpgradeSettings.class);
        Messages.Config messages = this.reloadConfig("messages.yml", Messages.Config.class);
        Messages.init(messages);
    }

    @Override
    public RecipesAPI getRecipesAPI() {
        return recipesAPI;
    }

    private <T extends Settings> void saveDefault(String path, Class<T> clazz, T instance) {
        if (!this.getDataPath().resolve(path).toFile().exists()) {
            YamlConfigurations.save(this.getDataPath().resolve(path), clazz, instance, CONFIGURATION_PROPERTIES);
        }
    }

    private <T extends Settings> T reloadConfig(String path, Class<T> clazz) {
        T instance = YamlConfigurations.load(this.getDataPath().resolve(path), clazz, CONFIGURATION_PROPERTIES);
        this.settings.put(clazz, instance);
        return instance;
    }
}
