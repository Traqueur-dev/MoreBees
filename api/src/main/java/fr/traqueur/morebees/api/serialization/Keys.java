package fr.traqueur.morebees.api.serialization;

import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

/**
 * Enum representing the keys used for persistent data storage in the More Bees plugin.
 * Each key corresponds to a specific piece of data that can be stored in a {@link PersistentDataContainer}.
 */
public enum Keys {

    BEE_TYPE,

    BEEHIVE,

    INTERNAL_BEEHIVE_BEE_TYPES,
    INTERNAL_BEEHIVE_UPGRADE,
    INTERNAL_BEEHIVE_DISPLAY_ID,

    TOOL_ID,
    MAX_BEES,
    BEES,

    INTERNAL_BEE_DATA_BEE_TYPE,
    INTERNAL_BEE_DATA_HAS_NECTAR,
    INTERNAL_BEE_DATA_IS_ADULT,
    UPGRADE_ID;

    private static final BeePlugin PLUGIN = JavaPlugin.getPlugin(BeePlugin.class);

    /**
     * Retrieves the value associated with this key from the given {@link PersistentDataContainer}.
     * The value is retrieved using the {@link PersistentDataType} provided.
     * @param container the {@link PersistentDataContainer} from which to retrieve the value
     * @param type the {@link PersistentDataType} that defines how to interpret the stored data
     * @return an {@link Optional} containing the value if it exists, or empty if it does not
     * @param <T> the type of the value to retrieve
     */
    public <T> Optional<T> get(PersistentDataContainer container, PersistentDataType<?,T> type) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        return Optional.ofNullable(container.get(key, type));
    }

    /**
     * Retrieves the value associated with this key from the given {@link PersistentDataContainer}.
     * If the value does not exist, the provided default value is returned.
     * @param container the {@link PersistentDataContainer} from which to retrieve the value
     * @param type the {@link PersistentDataType} that defines how to interpret the stored data
     * @param def the default value to return if the key does not exist
     * @return the value associated with this key, or the default value if it does not exist
     * @param <T> the type of the value to retrieve
     */
    public <T> T get(PersistentDataContainer container, PersistentDataType<?,T> type, T def) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        return container.getOrDefault(key, type, def);
    }

    /**
     * Sets the value associated with this key in the given {@link PersistentDataContainer}.
     * The value is stored using the provided {@link PersistentDataType}.
     * @param container the {@link PersistentDataContainer} in which to store the value
     * @param type the {@link PersistentDataType} that defines how to store the data
     * @param value the value to store
     * @param <T> the type of the value to store
     */
    public <T> void set(PersistentDataContainer container, PersistentDataType<?,T> type, T value) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        container.set(key, type, value);
    }

}