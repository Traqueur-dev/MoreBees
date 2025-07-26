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

public abstract class BeePlugin extends JavaPlugin {

    public abstract CommandManager<@NotNull BeePlugin> getCommandManager();

    public <T extends Manager> T getManager(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            throw new NoSuchElementException("No provider found for " + clazz.getSimpleName() + " class.");
        }
        return provider.getProvider();
    }

    public abstract  <T extends Settings> T getSettings(Class<T> clazz);

    public <I extends Manager, T extends I> void registerManager( Class<I> clazz, T instance) {
        getServer().getServicesManager().register(clazz, instance, this, ServicePriority.Normal);
        Logger.debug("<green>Registered manager for {} successfully", clazz.getSimpleName());
    }

    public void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
        Logger.debug("<green>Registered listener {} successfully", listener.getClass().getSimpleName());
    }


    public abstract RecipesAPI getRecipesAPI();
}
