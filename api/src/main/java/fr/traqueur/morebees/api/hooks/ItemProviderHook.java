package fr.traqueur.morebees.api.hooks;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

/**
 * ItemProviderHook is a hook interface that allows plugins to provide custom item names and item stacks.
 * This can be used to retrieve item names from ItemStacks or Blocks, and to create ItemStacks from product IDs.
 */
public interface ItemProviderHook extends Hook {

    /**
     * Gets the name of the item from the given ItemStack.
     *
     * @param item The ItemStack to get the name from.
     * @return The name of the item, or null if not available.
     */
    @Nullable String getItemName(ItemStack item);

    /**
     * Gets the name of the block from the given Block.
     *
     * @param block The Block to get the name from.
     * @return The name of the block, or null if not available.
     */
    @Nullable String getBlockName(Block block);

    /**
     * Gets an ItemStack from the given ID.
     *
     * @param id The product ID to get the ItemStack from.
     * @return The ItemStack corresponding to the product ID, or null if not available.
     */
    ItemStack getItemFromId(String id);
}
