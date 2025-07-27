package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.managers.UpgradesManager;
import fr.traqueur.morebees.api.models.Upgrade;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.serialization.datas.UpgradeDataType;
import fr.traqueur.morebees.listeners.UpgradesListener;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Beehive;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Transformation;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

public class UpgradesManagerImpl implements UpgradesManager {

    private static final float DISPLAY_SIZE = 0.3f; // Taille de l'item display (petit)
    private static final float OFFSET_FROM_FACE = 0.02f; // Distance de la face
    private static final float CORNER_OFFSET = 0.15f;

    public UpgradesManagerImpl() {
        this.getPlugin().registerListener(new UpgradesListener(this.getPlugin()));

        Bukkit.getScheduler().runTask(this.getPlugin(),() -> {
            for (World world : Bukkit.getWorlds()) {
                for (Chunk loadedChunk : world.getLoadedChunks()) {
                    for (BlockState tileEntity : loadedChunk.getTileEntities()) {
                        if (tileEntity instanceof org.bukkit.block.Beehive beehiveState) {
                            this.loadBeehive(beehiveState, Arrays.stream(loadedChunk.getEntities()));
                        }
                    }
                }
            }
        });
    }

    @Override
    public Optional<Upgrade> getUpgradeFromItem(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return Optional.empty();
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        return Keys.UPGRADE_ID.get(persistentDataContainer, UpgradeDataType.INSTANCE);
    }

    @Override
    public ItemDisplay createUpgradeDisplay(Block beehiveBlock, Upgrade upgrade) {
        if (upgrade.equals(Upgrade.NONE)) {
            return null;
        }

        Location displayLocation = calculateDisplayLocation(beehiveBlock);
        if (displayLocation == null) {
            return null;
        }

        ItemDisplay itemDisplay = displayLocation.getWorld().spawn(displayLocation, ItemDisplay.class);

        // Configuration de l'ItemDisplay
        ItemStack upgradeItem = upgrade.build();
        itemDisplay.setItemStack(upgradeItem);

        // Transformation pour la taille et la position
        Transformation transformation = new Transformation(
                new Vector3f(0, 0, 0), // Translation
                new AxisAngle4f(0, 0, 0, 1), // Rotation gauche
                new Vector3f(DISPLAY_SIZE, DISPLAY_SIZE, DISPLAY_SIZE), // Scale
                new AxisAngle4f(0, 0, 0, 1) // Rotation droite
        );
        itemDisplay.setTransformation(transformation);

        // Configuration additionnelle
        itemDisplay.setBillboard(ItemDisplay.Billboard.FIXED);
        itemDisplay.setViewRange(32.0f);
        itemDisplay.setPersistent(true);
        itemDisplay.setGlowing(false);

        return itemDisplay;
    }

    @Override
    public void removeUpgradeDisplay(UUID displayUUID) {
        if (displayUUID == null) return;

        Bukkit.getWorlds().forEach(world -> {
            world.getEntities().stream()
                    .filter(entity -> entity instanceof ItemDisplay)
                    .filter(entity -> entity.getUniqueId().equals(displayUUID))
                    .forEach(org.bukkit.entity.Entity::remove);
        });
    }

    @Override
    public void loadBeehive(org.bukkit.block.Beehive beehiveState, Stream<Entity> entities) {
        BeehiveManager beehiveManager = this.getPlugin().getManager(BeehiveManager.class);
        beehiveManager.getBeehiveFromBlock(beehiveState).ifPresent(beehive -> {
            Upgrade upgrade = beehive.getUpgrade();
            UUID currentDisplayUUID = beehive.getUpgradeId();
            if ((!upgrade.equals(Upgrade.NONE) && currentDisplayUUID == null) ||
                    entities.noneMatch(entity -> entity.getUniqueId().equals(currentDisplayUUID) && entity.getType() == EntityType.ITEM_DISPLAY)) {
                ItemDisplay newDisplay = this.createUpgradeDisplay(beehiveState.getBlock(), upgrade);
                UUID newDisplayUUID = newDisplay == null ? null : newDisplay.getUniqueId();
                beehiveManager.editBeehive(beehiveState.getBlock(), beehiveToEdit -> {
                    beehiveToEdit.setUpgradeId(newDisplayUUID);
                });
            }
        });
    }

    private Location calculateDisplayLocation(Block beehiveBlock) {
        if (!(beehiveBlock.getBlockData() instanceof Beehive beehiveData)) {
            return null;
        }

        Location blockLocation = beehiveBlock.getLocation().add(0.5, 0.5, 0.5);

        // Calculer la position selon la direction de la ruche
        return switch (beehiveData.getFacing()) {
            case NORTH -> blockLocation.add(-CORNER_OFFSET, CORNER_OFFSET, -0.5 - OFFSET_FROM_FACE);
            case SOUTH -> blockLocation.add(CORNER_OFFSET, CORNER_OFFSET, 0.5 + OFFSET_FROM_FACE);
            case EAST -> blockLocation.add(0.5 + OFFSET_FROM_FACE, CORNER_OFFSET, -CORNER_OFFSET);
            case WEST -> blockLocation.add(-0.5 - OFFSET_FROM_FACE, CORNER_OFFSET, CORNER_OFFSET);
            default -> null;
        };
    }



}
