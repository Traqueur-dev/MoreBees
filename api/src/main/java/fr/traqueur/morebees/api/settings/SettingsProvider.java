package fr.traqueur.morebees.api.settings;

/**
 * Contract for accessing the plugin configuration models.
 * <p>
 * Implemented by {@code BeePlugin}, which delegates to the global {@link Settings} registry.
 */
public interface SettingsProvider {

    /**
     * Retrieves a settings instance by its class.
     *
     * @param clazz the settings class
     * @param <T>   the settings type
     * @return the settings instance
     */
    <T extends Settings> T getSettings(Class<T> clazz);

}
