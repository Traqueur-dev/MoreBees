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
import fr.traqueur.recipes.api.RecipeLoader;
import fr.traqueur.recipes.api.RecipesAPI;
import fr.traqueur.recipes.api.hook.Hook;
import fr.traqueur.structura.api.Structura;
import fr.traqueur.structura.exceptions.StructuraException;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public final class MoreBees extends BeePlugin {

    private static final int BSTATS_PLUGIN_ID = 28733;

    private RecipesAPI recipesAPI;
    private RecipeLoader recipeLoader;
    private CommandManager<@NotNull BeePlugin> commandManager;

    @Override
    public void onEnable() {
        long startTime = System.currentTimeMillis();

        new Metrics(this, BSTATS_PLUGIN_ID);

        this.saveDefaultConfig();
        this.reloadConfig();

        GlobalSettings settings = this.getSettings(GlobalSettings.class);
        Logger.init(this.getSLF4JLogger(), settings.debug());

        UpgradeDataTypeImpl.init(this);
        BeeTypeDataTypeImpl.init(this);
        BeehiveDataTypeImpl.init();
        ToolDataTypeImpl.init();
        BeeDataDataTypeImpl.init();

        this.registerManager(BeeManager.class, new BeeManagerImpl());
        this.registerManager(UpgradesManager.class, new UpgradesManagerImpl());
        this.registerManager(BeehiveManager.class, new BeehiveManagerImpl());
        this.registerManager(ToolsManager.class, new ToolsManagerImpl());

        Hooks.initAll(this);

        // RecipesAPI must be created on the first tick, once the recipe registries are ready.
        Hook.addHook(new MoreBeesHook(this));
        this.recipesAPI = new RecipesAPI(this, settings.debug());
        this.recipeLoader = this.recipesAPI.createLoader().addFolder("recipes");
        this.recipeLoader.load();

        this.registerCommands(settings);

        Logger.success("MoreBees has been enabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    @Override
    public void onDisable() {
        long startTime = System.currentTimeMillis();

        Logger.success("MoreBees has been disabled in <yellow>{}ms", (System.currentTimeMillis() - startTime));
    }

    @Override
    public CommandManager<@NotNull BeePlugin> getCommandManager() {
        return commandManager;
    }

    @Override
    public RecipesAPI getRecipesAPI() {
        return recipesAPI;
    }

    @Override
    public RecipeLoader getRecipeLoader() {
        return recipeLoader;
    }

    @Override
    public void saveDefaultConfig() {
        this.saveDefault("config.yml");
        this.saveDefault("messages.yml");
        this.saveDefault("breeds.yml");
        this.saveDefault("upgrades.yml");
    }

    @Override
    public void reloadConfig() {
        this.loadSettings("config.yml", GlobalSettings.class);
        this.loadSettings("breeds.yml", BreedSettings.class);
        this.loadSettings("upgrades.yml", UpgradeSettings.class);
        try {
            Structura.loadEnum(this.getDataPath().resolve("messages.yml"), Messages.class);
        } catch (StructuraException e) {
            Logger.severe("Failed to load messages.yml, some messages will be by default", e);
        }
    }

    private void registerCommands(GlobalSettings settings) {
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
    }

    private <T extends Settings> void loadSettings(String path, Class<T> clazz) {
        T instance = Structura.load(this.getDataPath().resolve(path), clazz);
        Settings.register(clazz, instance);
    }

    private void saveDefault(String path) {
        if (!this.getDataPath().resolve(path).toFile().exists()) {
            this.saveResource(path, false);
        }
    }
}
