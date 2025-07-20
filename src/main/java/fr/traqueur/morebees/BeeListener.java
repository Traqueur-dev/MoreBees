package fr.traqueur.morebees;

import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.hooks.ModelEngineHook;
import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import fr.traqueur.morebees.util.Util;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;
import java.util.Set;

public class BeeListener implements Listener {

    private static final Set<CreatureSpawnEvent.SpawnReason> SPAWN_REASONS = Set.of(
            CreatureSpawnEvent.SpawnReason.BEEHIVE,
            CreatureSpawnEvent.SpawnReason.BREEDING,
            CreatureSpawnEvent.SpawnReason.SPAWNER_EGG
    );

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
        if(event.getRightClicked().getType() != EntityType.BEE) {
            return;
        }
        ItemStack item = event.getHand() == EquipmentSlot.OFF_HAND ? event.getPlayer().getInventory().getItemInOffHand() : event.getPlayer().getInventory().getItemInMainHand();

        Location spawnLocation = event.getRightClicked().getLocation();
        this.handleSpawn(event, event.getPlayer(), item, spawnLocation, true);
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
    public void onBreed(EntityBreedEvent event) {
        BeeManager beeManager = plugin.getManager(BeeManager.class);
        Optional<BeeType> motherType = beeManager.getBeeTypeFromEntity(event.getMother());
        Optional<BeeType> fatherType = beeManager.getBeeTypeFromEntity(event.getFather());

        Util.ifBothPresent(motherType, fatherType, (mother, father) -> {
            event.setCancelled(true);
            Location spawnLocation = event.getEntity().getLocation();
            BeeType child = beeManager.computeBreed(mother, father);
            beeManager.spawnBee(spawnLocation, child, CreatureSpawnEvent.SpawnReason.BREEDING, true);
        });
    }

}
