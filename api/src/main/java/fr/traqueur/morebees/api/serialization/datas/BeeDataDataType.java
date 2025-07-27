package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.BeeData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type for serializing and deserializing {@link BeeData} objects using Bukkit's
 * persistent data API.
 * <p>
 * This class provides the necessary methods to convert between {@link PersistentDataContainer}
 * and {@link BeeData}.
 */
public abstract class BeeDataDataType implements PersistentDataType<PersistentDataContainer, BeeData> {

    /**
     * Singleton instance of the {@link BeeDataDataType}.
     */
    public static BeeDataDataType INSTANCE;

    /**
     * Returns the class type of the complex data type, which is {@link BeeData}.
     * @return the class type of the complex data type
     */
    @Override
    public @NotNull Class<BeeData> getComplexType() {
        return BeeData.class;
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
