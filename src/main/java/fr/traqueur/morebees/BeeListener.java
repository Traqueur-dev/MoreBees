package fr.traqueur.morebees;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.util.Util;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class BeeListener implements Listener {

    private final BeePlugin plugin;

    public BeeListener(BeePlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null) {
            return;
        }

        Block spawnBlock = clickedBlock.getRelative(event.getBlockFace());
        Location spawnLocation = spawnBlock.getLocation().add(0.5, 0, 0.5);

        this.handleSpawn(event, event.getPlayer(), item, spawnLocation, false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        if(!(event.getRightClicked() instanceof Bee bee)) {
            return;
        }
        if(EquipmentSlot.OFF_HAND == event.getHand()) {
            return;
        }
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Location spawnLocation = event.getRightClicked().getLocation();
        BeeManager beeManager = plugin.getManager(BeeManager.class);
        Optional<BeeType> eggBeeType = beeManager.getBeeTypeFromEgg(item);
        if (eggBeeType.isPresent()) {
            this.handleSpawn(event, event.getPlayer(), item, spawnLocation, true);
            return;
        }

        Optional<BeeType> entityType = beeManager.getBeeTypeFromEntity(bee);
        entityType.ifPresent(beeType -> {
            event.setCancelled(true);
            if(!beeType.isFood(item)) {
               return;
            }
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
            beeManager.feed(event.getPlayer(), bee);
        });
    }

    private void handleSpawn(Cancellable event, Player player, ItemStack item, Location spawnLocation, boolean baby) {
        BeeManager beeManager = plugin.getManager(BeeManager.class);
        Optional<BeeType> beeType = beeManager.getBeeTypeFromEgg(item);

        if (beeType.isPresent()) {
            event.setCancelled(true);
            beeManager.spawnBee(spawnLocation, beeType.get(), CreatureSpawnEvent.SpawnReason.SPAWNER_EGG, baby);

            if (player.getGameMode() != GameMode.CREATIVE) {
                item.setAmount(item.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onSpawn(CreatureSpawnEvent event) {
        Entity entity = event.getEntity();
        if(entity.getType() != EntityType.BEE) {
            return;
        }
        if(!(entity instanceof Bee bee)) {
            return;
        }

        BeeManager beeManager = plugin.getManager(BeeManager.class);
        beeManager.getBeeTypeFromEntity(bee).ifPresent(beeType -> {
            beeManager.patchBee(bee, beeType);
        });
    }

    @EventHandler
    public void onBreed(EntityBreedEvent event) {
        BeeManager beeManager = plugin.getManager(BeeManager.class);
        Optional<BeeType> motherType = beeManager.getBeeTypeFromEntity(event.getMother());
        Optional<BeeType> fatherType = beeManager.getBeeTypeFromEntity(event.getFather());

        if(!(event.getEntity() instanceof Bee bee)) {
            return;
        }

        Util.ifBothPresent(motherType, fatherType, (mother, father) -> {
            BeeType child = beeManager.computeChildType(mother, father);
            beeManager.patchBee(bee, child);
        });
    }

}
