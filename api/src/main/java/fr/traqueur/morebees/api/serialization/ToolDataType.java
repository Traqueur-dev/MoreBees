package fr.traqueur.morebees.api.serialization;

import fr.traqueur.morebees.api.models.Tool;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class ToolDataType implements PersistentDataType<String, Tool> {

    public static ToolDataType INSTANCE;

    @Override
    public @NotNull Class<Tool> getComplexType() {
        return Tool.class;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }
}
