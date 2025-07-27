package fr.traqueur.morebees.api.hooks;

import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.entity.Bee;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a hook that can be enabled in the MoreBees plugin.
 * Hooks are used to integrate with other plugins or systems.
 */
public interface Hook {

    /**
     * A set of all registered hooks.
     */
    Set<Hook> HOOKS = new HashSet<>();

    /**
     * Get all hooks of a specific type.
     * @param type the class of the hook type to retrieve
     * @return a set of hooks of the specified type
     * @param <T> the type of the hook
     */
     static <T extends Hook> Set<T> getByClass(Class<T> type) {
        Set<T> hooks = new HashSet<>();
        for (Hook hook : HOOKS) {
            if (type.isInstance(hook)) {
                hooks.add(type.cast(hook));
            }
        }
        return hooks;
    }

    /**
     * Register a new hook.
     * This method is called to register a hook in the MoreBees plugin.
     *
     * @param hook the hook to register
     */
    static void register(Hook hook) {
        HOOKS.add(hook);
        hook.onEnable(BeePlugin.getPlugin(BeePlugin.class));
    }

    /**
     * Enable the hook for the specified plugin.
     * This method is called when the plugin is enabled.
     *
     * @param plugin the plugin that is enabling the hook
     */
    void onEnable(BeePlugin plugin);
}
