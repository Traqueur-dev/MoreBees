package fr.traqueur.morebees;

import fr.traqueur.morebees.api.hooks.Hooks;
import fr.traqueur.morebees.api.hooks.ModelEngineHook;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Breed;
import fr.traqueur.morebees.api.nms.EntityService;
import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.settings.BreedSettings;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import org.bukkit.Location;
import org.bukkit.entity.Bee;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeeManagerImpl implements BeeManager {

    private final EntityService service;

    public BeeManagerImpl() {
        this.service = EntityService.initialize(this.getPlugin());

        System.out.println(service.getClass().getSimpleName());

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
    public Optional<BeeType> getBeeTypeFromEntity(LivingEntity entity) {
        if (entity == null || entity.getType() != EntityType.BEE) {
            return Optional.empty();
        }
        PersistentDataContainer data = entity.getPersistentDataContainer();
        return Keys.BEETYPE.get(data, BeeTypeDataType.INSTANCE);
    }

    @Override
    public void spawnBee(Location location, BeeType beeType, CreatureSpawnEvent.SpawnReason reason, boolean baby) {
        Bee bee = location.getWorld().createEntity(location, Bee.class);

        PersistentDataContainer data = bee.getPersistentDataContainer();
        Keys.BEETYPE.set(data, BeeTypeDataType.INSTANCE, beeType);

        if(baby)
            bee.setBaby();

        Optional<ModelEngineHook> hookOptional = Hooks.MODEL_ENGINE.get();
        boolean textured = hookOptional.map(hook -> hook.overrideModel(bee, beeType)).orElse(false);

        if(!textured) {
            bee.setCustomNameVisible(true);
            bee.customName(MiniMessageHelper.parse(beeType.displayName()));
        }

        bee.spawnAt(location, reason);
    }

    @Override
    public BeeType computeBreed(BeeType mother, BeeType father) {
        List<String> parentsIds = new ArrayList<>();
        parentsIds.add(mother.type());
        parentsIds.add(father.type());
        parentsIds.sort(String::compareTo);

        Breed breed = this.getPlugin().getSettings(BreedSettings.class).breeds()
                .stream()
                .filter(breedChecked -> {
                    List<String> configParents = new ArrayList<>(breedChecked.parents());
                    if (configParents.size() != 2) return false;
                    configParents.sort(String::compareTo);
                    return configParents.equals(parentsIds);
                })
                .findFirst()
                .orElse(null);

        if (breed != null && Math.random() < breed.chance()) {
            return this.getPlugin().getSettings(GlobalSettings.class).bees()
                    .stream()
                    .filter(beeType -> beeType.type().equals(breed.child()))
                    .findFirst()
                    .orElse(null);
        }

        return Math.random() < 0.5 ? mother : father;
    }


}
