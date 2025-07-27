package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.models.Tool;
import fr.traqueur.morebees.api.serialization.datas.ToolDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

public class ToolDataTypeImpl extends ToolDataType {

    public static void init() {
        ToolDataType.INSTANCE = new ToolDataTypeImpl();
    }

    @Override
    public @NotNull String toPrimitive(@NotNull Tool complex, @NotNull PersistentDataAdapterContext context) {
        return complex.name();
    }

    @Override
    public @NotNull Tool fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return Tool.valueOf(primitive);
    }
}
