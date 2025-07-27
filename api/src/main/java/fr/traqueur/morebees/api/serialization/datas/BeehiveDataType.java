package fr.traqueur.morebees.api.serialization.datas;

import fr.traqueur.morebees.api.models.Beehive;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public abstract class BeehiveDataType implements PersistentDataType<PersistentDataContainer, Beehive> {

    public static BeehiveDataType INSTANCE;

    @Override
    public @NotNull Class<Beehive> getComplexType() {
        return Beehive.class;
    }

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }
}
