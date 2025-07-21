package fr.traqueur.morebees.api.nms.v1_21_R3;

import fr.traqueur.morebees.api.models.BeeType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.jetbrains.annotations.NotNull;

public class BeeEntity extends Bee {

    private final BeeType beeType;

    public BeeEntity(EntityType<? extends Bee> entityType, Level level, BeeType beeType) {
        super(entityType, level);
        this.beeType = beeType;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        super.goalSelector.removeAllGoals(goal -> goal instanceof TemptGoal);
        super.goalSelector.addGoal(3, new TemptGoal(this, 1.25F, (stack) -> beeType.isFood(CraftItemStack.asBukkitCopy(stack)), false));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return beeType.isFood(CraftItemStack.asBukkitCopy(stack));
    }
}
