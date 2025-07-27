package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type for serializing and deserializing {@link BeeType} objects using Bukkit's
 * persistent data API.
 * <p>
 * This class provides the necessary methods to convert between {@link String} and {@link BeeType}.
 */
public abstract class BeeTypeDataType implements PersistentDataType<String, BeeType> {

    /**
     * Singleton instance of the {@link BeeTypeDataType}.
     */
    public static BeeTypeDataType INSTANCE;

    /**
     * Returns the class type of the complex data type, which is {@link BeeType}.
     * @return the class type of the complex data type
     */
    @Override
    public @NotNull Class<BeeType> getComplexType() {
        return BeeType.class;
    }

    /**
     * Returns the class type of the primitive data type, which is {@link String}.
     * @return the class type of the primitive data type
     */
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }
}
