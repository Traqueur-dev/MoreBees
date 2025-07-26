package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.serialization.*;
import fr.traqueur.morebees.models.BeehiveImpl;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

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
