package fr.traqueur.morebees.api;

/**
 * Contract for registering and retrieving the plugin managers.
 * <p>
 * Implemented by {@link BeePlugin}, which is backed by Bukkit's {@code ServicesManager}.
 */
public interface ManagerProvider {

    /**
     * Registers a manager instance for the specified interface.
     *
     * @param clazz    the manager interface to register the instance under
     * @param instance the manager implementation
     * @param <I>      the manager interface type
     * @param <T>      the manager implementation type
     */
    <I extends Manager, T extends I> void registerManager(Class<I> clazz, T instance);

    /**
     * Retrieves a registered manager by its interface.
     *
     * @param clazz the manager interface
     * @param <T>   the manager type
     * @return the registered manager instance
     */
    <T extends Manager> T getManager(Class<T> clazz);

}
