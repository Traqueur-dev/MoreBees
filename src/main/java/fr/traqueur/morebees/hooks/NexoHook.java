package fr.traqueur.morebees.hooks;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockMechanic;
import com.nexomc.nexo.mechanics.custom_block.CustomBlockRegistry;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class NexoHook implements ItemProviderHook {
    @Override
    public @Nullable String getItemName(ItemStack item) {
        return NexoItems.idFromItem(item);
    }

    @Override
    public @Nullable String getBlockName(Block block) {
        CustomBlockMechanic blockMechanic = CustomBlockRegistry.INSTANCE.getMechanic(block);
        if (blockMechanic != null) {
            return blockMechanic.getItemID();
        }
        return null;
    }

    @Override
    public void onEnable(BeePlugin plugin) {
        Logger.success("ItemsAdder hook enabled successfully!");
    }
}
