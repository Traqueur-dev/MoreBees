package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.serialization.ToolDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

public class ToolDataTypeImpl extends ToolDataType {

    public static void init() {
        ToolDataType.INSTANCE = new ToolDataTypeImpl();
    }

    @Override
    public @NotNull String toPrimitive(ToolsManager.@NotNull Tool complex, @NotNull PersistentDataAdapterContext context) {
        return complex.name();
    }

    @Override
    public @NotNull ToolsManager.Tool fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return ToolsManager.Tool.valueOf(primitive);
    }
}
