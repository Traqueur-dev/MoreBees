package fr.traqueur.morebees.api.serialization;

import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public enum Keys {


    BEETYPE,
    BEEHIVE,

    INTERNAL_BEEHIVE_BEETYPES,
    INTERNAL_BEEHIVE_HONEY_COUNTS,
    HONEY_LEVEL, TOOL_ID;

    private static final BeePlugin PLUGIN = JavaPlugin.getPlugin(BeePlugin.class);

    public <T> Optional<T> get(PersistentDataContainer container, PersistentDataType<?,T> type) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        return Optional.ofNullable(container.get(key, type));
    }

    public <T> T get(PersistentDataContainer container, PersistentDataType<?,T> type, T def) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        return container.getOrDefault(key, type, def);
    }

    public <T> void set(PersistentDataContainer container, PersistentDataType<?,T> type, T value) {
        NamespacedKey key = new NamespacedKey(PLUGIN, name().toLowerCase());
        container.set(key, type, value);
    }

}