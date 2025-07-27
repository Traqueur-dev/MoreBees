package fr.traqueur.morebees.listeners;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Beehive;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.HashMap;
import java.util.Map;

public class BeehiveListener implements Listener {

    private final BeePlugin plugin;

    public BeehiveListener(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDrop(BlockDropItemEvent event) {
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);
        beehiveManager.getBeehiveFromBlock(event.getBlockState()).ifPresent(beehive -> {
            for (Item item : event.getItems()) {
                ItemStack itemStack = item.getItemStack();
                if(itemStack.getItemMeta() instanceof BlockStateMeta blockStateMeta && blockStateMeta.getBlockState() instanceof Beehive) {
                    item.setItemStack(beehive.patch(itemStack));
                    Logger.debug("Dropped beehive at {}", item.getLocation());
                }
            }
        });
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);
        ItemStack itemInHand = event.getItemInHand();

        beehiveManager.getBeehiveFromItem(itemInHand).ifPresent(beehive -> {
            beehiveManager.saveBeehiveToBlock(event.getBlockPlaced(), beehive);
            Logger.debug("Placed beehive at {}", event.getBlockPlaced().getLocation());
        });
    }


    @EventHandler
    public void onEntityChangeBlockEvent(EntityChangeBlockEvent event) {
        BeeManager beeManager = this.plugin.getManager(BeeManager.class);
        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);

        if(!(event.getEntity() instanceof Bee bee)) {
            return;
        }

        if(!(event.getBlock().getState() instanceof org.bukkit.block.Beehive beehiveState)) {
            return;
        }

        if(!(beehiveState.getBlockData() instanceof org.bukkit.block.data.type.Beehive beehiveData)) {
            return;
        }

        if(beehiveData.getHoneyLevel() > beehiveData.getMaximumHoneyLevel()) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
            Util.ifBothPresent(beehiveManager.getBeehiveFromBlock(event.getBlock().getState()), beeManager.getBeeTypeFromEntity(bee), (beehive, beeType) -> {
                beehive.addHoney(beeType, 1);
                Logger.debug("Bee {} added {} honey to beehive at {}", bee.getUniqueId(), beeType.displayName(), event.getBlock().getLocation());
                beehiveManager.saveBeehiveToBlock(event.getBlock(), beehive);
            });
        }, 1L);
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;
        if(event.getHand() != EquipmentSlot.HAND) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if(event.getPlayer().getInventory().getItemInMainHand().getType() != Material.SHEARS) return;
        if(!(event.getClickedBlock().getState() instanceof org.bukkit.block.Beehive beehiveBlock)) return;
        if(!(event.getClickedBlock().getBlockData() instanceof org.bukkit.block.data.type.Beehive beehiveData)) return;

        if(beehiveData.getHoneyLevel() < beehiveData.getMaximumHoneyLevel()) {
            return;
        }

        BeehiveManager beehiveManager = this.plugin.getManager(BeehiveManager.class);

        event.setUseInteractedBlock(Event.Result.DENY);

        beehiveManager.editBeehive(event.getClickedBlock(), beehive -> {
            Map<BeeType, Integer> honeyCombCounts = new HashMap<>(beehive.getHoneyCombCounts());
            honeyCombCounts.forEach((beeType, count) -> {
                ItemStack template = beeType.honey(1, false);
                int realAmount = (int) Math.floor(count * beehive.getUpgrade().productionMultiplier());
                Logger.debug("Dropping honey combs for bee type {}: {}", beeType.type(), realAmount);
                while(realAmount > 0) {
                    int amount = Math.min(count, template.getMaxStackSize());
                    ItemStack honeyComb = beeType.honey(amount, beehive.getUpgrade().produceBlocks());
                    beehive.removeHoney(beeType, amount);
                    event.getPlayer().getWorld().dropItemNaturally(event.getPlayer().getLocation(), honeyComb);
                    realAmount -= amount;
                }
            });
        });

        beehiveData.setHoneyLevel(0);
        beehiveBlock.setBlockData(beehiveData);
        beehiveBlock.update();
    }

}
