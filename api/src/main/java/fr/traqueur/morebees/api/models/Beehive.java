package fr.traqueur.morebees.api.models;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public interface Beehive {

    Upgrade getUpgrade();

    void setUpgrade(Upgrade upgrade);

    UUID getUpgradeId();

    void setUpgradeId(UUID upgradeId);

    Map<BeeType, Integer> getHoneyCombCounts();

    int getHoneyCombCount(BeeType beeType);

    void addHoney(BeeType beeType, int i);

    void removeHoney(BeeType beeType, int i);

    ItemStack patch(ItemStack item);
}
