package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.BeeData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class BeeDataDataType implements PersistentDataType<PersistentDataContainer, BeeData> {

    public static BeeDataDataType INSTANCE;

    @Override
    public @NotNull Class<BeeData> getComplexType() {
        return BeeData.class;
    }

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }
}
