package fr.traqueur.morebees.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BeePollinateGoal implements Goal<Bee> {

    private static final int SEARCH_RADIUS = 10; // Rayon de recherche des fleurs

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
        if (pollinationTicks >= 100) {
            bee.setHasNectar(true);
            pollinationTicks = 0;
        }
    }

    private Block findNearestFlower() {
        Location beeLoc = bee.getLocation();
        Block nearestFlower = null;
        double nearestDistance = Double.MAX_VALUE;

        // Recherche dans un cube autour de l'abeille
        for (int x = -SEARCH_RADIUS; x <= SEARCH_RADIUS; x++) {
            for (int y = -SEARCH_RADIUS/2; y <= SEARCH_RADIUS/2; y++) {
                for (int z = -SEARCH_RADIUS; z <= SEARCH_RADIUS; z++) {
                    Block block = beeLoc.clone().add(x, y, z).getBlock();

                    if (isFlower.test(block)) {
                        double distance = beeLoc.distance(block.getLocation());

                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearestFlower = block;
                        }
                    }
                }
            }
        }

        return nearestFlower;
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
