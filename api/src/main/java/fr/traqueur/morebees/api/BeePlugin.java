package fr.traqueur.morebees.api;

import fr.traqueur.commands.spigot.CommandManager;
import fr.traqueur.morebees.api.settings.Settings;
import fr.traqueur.recipes.api.RecipesAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * The main class for the MoreBees plugin.
 * This class provides methods to access various managers and settings.
 * It also allows for registering listeners and managers.
 */
public abstract class BeePlugin extends JavaPlugin {

    /**
     * Gets the command manager for this plugin.
     *
     * @return the command manager
     */
    public abstract CommandManager<@NotNull BeePlugin> getCommandManager();

    /**
     * Retrieve a manager instance of the specified class.
     * This method uses the Bukkit Services API to get the registered service provider for the specified class.
     * If no provider is found, it throws a NoSuchElementException.
     * @param clazz the class of the manager to retrieve
     * @return the manager instance of the specified class
     * @param <T> the type of the manager
     */
    public <T extends Manager> T getManager(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            throw new NoSuchElementException("No provider found for " + clazz.getSimpleName() + " class.");
        }
        return provider.getProvider();
    }

    /**
     * Registers a manager instance for the specified class.
     * This method uses the Bukkit Services API to register the manager instance with normal service priority.
     * @param clazz the class of the manager to register
     * @param instance the instance of the manager to register
     * @param <I> the type of the manager interface
     * @param <T> the type of the manager implementation
     */
    public <I extends Manager, T extends I> void registerManager( Class<I> clazz, T instance) {
        getServer().getServicesManager().register(clazz, instance, this, ServicePriority.Normal);
        Logger.debug("<green>Registered manager for {} successfully", clazz.getSimpleName());
    }

    /**
     * Registers a listener for this plugin.
     * This method registers the listener with the plugin's server and logs the registration.
     *
     * @param listener the listener to register
     */
    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
        Logger.debug("<green>Registered listener {} successfully", listener.getClass().getSimpleName());
    }

    /**
     * Retrieves the settings of the specified class.
     * This method uses the Bukkit Services API to get the registered service provider for the specified settings class.
     * If no provider is found, it throws a NoSuchElementException.
     *
     * @param clazz the class of the settings to retrieve
     * @return the settings instance of the specified class
     * @param <T> the type of the settings
     */
    public abstract  <T extends Settings> T getSettings(Class<T> clazz);

    /**
     * Retrieves the RecipesAPI instance for this plugin.
     * This method uses the Bukkit Services API to get the registered service provider for RecipesAPI.
     *
     * @return the RecipesAPI instance
     */
    public abstract RecipesAPI getRecipesAPI();
}
