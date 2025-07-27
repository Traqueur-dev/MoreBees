package fr.traqueur.morebees.api;

/**
 * Manager interface for the MoreBees API.
 * This interface provides a method to access the main plugin instance.
 * It is intended to be implemented by classes that manage or interact with the plugin.
 */
public interface Manager {

    /**
     * Gets the main plugin instance.
     * This method provides access to the main plugin instance of MoreBees.
     *
     * @return The main plugin instance.
     */
    default BeePlugin getPlugin() {
        return BeePlugin.getPlugin(BeePlugin.class);
    }

}
