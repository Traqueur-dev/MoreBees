package fr.traqueur.morebees;

import fr.traqueur.commands.spigot.CommandManager;
import fr.traqueur.morebees.settings.Settings;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

public abstract class BeePlugin extends JavaPlugin {

    public abstract Settings getSettings();

    public <T> T getManager(Class<T> clazz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(clazz);
        if (provider == null) {
            throw new NoSuchElementException("No provider found for " + clazz.getSimpleName() + " class.");
        }
        return provider.getProvider();
    }

    public <I, T extends I> void registerManager( Class<I> clazz, T instance) {
        getServer().getServicesManager().register(clazz, instance, this, ServicePriority.Normal);
        Logger.debug("Registered manager for {} successfully", clazz.getSimpleName());
    }

    public abstract CommandManager<@NotNull BeePlugin> getCommandManager();
}
