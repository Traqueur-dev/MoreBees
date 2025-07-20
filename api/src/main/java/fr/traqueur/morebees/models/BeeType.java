package fr.traqueur.morebees.models;

import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import fr.traqueur.morebees.util.MiniMessageHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public record BeeType(String type, String displayName, Material food, Material flower, String model) {

    public ItemStack egg() {
        ItemStack item = new ItemStack(Material.BEE_SPAWN_EGG);
        item.editMeta(meta -> {
            meta.itemName(MiniMessageHelper.parse(displayName + " Egg"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEETYPE.set(container, BeeTypeDataType.INSTANCE, this);
        });
        return item;
    }

}
