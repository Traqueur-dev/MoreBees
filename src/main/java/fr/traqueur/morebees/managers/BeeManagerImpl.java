package fr.traqueur.morebees.managers;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.VanillaGoal;
import fr.traqueur.morebees.api.managers.BeeManager;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.models.Breed;
import fr.traqueur.morebees.api.models.Mutation;
import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.settings.BreedSettings;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import fr.traqueur.morebees.api.util.MiniMessageHelper;
import fr.traqueur.morebees.goals.BeePollinateGoal;
import fr.traqueur.morebees.goals.BeeTemptGoal;
import fr.traqueur.morebees.hooks.Hooks;
import fr.traqueur.morebees.hooks.modelengine.ModelEngineHook;
import fr.traqueur.morebees.listeners.BeeListener;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeeManagerImpl implements BeeManager {

    public BeeManagerImpl() {
        this.getPlugin().registerListener(new BeeListener(this.getPlugin()));

        Bukkit.getScheduler().runTask(this.getPlugin(), () -> {
            for (World world : Bukkit.getWorlds()) {
                for (Bee bee : world.getEntitiesByClass(Bee.class)) {
                    this.getBeeTypeFromEntity(bee).ifPresent(beeType -> {
                        this.patchBee(bee, beeType);
                    });
                }
            }
        });
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

        if(baby)
            bee.setBaby();

        this.patchBee(bee, beeType);

        bee.spawnAt(location, reason);
    }

    @Override
    public void patchBee(Bee bee, BeeType beeType) {

        PersistentDataContainer data = bee.getPersistentDataContainer();
        Keys.BEETYPE.set(data, BeeTypeDataType.INSTANCE, beeType);

        Optional<ModelEngineHook> hookOptional = Hooks.MODEL_ENGINE.get();
        boolean textured = hookOptional.map(hook -> hook.overrideModel(bee, beeType)).orElse(false);

        if(!textured) {
            bee.setCustomNameVisible(true);
            bee.customName(MiniMessageHelper.parse(beeType.displayName()));
        }

        Goal<@NotNull Creature> temptGoal = Bukkit.getMobGoals().getGoal(bee, VanillaGoal.TEMPT);
        if (temptGoal != null) {
            Bukkit.getMobGoals().removeGoal(bee, temptGoal);
        }
        Goal<@NotNull Bee> pollinateGoal = Bukkit.getMobGoals().getGoal(bee, VanillaGoal.BEE_POLLINATE);
        if (pollinateGoal != null) {
            Bukkit.getMobGoals().removeGoal(bee, pollinateGoal);
        }
        Bukkit.getMobGoals().addGoal(bee, 4, new BeePollinateGoal(this.getPlugin(), bee, beeType::isFlower));
        Bukkit.getMobGoals().addGoal(bee,3, new BeeTemptGoal(this.getPlugin(), bee, 1.25F, beeType::isFood));
    }

    @Override
    public BeeType computeChildType(BeeType mother, BeeType father) {
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
            return this.getPlugin().getSettings(GlobalSettings.class).getBeeType(breed.child()).orElse(null);
        }

        return Math.random() < 0.5 ? mother : father;
    }

    @Override
    public void feed(@NotNull Player player, Bee bee) {
        if(bee.canBreed()) {
            bee.getWorld().spawnParticle(Particle.HEART,
                    bee.getLocation().add(0, 0.1, 0),
                    7, 0.5, 0.5, 0.5, 0);
            bee.setLoveModeTicks(600);
            bee.setBreedCause(player.getUniqueId());
        } else if (!bee.isAdult()) {
            bee.setAge(bee.getAge() - ((int) (bee.getAge() * 0.1)));
        }
    }

    @Override
    public void mutate(Bee bee, Mutation mutation, Location to) {
        bee.setHasNectar(false);
        to.getBlock().setType(Material.AIR);
        this.getPlugin().getSettings(GlobalSettings.class).getBeeType(mutation.child()).ifPresent(beeType -> {
            to.getWorld().dropItem(to, beeType.egg());
        });
    }

}
