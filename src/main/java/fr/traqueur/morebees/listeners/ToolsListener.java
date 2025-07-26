package fr.traqueur.morebees.listeners;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.Messages;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.managers.ToolsManager;
import fr.traqueur.morebees.api.models.BeeData;
import fr.traqueur.morebees.api.util.Formatter;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ToolsListener implements Listener {

    private final BeePlugin plugin;
    private final Set<UUID> catchers;

    public ToolsListener(BeePlugin plugin) {
        this.plugin = plugin;
        this.catchers = new HashSet<>();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }

        if(this.catchers.contains(event.getPlayer().getUniqueId())) {
            return;
        }

        if(!event.getAction().isRightClick()) {
            return;
        }

        Location location = event.getClickedBlock() != null ? event.getClickedBlock().getLocation() : event.getPlayer().getLocation();
        if (event.getClickedBlock() != null) {
            location = location.add(event.getBlockFace().getDirection());
        }
        location = location.add(0.5, 0, 0.5);

        Location finalLocation = location;
        ItemStack toolItem = event.getPlayer().getInventory().getItemInMainHand();
        ToolsManager toolsManager = plugin.getManager(ToolsManager.class);
        BeeManager beeManager = plugin.getManager(BeeManager.class);

        toolsManager.getTool(toolItem).ifPresent(tool -> {
            List<BeeData> released = toolsManager.releaseBee(toolItem, event.getPlayer().isSneaking());
            for (BeeData beeData : released) {
                beeManager.spawnBee(finalLocation, beeData.type(), CreatureSpawnEvent.SpawnReason.CUSTOM, !beeData.isAdult(), beeData.hasNectar());
                Logger.debug("Released bee of type {} with tool {} at location {}",
                        beeData.type().type(),
                        tool.name(),
                        finalLocation);
            }
        });

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
         if (toolOpt.isEmpty()) {
             return;
        }

        if(toolsManager.isFull(toolItem)) {
            Messages.TOOL_FULL.send(event.getPlayer(), Formatter.format("tool", MiniMessageHelper.unparse(toolItem.getItemMeta().itemName())));
            return;
        }

        Util.ifBothPresent(beeManager.getBeeTypeFromEntity(bee), toolOpt, (beeType, tool) -> {
            toolsManager.catchBee(toolItem, bee, beeType);
            this.catchers.add(event.getPlayer().getUniqueId());
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> this.catchers.remove(event.getPlayer().getUniqueId()), 5L);
            event.setCancelled(true);
            Logger.debug("Caught bee {} of type {} with tool {}",
                    bee.getUniqueId(),
                    beeType.type(),
                    tool.name());
        });
    }

}
