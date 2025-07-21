package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

public record BeeType(String type, int modelId, String displayName, Material food, Material flower, String model) {

    public ItemStack egg() {
        ItemStack item = new ItemStack(Material.BEE_SPAWN_EGG);
        item.editMeta(meta -> {
            meta.itemName(MiniMessageHelper.parse(displayName + " Egg"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEETYPE.set(container, BeeTypeDataType.INSTANCE, this);
            if(modelId > 0) {
                meta.setCustomModelData(modelId);
            }
        });
        return item;
    }

}
