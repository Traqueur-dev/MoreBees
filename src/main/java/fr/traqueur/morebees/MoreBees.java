package fr.traqueur.morebees;

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
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MoreBees extends BeePlugin {

    private final Map<Class<? extends Settings>, Settings> settings = new HashMap<>();

    private RecipesAPI recipesAPI;
    private CommandManager<@NotNull BeePlugin> commandManager;

    public void onEnable() {

        long startTime = System.currentTimeMillis();

        this.saveDefault("config.yml");
        GlobalSettings settings = this.reloadConfig("config.yml", GlobalSettings.class);

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
        this.saveDefault("messages.yml");
        this.saveDefault("breeds.yml");
        this.saveDefault("upgrades.yml");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        this.reloadConfig("config.yml", GlobalSettings.class);
        this.reloadConfig("breeds.yml", BreedSettings.class);
        this.reloadConfig("upgrades.yml", UpgradeSettings.class);
        try {
            Structura.loadEnum(this.getDataPath().resolve("messages.yml"), Messages.class);
        } catch (StructuraException e) {
            Logger.severe("Failed to load messages.yml, some messages will be by default", e);
        }
    }

    @Override
    public RecipesAPI getRecipesAPI() {
        return recipesAPI;
    }

    private <T extends Settings> void saveDefault(String path) {
        if (!this.getDataPath().resolve(path).toFile().exists()) {
            this.saveResource(path, false);
        }
    }

    private <T extends Settings> T reloadConfig(String path, Class<T> clazz) {
        T instance = Structura.load(this.getDataPath().resolve(path), clazz);
        this.settings.put(clazz, instance);
        return instance;
    }
}
