package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.api.settings.UpgradeSettings;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.jetbrains.annotations.NotNull;

public class UpgradeDataTypeImpl extends UpgradeDataType {

    public static void init(BeePlugin plugin) {
        UpgradeDataType.INSTANCE = new UpgradeDataTypeImpl(plugin);
    }

    private final BeePlugin plugin;

    private UpgradeDataTypeImpl(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String toPrimitive(@NotNull Upgrade complex, @NotNull PersistentDataAdapterContext context) {
        return complex.id();
    }

    @Override
    public @NotNull Upgrade fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return this.plugin.getSettings(UpgradeSettings.class).getUpgrade(primitive).orElse(Upgrade.NONE);
    }
}
