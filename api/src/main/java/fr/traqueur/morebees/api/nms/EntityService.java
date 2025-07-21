package fr.traqueur.morebees.api.nms;

import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.models.BeeType;
import org.bukkit.World;
import org.bukkit.entity.Bee;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public interface EntityService {

    static EntityService initialize(BeePlugin plugin) {
        ServiceLoader<EntityService> loader = ServiceLoader.load(EntityService.class, plugin.getClass().getClassLoader());
        return StreamSupport.stream(loader.spliterator(), false)
                .filter(EntityService::isCompatible)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No compatible EntityService found"));
    }

    Bee createBee(World world, BeeType beeType);

    boolean isCompatible();

}
