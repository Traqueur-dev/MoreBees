package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.MapDataType;
import fr.traqueur.morebees.api.serialization.datas.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.datas.BeehiveDataType;
import fr.traqueur.morebees.models.BeehiveImpl;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BeehiveDataTypeImpl extends BeehiveDataType {

    private static final MapDataType<BeeType, Integer> TYPE = new MapDataType<>(HashMap::new,BeeTypeDataType.INSTANCE, PersistentDataType.INTEGER);

    public static void init() {
        BeehiveDataType.INSTANCE = new BeehiveDataTypeImpl();
    }
    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Beehive complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        Keys.INTERNAL_BEEHIVE_BEE_TYPES.set(container, TYPE, complex.getHoneyCombCounts());
        return container;
    }

    @Override
    public @NotNull Beehive fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        Map<BeeType, Integer> honeyCombCounts = Keys.INTERNAL_BEEHIVE_BEE_TYPES.get(primitive, TYPE, new HashMap<>());
        return new BeehiveImpl(honeyCombCounts);
    }
}
