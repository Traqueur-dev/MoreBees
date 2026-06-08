package fr.traqueur.morebees.api;

import fr.traqueur.commands.spigot.CommandManager;
import fr.traqueur.morebees.api.settings.Settings;
import fr.traqueur.morebees.api.settings.SettingsProvider;
import fr.traqueur.recipes.api.RecipeLoader;
import fr.traqueur.recipes.api.RecipesAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * The base class for the MoreBees plugin.
 * <p>
 * It exposes the command manager and the {@link RecipesAPI}, and provides — through
 * {@link ManagerProvider} and {@link SettingsProvider} — access to the plugin managers
 * (backed by Bukkit's {@code ServicesManager}) and to the loaded configuration models
 * (backed by the global {@link Settings} registry).
 */
public abstract class BeePlugin extends JavaPlugin implements ManagerProvider, SettingsProvider {

    /**
     * Gets the command manager for this plugin.
     *
     * @return the command manager
     */
    public abstract CommandManager<@NotNull BeePlugin> getCommandManager();

    /**
     * Retrieves the {@link RecipesAPI} instance for this plugin.
     *
     * @return the RecipesAPI instance
     */
    public abstract RecipesAPI getRecipesAPI();

    /**
     * Retrieves the {@link RecipeLoader} that loads the recipe files shipped with the plugin.
     * Calling {@link RecipeLoader#reload()} unregisters all recipes and reloads them from disk.
     *
     * @return the recipe loader
     */
    public abstract RecipeLoader getRecipeLoader();

    @Override
    public <T extends Manager> T getManager(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            throw new NoSuchElementException("No provider found for " + clazz.getSimpleName() + " class.");
        }
        return provider.getProvider();
    }

    @Override
    public <I extends Manager, T extends I> void registerManager(Class<I> clazz, T instance) {
        getServer().getServicesManager().register(clazz, instance, this, ServicePriority.Normal);
        Logger.debug("<green>Registered manager for {} successfully", clazz.getSimpleName());
    }

    @Override
    public <T extends Settings> T getSettings(Class<T> clazz) {
        T instance = Settings.get(clazz);
        if (instance == null) {
            throw new IllegalArgumentException("No settings registered for " + clazz.getName());
        }
        return instance;
    }

    /**
     * Registers a listener for this plugin.
     *
     * @param listener the listener to register
     */
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
        Logger.debug("<green>Registered listener {} successfully", listener.getClass().getSimpleName());
    }
}
