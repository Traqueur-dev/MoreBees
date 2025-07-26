package fr.traqueur.morebees.api.hooks;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public interface ItemProviderHook extends Hook {

    @Nullable String getItemName(ItemStack item);

    @Nullable String getBlockName(Block block);

    ItemStack getItemFromId(String product);
}
