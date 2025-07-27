package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.Upgrade;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class UpgradeDataType implements PersistentDataType<String, Upgrade> {

    public static UpgradeDataType INSTANCE;

    @Override
    public @NotNull Class<Upgrade> getComplexType() {
        return Upgrade.class;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }
}
