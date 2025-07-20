package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.BeePlugin;
import fr.traqueur.morebees.models.BeeType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

public class BeeTypeDataTypeImpl extends BeeTypeDataType {

    public static void init(@NotNull BeePlugin plugin) {
        BeeTypeDataType.INSTANCE = new BeeTypeDataTypeImpl(plugin);
    }

    private final BeePlugin plugin;

    private BeeTypeDataTypeImpl(@NotNull BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull BeeType complex, @NotNull PersistentDataAdapterContext context) {
        return complex.type();
    }

    @Override
    public @NotNull BeeType fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return plugin.getSettings().bees().stream().filter(beeType -> beeType.type().equals(primitive)).findFirst().orElseThrow();
    }
}
