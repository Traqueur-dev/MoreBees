package fr.traqueur.morebees.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.BeehiveDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.models.BeehiveImpl;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BeehiveDataTypeImpl extends BeehiveDataType {

    public static void init(BeePlugin plugin) {
        BeehiveDataType.INSTANCE = new BeehiveDataTypeImpl(plugin);
    }

    private final BeePlugin plugin;

    private BeehiveDataTypeImpl(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull Beehive complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();

        Map<BeeType, Integer> honeyCombCounts = complex.getHoneyCombCounts();

        List<String> beeTypeIds = new ArrayList<>(honeyCombCounts.size());
        List<Integer> counts = new ArrayList<>(honeyCombCounts.size());

        for (Map.Entry<BeeType, Integer> entry : honeyCombCounts.entrySet()) {
            beeTypeIds.add(entry.getKey().type());
            counts.add(entry.getValue());
        }

        Keys.INTERNAL_BEEHIVE_BEETYPES.set(container, PersistentDataType.LIST.strings(), beeTypeIds);
        Keys.INTERNAL_BEEHIVE_HONEY_COUNTS.set(container, PersistentDataType.LIST.integers(), counts);

        return container;
    }

    @Override
    public @NotNull Beehive fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        List<String> beeTypeIds = Keys.INTERNAL_BEEHIVE_BEETYPES.get(primitive, PersistentDataType.LIST.strings(), new ArrayList<>());
        List<Integer> honeyCountsList = Keys.INTERNAL_BEEHIVE_HONEY_COUNTS.get(primitive, PersistentDataType.LIST.integers(),new ArrayList<>());
        if (beeTypeIds.size() != honeyCountsList.size()) {
            throw new IllegalArgumentException("Bee type IDs and honey counts must have the same length");
        }

        List<BeeType> beeTypes = beeTypeIds.stream().map(id -> this.plugin.getSettings(GlobalSettings.class).getBeeType(id).orElseThrow()).toList();

        Map<BeeType, Integer> honeyCombCounts = new HashMap<>();

        for (int i = 0; i < beeTypes.size(); i++) {
            honeyCombCounts.put(beeTypes.get(i), honeyCountsList.get(i));
        }

        return new BeehiveImpl(honeyCombCounts);
    }
}
