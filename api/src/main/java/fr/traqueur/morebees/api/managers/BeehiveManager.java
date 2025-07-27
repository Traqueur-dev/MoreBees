package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.Beehive;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Manages beehives in the MoreBees API.
 * Provides methods to retrieve, save, and edit beehives from blocks and items.
 */
public interface BeehiveManager extends Manager {

    /**
     * Retrieves a Beehive from a BlockState.
     *
     * @param block The BlockState to retrieve the Beehive from.
     * @return An Optional containing the Beehive if present, otherwise empty.
     */
    Optional<Beehive> getBeehiveFromBlock(BlockState block);

    /**
     * Retrieves a Beehive from an ItemStack.
     *
     * @param stack The ItemStack to retrieve the Beehive from.
     * @return An Optional containing the Beehive if present, otherwise empty.
     */
    Optional<Beehive> getBeehiveFromItem(ItemStack stack);

    /**
     * Saves a Beehive to a Block.
     *
     * @param block   The Block to save the Beehive to. The block must be a valid beehive block.
     *                If the block is not a valid beehive block, nothing will happen.
     *                It is recommended to check if the block is a beehive using {@link org.bukkit.block.Beehive}.
     * @param beehive The Beehive to save.
     */
    void saveBeehiveToBlock(Block block, Beehive beehive);

    /**
     * Edits a Beehive from a Block.
     * This method calls {@link fr.traqueur.morebees.api.managers.BeehiveManager#getBeehiveFromBlock} and then applies the provided Consumer to the Beehive and saves it back to the block with {@link fr.traqueur.morebees.api.managers.BeehiveManager#saveBeehiveToBlock}.
     * @param block    The Block to edit the Beehive from. The block must be a valid beehive block.
     *                 If the block is not a valid beehive block, nothing will happen.
     *                 It is recommended to check if the block is a beehive using {@link org.bukkit.block.Beehive}.
     * @param consumer A Consumer that takes the Beehive and allows modifications.
     */
    void editBeehive(Block block, Consumer<Beehive> consumer);
}
