package fr.traqueur.morebees.api.models;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public interface Beehive {
    Map<BeeType, Integer> getHoneyCombCounts();

    int getHoneyCombCount(BeeType beeType);

    void addHoney(BeeType beeType, int i);

    void removeHoney(BeeType beeType, int i);

    ItemStack patch(ItemStack item);
}
