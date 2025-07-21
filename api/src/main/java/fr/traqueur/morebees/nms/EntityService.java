package fr.traqueur.morebees.nms;

import fr.traqueur.morebees.BeePlugin;
import org.bukkit.Bukkit;

import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public interface EntityService {

    static EntityService initialize(BeePlugin plugin) {
        ServiceLoader<EntityService> loader = ServiceLoader.load(EntityService.class, plugin.getClass().getClassLoader());
        for (EntityService service : loader) {
            System.out.println("Found SPI impl: " + service.getClass().getName());
        }
        return StreamSupport.stream(loader.spliterator(), false)
                .filter(EntityService::isCompatible)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No compatible EntityService found"));
    }

    boolean isCompatible();

}
