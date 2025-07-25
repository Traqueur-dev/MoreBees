package fr.traqueur.morebees.models;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.serialization.BeehiveDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BeehiveImpl implements Beehive {

    private final Map<BeeType, Integer> honeyCombCounts;

    public BeehiveImpl() {
        this.honeyCombCounts = new HashMap<>();
    }

    public BeehiveImpl(Map<BeeType, Integer> honeyCombCounts) {
        this.honeyCombCounts = honeyCombCounts;
    }

    @Override
    public Map<BeeType, Integer> getHoneyCombCounts() {
        return honeyCombCounts;
    }

    @Override
    public int getHoneyCombCount(BeeType beeType) {
        return honeyCombCounts.getOrDefault(beeType, 0);
    }

    @Override
    public void addHoney(BeeType beeType, int i) {
        honeyCombCounts.merge(beeType, 1, Integer::sum);
    }

    @Override
    public void removeHoney(BeeType beeType, int i) {
        honeyCombCounts.merge(beeType, -i, Integer::sum);
        if (honeyCombCounts.get(beeType) <= 0) {
            honeyCombCounts.remove(beeType);
        }
    }

    @Override
    public void patch(ItemStack itemStack) {
        itemStack.editMeta(itemMeta -> {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            Keys.BEEHIVE.get(container, BeehiveDataType.INSTANCE, this);
        });
    }

}
