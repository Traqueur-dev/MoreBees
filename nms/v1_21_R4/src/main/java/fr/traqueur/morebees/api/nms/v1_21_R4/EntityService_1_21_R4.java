package fr.traqueur.morebees.api.nms.v1_21_R4;

import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.nms.EntityService;
import fr.traqueur.morebees.api.nms.NMSVersion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Bee;

public class EntityService_1_21_R4 implements EntityService {
    @Override
    public Bee createBee(World world, BeeType beeType) {
        ServerLevel level = ((CraftWorld) world).getHandle();
        BeeEntity beeEntity = new BeeEntity(EntityType.BEE, level, beeType);
        return (Bee) beeEntity.getBukkitEntity();
    }

    @Override
    public boolean isCompatible() {
        return NMSVersion.CURRENT == NMSVersion.V1_21_R4;
    }
}
