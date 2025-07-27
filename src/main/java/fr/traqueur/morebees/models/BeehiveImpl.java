package fr.traqueur.morebees.models;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.BeehiveDataType;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeehiveImpl implements Beehive {

    private final Map<BeeType, Integer> honeyCombCounts;
    private @NotNull Upgrade upgrade;

    public BeehiveImpl() {
        this.honeyCombCounts = new HashMap<>();
        this.upgrade = Upgrade.NONE;
    }

    public BeehiveImpl(Map<BeeType, Integer> honeyCombCounts, @NotNull Upgrade upgrade) {
        this.honeyCombCounts = honeyCombCounts;
        this.upgrade = upgrade;
    }

    @Override
    public @NotNull Upgrade getUpgrade() {
        return upgrade;
    }

    @Override
    public void setUpgrade(@NotNull Upgrade upgrade) {
        this.upgrade = upgrade;
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
    public ItemStack patch(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return item;
        }

        ItemStack patchedItem = item.clone();
        patchedItem.editMeta(itemMeta -> {
            PersistentDataContainer container = itemMeta.getPersistentDataContainer();
            Keys.BEEHIVE.set(container, BeehiveDataType.INSTANCE, this);
            List<String> additionalLore = BeePlugin.getPlugin(BeePlugin.class).getSettings(GlobalSettings.class).beehiveLore();
            if(!this.upgrade.equals(Upgrade.NONE) && additionalLore != null) {
                itemMeta.lore(Util.parseLore(additionalLore, this.upgrade.formatters()));
            }
        });
        return patchedItem;
    }

}
