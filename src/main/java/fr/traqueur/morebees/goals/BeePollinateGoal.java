package fr.traqueur.morebees.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BeePollinateGoal implements Goal<@NotNull Bee> {

    private static final int SEARCH_RADIUS = 10;

    private final BeePlugin plugin;
    private final Bee bee;
    private final Predicate<Block> isFlower;
    private Block targetFlower;

    private int pollinationTicks;
    private int searchCooldown;

    public BeePollinateGoal(BeePlugin plugin, Bee bee, Predicate<Block> isFlower) {
        this.bee = bee;
        this.isFlower = isFlower;
        this.plugin = plugin;
        this.pollinationTicks = 0;
        this.searchCooldown = 0;
    }


    @Override
    public boolean shouldActivate() {
        if (bee.hasNectar() || bee.getAnger() > 0) {
            return false;
        }

        // Cooldown de recherche
        if (searchCooldown > 0) {
            searchCooldown--;
            return false;
        }

        targetFlower = findNearestFlower();
        return targetFlower != null;
    }


    @Override
    public boolean shouldStayActive() {
        if (targetFlower == null) return false;

        if (!isFlower.test(targetFlower)) return false;

        if (pollinationTicks > 0) {
            return true;
        }

        return !bee.hasNectar();
    }

    @Override
    public void start() {
        pollinationTicks = 0;
    }

    @Override
    public void stop() {
        targetFlower = null;
        pollinationTicks = 0;
        searchCooldown = 60;
    }

    @Override
    public void tick() {
        if (targetFlower == null) return;

        Location flowerLoc = targetFlower.getLocation().add(0.5, 0.5, 0.5);
        Location beeLoc = bee.getLocation();

        double distance = beeLoc.distance(flowerLoc);

        if (distance > 2.0) {
            moveToFlower(flowerLoc);
        } else {
            pollinateFlower();
        }
    }

    private void moveToFlower(Location flowerLoc) {
        Location beeLoc = bee.getLocation();
        Vector direction = flowerLoc.toVector().subtract(beeLoc.toVector()).normalize();
        Location target = flowerLoc.clone().add(direction.multiply(-0.5)).add(0, 0.5, 0);
        bee.getPathfinder().moveTo(target, 1.0);
    }

    private void pollinateFlower() {
        pollinationTicks++;

        float rotationSpeed = 90f;
        float currentBodyYaw = bee.getBodyYaw();
        float newBodyYaw = (currentBodyYaw + rotationSpeed) % 360f;
        bee.setBodyYaw(newBodyYaw);

        if (pollinationTicks >= 100) {
            bee.setHasNectar(true);
            pollinationTicks = 0;
        }
    }

    private Block findNearestFlower() {
        Location beeLoc = bee.getLocation();
        for (int radius = 1; radius <= SEARCH_RADIUS; radius++) {
            Block found = searchAtRadius(beeLoc, radius);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private Block searchAtRadius(Location center, int radius) {
        // Recherche sur les faces du cube à ce rayon
        int minY = Math.max(center.getBlockY() - radius/2, center.getWorld().getMinHeight());
        int maxY = Math.min(center.getBlockY() + radius/2, center.getWorld().getMaxHeight());

        for (int y = minY; y <= maxY; y++) {
            for (int x = -radius; x <= radius; x++) {
                Block block1 = center.getWorld().getBlockAt(
                        center.getBlockX() + x, y, center.getBlockZ() + radius);
                Block block2 = center.getWorld().getBlockAt(
                        center.getBlockX() + x, y, center.getBlockZ() - radius);

                if (isFlower.test(block1)) return block1;
                if (isFlower.test(block2)) return block2;
            }

            for (int z = -radius + 1; z < radius; z++) {
                Block block1 = center.getWorld().getBlockAt(
                        center.getBlockX() + radius, y, center.getBlockZ() + z);
                Block block2 = center.getWorld().getBlockAt(
                        center.getBlockX() - radius, y, center.getBlockZ() + z);

                if (isFlower.test(block1)) return block1;
                if (isFlower.test(block2)) return block2;
            }
        }

        return null;
    }

    @Override
    public @NotNull GoalKey<@NotNull Bee> getKey() {
        return GoalKey.of(Bee.class, new NamespacedKey(plugin, "pollinate"));
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }
}
