package fr.traqueur.morebees.api.models;

import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.BeeTypeDataType;
import fr.traqueur.morebees.api.settings.ItemStackWrapper;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import fr.traqueur.structura.annotations.Options;
import fr.traqueur.structura.api.Loadable;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public record BeeType(String type,
                      @Options(optional = true) Integer modelId,
                      String displayName,
                      List<String> foods,
                      List<String> flowers,
                      Product product,
                      @Options(optional = true) String model) implements Loadable {


    public record Product(ItemStackWrapper honey, ItemStackWrapper honeyBlock, ItemStackWrapper additional) implements Loadable {}

    public static final BeeType NORMAL = new BeeType("normal", null, "Bee", List.of(), List.of(), null, null);

    public BeeType {
        if(foods.isEmpty() && !type.equalsIgnoreCase("normal")) {
            Logger.warning("No foods defined for the bee type {}", type);
        }
        if(flowers.isEmpty() && !type.equalsIgnoreCase("normal")) {
            Logger.warning("No flowers defined for the bee type {}", type);
        }
    }

    public @NotNull ItemStack productItem() {
        return product.additional().build();
    }

    public @NotNull ItemStack egg() {
        ItemStack item = ItemStack.of(Material.BEE_SPAWN_EGG);
        if(this.equals(NORMAL)) {
            return item;
        }

        item.editMeta(meta -> {
            meta.itemName(MiniMessageHelper.parse(displayName + " Egg<reset>"));
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEE_TYPE.set(container, BeeTypeDataType.INSTANCE, this);
            if(modelId != null && modelId > 0) {
                meta.setCustomModelData(modelId);
            }
        });
        return item;
    }

    public @NotNull ItemStack honey(int amount, boolean block) {
        if(this.equals(NORMAL)) {
            Material material = block ? Material.HONEYCOMB_BLOCK : Material.HONEYCOMB;
            return ItemStack.of(material, amount);
        }

        ItemStackWrapper honeyItem = block ? product.honeyBlock() : product.honey();
        ItemStack item = honeyItem.build();
        item.setAmount(amount);
        item.editMeta(meta -> {
            PersistentDataContainer container = meta.getPersistentDataContainer();
            Keys.BEE_TYPE.set(container, BeeTypeDataType.INSTANCE, this);
        });
        return item;
    }

    public boolean isFood(@NotNull ItemStack item) {
        Material type = item.getType();

        if(this.equals(NORMAL)) {
            return Tag.FLOWERS.isTagged(type);
        }

        Set<ItemProviderHook> hooks = Hook.getByClass(ItemProviderHook.class);
        String itemName = hooks.stream().map(hook -> hook.getItemName(item)).filter(Objects::nonNull).findFirst().orElse(type.name());
        return foods.contains(itemName);
    }

    public boolean isFlower(Block block) {
        if(this.equals(NORMAL)) {
            return Tag.FLOWERS.isTagged(block.getType());
        }

       return Util.isValidBlock(block, flowers);
    }
}
