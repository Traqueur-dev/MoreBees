package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.Upgrade;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type for serializing and deserializing {@link Upgrade} objects using Bukkit's
 * persistent data API.
 * <p>
 * This class provides the necessary methods to convert between {@link String} and {@link Upgrade}.
 */
public abstract class UpgradeDataType implements PersistentDataType<String, Upgrade> {

    /**
     * Singleton instance of the {@link UpgradeDataType}.
     */
    public static UpgradeDataType INSTANCE;

    /**
     * Returns the class type of the complex data type, which is {@link Upgrade}.
     * @return the class type of the complex data type
     */
    @Override
    public @NotNull Class<Upgrade> getComplexType() {
        return Upgrade.class;
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
