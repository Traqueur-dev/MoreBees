package fr.traqueur.morebees.api.models;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface Beehive {
    Map<BeeType, Integer> getHoneyCombCounts();

    int getHoneyCombCount(BeeType beeType);

    void addHoney(BeeType beeType, int i);

    void removeHoney(BeeType beeType, int i);

    void patch(ItemStack itemStack);
}
