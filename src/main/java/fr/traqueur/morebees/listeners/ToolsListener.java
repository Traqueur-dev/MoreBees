package fr.traqueur.morebees.listeners;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.entity.Bee;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ToolsListener implements Listener {

    private final BeePlugin plugin;

    public ToolsListener(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        BeeManager beeManager = plugin.getManager(BeeManager.class);
        ToolsManager toolsManager = plugin.getManager(ToolsManager.class);

        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if(!(event.getRightClicked() instanceof Bee bee)) {
            return;
        }

        ItemStack toolItem = event.getPlayer().getInventory().getItemInMainHand();
        Optional<ToolsManager.Tool> toolOpt = toolsManager.getTool(toolItem);
         if (toolOpt.isEmpty() || toolsManager.isFull(toolItem)) {
             return;
        }

        Util.ifBothPresent(beeManager.getBeeTypeFromEntity(bee), toolOpt, (beeType, tool) -> {
            toolsManager.catchBee(toolItem, bee, beeType);
            Logger.debug("Caught bee {} of type {} with tool {}",
                    bee.getUniqueId(),
                    beeType.type(),
                    tool.name());
        });

    }

}
