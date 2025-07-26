package fr.traqueur.morebees.hooks.modelengine;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.animation.handler.AnimationHandler;
import com.ticxo.modelengine.api.animation.property.IAnimationProperty;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import fr.traqueur.morebees.api.BeePlugin;
import fr.traqueur.morebees.api.Logger;
import fr.traqueur.morebees.api.hooks.Hook;
import fr.traqueur.morebees.api.models.BeeType;
import fr.traqueur.morebees.api.serialization.BeeTypeDataType;
import fr.traqueur.morebees.api.serialization.Keys;
import fr.traqueur.morebees.api.settings.GlobalSettings;
import org.bukkit.entity.Bee;
import org.bukkit.persistence.PersistentDataContainer;

public class ModelEngineHook implements Hook {

    private BeePlugin plugin;
    private BeeGrowWatcher beeGrowWatcher;

    @Override
    public void onEnable(BeePlugin plugin) {
        this.plugin = plugin;
        this.beeGrowWatcher = new BeeGrowWatcher(plugin,this);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this.beeGrowWatcher, 20L, 100L);
        Logger.success("ModelEngine hook enabled successfully!");
    }

    public boolean overrideModel(Bee entity, BeeType beeType) {
        if(beeType.model() == null) {
            return false;
        }

        if (ModelEngineAPI.getAPI().getModelRegistry().get(beeType.model()) == null) {
            Logger.warning("The model {} does not exist, skipping model override for entity {}", beeType.model(), entity.getUniqueId());
            return false;
        }

        entity.setInvisible(true);

        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(beeType.model());

        AnimationHandler animationHandler = activeModel.getAnimationHandler();
        IAnimationProperty playedAnimation = animationHandler.playAnimation(this.plugin.getSettings(GlobalSettings.class).flyAnimation(), 0.3, 0.3, 1, true);
        if(playedAnimation == null) {
            Logger.warning("Failed to play animation {} for model {} on entity {}", this.plugin.getSettings(GlobalSettings.class).flyAnimation(), beeType.model(), entity.getUniqueId());
        }

        if (!entity.isAdult()) {
            activeModel.setScale(0.5f);
            beeGrowWatcher.track(entity);
        }

        modeledEntity.addModel(activeModel, true);
        return true;
    }

    public void grow(Bee entity) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Keys.BEE_TYPE.get(data, BeeTypeDataType.INSTANCE).ifPresent(beeType -> {
            if(beeType.model() == null) {
                return;
            }

            ModeledEntity modeledEntity = ModelEngineAPI.getModeledEntity(entity);
            if (modeledEntity == null) {
                return;
            }
            modeledEntity.getModel(beeType.model()).ifPresent(beeModel -> {
                beeModel.setScale(1f);
            });
        });
    }


}
