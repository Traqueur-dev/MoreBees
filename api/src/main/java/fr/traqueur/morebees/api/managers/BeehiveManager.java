package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.Beehive;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

public interface BeehiveManager extends Manager {
    Optional<Beehive> getBeehiveFromBlock(BlockState block);

    Optional<Beehive> getBeehiveFromItem(ItemStack stack);

    void saveBeehiveToBlock(Block block, Beehive beehive);

    void editBeehive(Block block, Consumer<Beehive> consumer);
}
