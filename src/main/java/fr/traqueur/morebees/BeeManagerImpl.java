package fr.traqueur.morebees;

import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.hooks.ModelEngineHook;
import fr.traqueur.morebees.managers.BeeManager;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Optional;

public class BeeManagerImpl implements BeeManager {

    public BeeManagerImpl() {
        this.getPlugin().registerListener(new BeeListener(this.getPlugin()));
    }

    @Override
    public Optional<BeeType> getBeeTypeFromEgg(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return Optional.empty();
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) {
            return Optional.empty();
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        return  Keys.BEETYPE.get(container, BeeTypeDataType.INSTANCE);
    }

    @Override
    public void spawnBee(Location location, BeeType beeType) {
        LivingEntity bee = location.getWorld().createEntity(location, Bee.class);
        PersistentDataContainer data = bee.getPersistentDataContainer();
        Keys.BEETYPE.set(data, BeeTypeDataType.INSTANCE, beeType);
        Optional<ModelEngineHook> hookOptional = Hooks.MODEL_ENGINE.get();
        hookOptional.ifPresent(hook -> {
            hook.overrideModel(bee, beeType);
        });
        bee.spawnAt(location, CreatureSpawnEvent.SpawnReason.SPAWNER_EGG);
    }


}
