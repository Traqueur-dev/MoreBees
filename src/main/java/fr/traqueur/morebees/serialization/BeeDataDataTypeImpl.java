package fr.traqueur.morebees.serialization;

import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.BeeDataDataType;
import fr.traqueur.morebees.api.serialization.datas.BeeTypeDataType;
import fr.traqueur.morebees.models.BeeDataImpl;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class BeeDataDataTypeImpl extends BeeDataDataType {

    public static void init() {
        BeeDataDataType.INSTANCE = new BeeDataDataTypeImpl();
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull BeeData complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        Keys.INTERNAL_BEE_DATA_BEE_TYPE.set(container, BeeTypeDataType.INSTANCE, complex.type());
        Keys.INTERNAL_BEE_DATA_HAS_NECTAR.set(container, PersistentDataType.BOOLEAN, complex.hasNectar());
        Keys.INTERNAL_BEE_DATA_IS_ADULT.set(container, PersistentDataType.BOOLEAN, complex.isAdult());
        return container;
    }

    @Override
    public @NotNull BeeData fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        BeeType beeType = Keys.INTERNAL_BEE_DATA_BEE_TYPE.get(primitive, BeeTypeDataType.INSTANCE).orElse(BeeType.NORMAL);
        boolean hasNectar = Keys.INTERNAL_BEE_DATA_HAS_NECTAR.get(primitive, PersistentDataType.BOOLEAN).orElse(false);
        boolean isAdult = Keys.INTERNAL_BEE_DATA_IS_ADULT.get(primitive, PersistentDataType.BOOLEAN).orElse(true);

        return new BeeDataImpl(beeType, hasNectar, isAdult);
    }
}
