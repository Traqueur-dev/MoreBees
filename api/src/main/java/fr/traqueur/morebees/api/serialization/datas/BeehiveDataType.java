package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.Beehive;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type for serializing and deserializing {@link Beehive} objects using Bukkit's
 * persistent data API.
 * <p>
 * This class provides the necessary methods to convert between {@link PersistentDataContainer}
 * and {@link Beehive}.
 */
public abstract class BeehiveDataType implements PersistentDataType<PersistentDataContainer, Beehive> {

    /**
     * Singleton instance of the {@link BeehiveDataType}.
     */
    public static BeehiveDataType INSTANCE;

    /**
     * Returns the class type of the complex data type, which is {@link Beehive}.
     * @return the class type of the complex data type
     */
    @Override
    public @NotNull Class<Beehive> getComplexType() {
        return Beehive.class;
    }

    /**
     * Returns the class type of the primitive data type, which is {@link PersistentDataContainer}.
     * @return the class type of the primitive data type
     */
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }
}
