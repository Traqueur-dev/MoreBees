package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.Tool;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a data type for serializing and deserializing {@link Tool} objects using Bukkit's
 * persistent data API.
 * <p>
 * This class provides the necessary methods to convert between {@link String} and {@link Tool}.
 */
public abstract class ToolDataType implements PersistentDataType<String, Tool> {

    /**
     * Singleton instance of the {@link ToolDataType}.
     */
    public static ToolDataType INSTANCE;

    /**
     * Returns the class type of the complex data type, which is {@link Tool}.
     * @return the class type of the complex data type
     */
    @Override
    public @NotNull Class<Tool> getComplexType() {
        return Tool.class;
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
