package fr.traqueur.morebees.api.serialization;

import fr.traqueur.morebees.api.managers.ToolsManager;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class ToolDataType implements PersistentDataType<String, ToolsManager.Tool> {

    public static ToolDataType INSTANCE;

    @Override
    public @NotNull Class<ToolsManager.Tool> getComplexType() {
        return ToolsManager.Tool.class;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }
}
