package fr.traqueur.morebees.api.settings;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import fr.traqueur.structura.api.Loadable;

/**
 * Marker interface for the MoreBees configuration models.
 * <p>
 * It also hosts the global registry of loaded settings instances: configurations parsed by
 * Structura are registered here once and retrieved by their class anywhere in the plugin.
 */
public interface Settings extends Loadable {

    /** Global registry of loaded settings instances, keyed by their concrete class. */
    ClassToInstanceMap<Settings> SETTINGS = MutableClassToInstanceMap.create();

    /**
     * Retrieves a settings instance by its class.
     *
     * @param clazz the settings class
     * @param <T>   the settings type
     * @return the registered settings instance, or {@code null} if none is registered
     */
    static <T extends Settings> T get(Class<T> clazz) {
        return SETTINGS.getInstance(clazz);
    }

    /**
     * Registers (or replaces) a settings instance for its class.
     *
     * @param clazz    the settings class
     * @param instance the settings instance
     * @param <T>      the settings type
     */
    static <T extends Settings> void register(Class<T> clazz, T instance) {
        SETTINGS.putInstance(clazz, instance);
    }

}
