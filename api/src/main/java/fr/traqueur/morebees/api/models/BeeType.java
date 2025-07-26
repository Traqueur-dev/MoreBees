package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public record BeeType(String type, int modelId, String displayName, List<String> foods, List<String> flowers, String product, @Nullable String model) {

    public BeeType {
        if(foods.isEmpty()) {
            Logger.warning("No foods defined for the bee type {}", type);
        }
        if(flowers.isEmpty()) {
            Logger.warning("No flowers defined for the bee type {}", type);
        }
    }

    public ItemStack productItem() {
        return Hook.getByClass(ItemProviderHook.class)
                .stream()
                .map(hook -> hook.getItemFromId(product))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(ItemStack.of(Material.valueOf(product)));
    }

    public ItemStack egg() {
        ItemStack item = new ItemStack(Material.BEE_SPAWN_EGG);
        item.editMeta(meta -> {
            meta.itemName(MiniMessageHelper.parse(displayName + " Egg<reset>"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEETYPE.set(container, BeeTypeDataType.INSTANCE, this);
            if(modelId > 0) {
                meta.setCustomModelData(modelId);
            }
        });
        return item;
    }

    public ItemStack honey(int amount, boolean block) {
        Material material = block ? Material.HONEYCOMB_BLOCK : Material.HONEYCOMB;
        ItemStack item = new ItemStack(material, amount);
        item.editMeta(meta -> {
            meta.itemName(MiniMessageHelper.parse(displayName + " Honey<reset>"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEETYPE.set(container, BeeTypeDataType.INSTANCE, this);
            if(modelId > 0) {
                meta.setCustomModelData(modelId);
            }
        });
        return item;
    }

    public boolean isFood(ItemStack item) {
        Material type = item.getType();
        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        String itemName = hooks.stream().map(hook -> hook.getItemName(item)).filter(Objects::nonNull).findFirst().orElse(type.name());
        return foods.contains(itemName);
    }

    public boolean isFlower(Block block) {
       return Util.isValidBlock(block, flowers);
    }
}
