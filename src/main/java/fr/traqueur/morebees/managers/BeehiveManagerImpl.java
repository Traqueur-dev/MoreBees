package fr.traqueur.morebees.managers;

import fr.traqueur.morebees.api.managers.BeehiveManager;
import fr.traqueur.morebees.api.models.Beehive;
import fr.traqueur.morebees.api.serialization.BeehiveDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.listeners.BeehiveListener;
import fr.traqueur.morebees.models.BeehiveImpl;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;
import java.util.function.Consumer;

public class BeehiveManagerImpl implements BeehiveManager {

    public BeehiveManagerImpl() {
        this.getPlugin().registerListener(new BeehiveListener(this.getPlugin()));
    }

    @Override
    public Optional<Beehive> getBeehiveFromBlock(BlockState block) {
        if(!(block instanceof org.bukkit.block.Beehive beehive)) {
            return Optional.empty();
        }
        PersistentDataContainer container = beehive.getPersistentDataContainer();
        return Optional.of(Keys.BEEHIVE.get(container, BeehiveDataType.INSTANCE, new BeehiveImpl()));
    }

    @Override
    public Optional<Beehive> getBeehiveFromItem(ItemStack stack) {
        if (stack == null || stack.getType().isAir()) {
            return Optional.empty();
        }
        PersistentDataContainer container = stack.getItemMeta() != null ? stack.getItemMeta().getPersistentDataContainer() : null;
        if (container == null) {
            return Optional.empty();
        }
        return Keys.BEEHIVE.get(container, BeehiveDataType.INSTANCE);
    }

    @Override
    public void saveBeehiveToBlock(Block block, Beehive beehive) {
        if(!(block.getState() instanceof org.bukkit.block.Beehive beehiveState)) {
            return;
        }
        PersistentDataContainer container = beehiveState.getPersistentDataContainer();
        Keys.BEEHIVE.set(container, BeehiveDataType.INSTANCE, beehive);
        beehiveState.update();
    }

    @Override
    public void editBeehive(Block block, Consumer<Beehive> consumer) {
        this.getBeehiveFromBlock(block.getState()).ifPresent(beehive -> {
            consumer.accept(beehive);
            this.saveBeehiveToBlock(block, beehive);
        });
    }

}
