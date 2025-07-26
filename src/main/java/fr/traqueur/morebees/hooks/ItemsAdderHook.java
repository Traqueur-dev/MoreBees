package fr.traqueur.morebees.hooks;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemsAdderHook implements ItemProviderHook {
    @Override
    public @Nullable String getItemName(ItemStack item) {
        CustomStack stack = CustomStack.byItemStack(item);
        if(stack == null) {
            return null;
        }
        return stack.getNamespacedID();
    }

    @Override
    public @Nullable String getBlockName(Block block) {
        CustomBlock customBlock = CustomBlock.byAlreadyPlaced(block);
        if(customBlock != null) {
            return customBlock.getNamespacedID();
        }
        return null;
    }

    @Override
    public ItemStack getItemFromId(String product) {
        CustomStack stack = CustomStack.getInstance(product);
        if(stack == null) {
            return null;
        }
        return stack.getItemStack();
    }

    @Override
    public void onEnable(BeePlugin plugin) {
        Logger.success("ItemsAdder hook enabled successfully!");
    }
}
