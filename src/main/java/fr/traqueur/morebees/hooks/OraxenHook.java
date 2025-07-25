package fr.traqueur.morebees.hooks;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.ItemProviderHook;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.mechanics.provided.gameplay.block.BlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class OraxenHook implements ItemProviderHook {
    @Override
    public @Nullable String getItemName(ItemStack item) {
        return OraxenItems.getIdByItem(item);
    }

    @Override
    public @Nullable String getBlockName(Block block) {
        BlockMechanic blockMechanic = OraxenBlocks.getBlockMechanic(block);
        if (blockMechanic != null) {
            return blockMechanic.getItemID();
        }
        NoteBlockMechanic noteBlockMechanic = OraxenBlocks.getNoteBlockMechanic(block);
        if (noteBlockMechanic != null) {
            return noteBlockMechanic.getItemID();
        }
        return null;
    }

    @Override
    public void onEnable(BeePlugin plugin) {
        Logger.success("ItemsAdder hook enabled successfully!");
    }
}
