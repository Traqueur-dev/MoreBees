package fr.traqueur.morebees.hooks;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.Hook;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public enum Hooks {

    MODEL_ENGINE("ModelEngine", ModelEngineHook::new),
    ;

    private final String pluginName;
    private final Supplier<? extends Hook> hookSupplier;
    private Hook hook;

    private static final Set<Hooks> TO_RETRY = new HashSet<>();

    Hooks(String pluginName, Supplier<? extends Hook> hookSupplier) {
        this.pluginName = pluginName;
        this.hookSupplier = hookSupplier;
    }


    public <T extends Hook> Optional<T> get() {
        //noinspection unchecked
        return Optional.ofNullable((T) hook);
    }

    private boolean init() {
        if (hook == null && Bukkit.getPluginManager().getPlugin(pluginName) != null) {
            hook = hookSupplier.get();
            return true;
        }
        return false;
    }

    public static void initAll(BeePlugin plugin) {
        for (Hooks hooks : Hooks.values()) {
            if (hooks.init()) {
                hooks.get().ifPresent(hook -> enableHook(hooks.pluginName, hook, plugin));
            } else {
                TO_RETRY.add(hooks);
            }
        }
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Hooks hooks : TO_RETRY) {
                if (hooks.init()) {
                    hooks.get().ifPresent(hook -> enableHook(hooks.pluginName, hook, plugin));
                } else {
                    Logger.debug("{} hook failed to initialize...", hooks.pluginName);
                }
            }
            TO_RETRY.clear();
        });
    }

    private static void enableHook(String hookName, Hook hook, BeePlugin plugin) {
        try {
            hook.onEnable(plugin);
        } catch (Exception e) {
            Logger.severe("Failed to enable hook for {}",e, hookName);
        }
    }
}
