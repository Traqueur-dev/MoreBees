package fr.traqueur.morebees.api.serialization;

import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class BeeTypeDataType implements PersistentDataType<String, BeeType> {

    public static BeeTypeDataType INSTANCE;

    @Override
    public @NotNull Class<BeeType> getComplexType() {
        return BeeType.class;
    }

    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }
}
