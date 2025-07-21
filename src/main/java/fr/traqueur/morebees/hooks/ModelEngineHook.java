package fr.traqueur.morebees.hooks;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import fr.traqueur.morebees.BeeGrowWatcher;
import fr.traqueur.morebees.BeePlugin;
import fr.traqueur.morebees.Logger;
import fr.traqueur.morebees.models.BeeType;
import fr.traqueur.morebees.serialization.BeeTypeDataType;
import fr.traqueur.morebees.serialization.Keys;
import org.bukkit.entity.Bee;
import org.bukkit.persistence.PersistentDataContainer;

public class ModelEngineHook implements Hook {

    private BeeGrowWatcher beeGrowWatcher;

    @Override
    public void onEnable(BeePlugin plugin) {
        this.beeGrowWatcher = new BeeGrowWatcher(plugin,this);
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, this.beeGrowWatcher, 20L, 100L);
        Logger.success("ModelEngine hook enabled successfully!");
    }

    public boolean overrideModel(Bee entity, BeeType beeType) {
        if(beeType.model().equalsIgnoreCase("default")) {
            return false;
        }

        if (ModelEngineAPI.getAPI().getModelRegistry().get(beeType.model()) == null) {
            Logger.warning("The model {} does not exist, skipping model override for entity {}", beeType.model(), entity.getUniqueId());
            return false;
        }

        entity.setInvisible(true);

        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(beeType.model());

        if (!entity.isAdult()) {
            activeModel.setScale(0.5f);
            beeGrowWatcher.track(entity);
        }

        modeledEntity.addModel(activeModel, true);
        return true;
    }

    public void grow(Bee entity) {
        PersistentDataContainer data = entity.getPersistentDataContainer();
        Keys.BEETYPE.get(data, BeeTypeDataType.INSTANCE).ifPresent(beeType -> {
            if(beeType.model().equalsIgnoreCase("default")) {
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
