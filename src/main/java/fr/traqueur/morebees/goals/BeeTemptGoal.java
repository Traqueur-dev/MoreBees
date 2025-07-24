package fr.traqueur.morebees.goals;

import com.destroystokyo.paper.entity.ai.Goal;
import com.destroystokyo.paper.entity.ai.GoalKey;
import com.destroystokyo.paper.entity.ai.GoalType;
import fr.traqueur.morebees.api.BeePlugin;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BeeTemptGoal implements Goal<@NotNull Bee> {

    private final BeePlugin plugin;
    private final Bee bee;
    private final double speedModifier;
    private final Predicate<ItemStack> items;
    private Player targetPlayer;
    private int calmDown;

    public BeeTemptGoal(BeePlugin plugin, Bee bee, double speedModifier, Predicate<ItemStack> items) {
        this.bee = bee;
        this.plugin = plugin;
        this.speedModifier = speedModifier;
        this.items = items;
        this.calmDown = 0;
    }


    @Override
    public boolean shouldActivate() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        }

        this.targetPlayer = findNearestTemptingPlayer();
        return this.targetPlayer != null;
    }

    @Override
    public boolean shouldStayActive() {
        if (this.targetPlayer == null) {
            return false;
        }

        return this.shouldActivate();
    }

    @Override
    public void stop() {
        this.targetPlayer = null;
        this.bee.getPathfinder().stopPathfinding();
        this.calmDown = 100;
    }

    @Override
    public void tick() {
        if (this.targetPlayer == null) return;

        Location mobLoc = this.bee.getLocation();
        Location playerLoc = this.targetPlayer.getLocation();

        this.bee.lookAt(playerLoc, 30.0F, 30.0F);

        if (mobLoc.distanceSquared(playerLoc) < 6.25) {
            this.bee.getPathfinder().stopPathfinding();
        } else {
            this.bee.getPathfinder().moveTo(playerLoc, this.speedModifier);
        }
    }

    private Player findNearestTemptingPlayer() {
        return this.bee.getWorld()
                .getNearbyPlayers(this.bee.getLocation(), this.getTemptRangeFromEntity())
                .stream()
                .filter(this::shouldFollowPlayer)
                .min((p1, p2) -> Double.compare(
                        p1.getLocation().distanceSquared(this.bee.getLocation()),
                        p2.getLocation().distanceSquared(this.bee.getLocation())
                ))
                .orElse(null);
    }

    @Override
    public @NotNull GoalKey<@NotNull Bee> getKey() {
        return GoalKey.of(Bee.class, new NamespacedKey(plugin, "tempt"));
    }

    @Override
    public @NotNull EnumSet<GoalType> getTypes() {
        return EnumSet.of(GoalType.MOVE, GoalType.LOOK);
    }

    private boolean shouldFollowPlayer(Player player) {
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();

        return this.items.test(mainHand) || this.items.test(offHand);
    }

    private double getTemptRangeFromEntity() {
        if (this.bee instanceof org.bukkit.attribute.Attributable attributable) {
            AttributeInstance temptRangeAttr = attributable.getAttribute(Attribute.TEMPT_RANGE);
            if (temptRangeAttr != null) {
                return temptRangeAttr.getValue();
            }
        }
        return 10.0;
    }

    private double distanceSquared(double x1, double y1, double z1, double x2, double y2, double z2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dz = z1 - z2;
        return dx * dx + dy * dy + dz * dz;
    }
}
