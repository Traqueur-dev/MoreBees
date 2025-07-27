package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Tool;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * ToolsManager is responsible for managing tools related to bees.
 * It provides methods to get tools, check if a tool is full, and manage bee data.
 */
public interface ToolsManager extends Manager {

    /**
     * Gets the tool associated with the given ItemStack.
     *
     * @param itemStack the ItemStack to check
     * @return an Optional containing the Tool if present, otherwise empty
     */
    Optional<Tool> getTool(ItemStack itemStack);

    /**
     * Checks if the given ItemStack is a full tool.
     *
     * @param itemStack the ItemStack to check
     * @return true if the tool is full, false otherwise
     */
    boolean isFull(ItemStack itemStack);

    /**
     * Converts a Bee and BeeType into a BeeData object.
     *
     * @param bee the Bee to convert
     * @param beeType the type of the Bee
     * @return a BeeData object representing the Bee and its type
     */
    BeeData toData(Bee bee, BeeType beeType);

    /**
     * Catches a Bee using the specified tool.
     *
     * @param tool the ItemStack representing the tool
     * @param bee the Bee to catch
     * @param beeType the type of the Bee
     */
    void catchBee(ItemStack tool, Bee bee, BeeType beeType);

    /**
     * Releases bees from the specified tool.
     *
     * @param tool the ItemStack representing the tool
     * @param all if true, releases all bees; otherwise, releases only one
     * @return a list of BeeData representing the released bees
     * You must spawn the bees manually using the content of BeeData and the {@link fr.traqueur.morebees.api.managers.BeeManager#spawnBee}
     */
    List<BeeData> releaseBee(ItemStack tool, boolean all);

}
