package fr.traqueur.morebees.api.managers;

import fr.traqueur.morebees.api.Manager;
import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Tool;
import org.bukkit.entity.Bee;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public interface ToolsManager extends Manager {

    Optional<Tool> getTool(ItemStack itemStack);

    boolean isFull(ItemStack itemStack);

    BeeData toData(Bee bee, BeeType beeType);

    void catchBee(ItemStack tool, Bee bee, BeeType beeType);

    List<BeeData> releaseBee(ItemStack tool, boolean all);



}
