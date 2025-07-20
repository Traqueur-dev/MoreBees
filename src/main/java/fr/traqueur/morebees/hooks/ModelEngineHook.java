package fr.traqueur.morebees.hooks;

import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import fr.traqueur.morebees.BeePlugin;
import fr.traqueur.morebees.Logger;
import fr.traqueur.morebees.models.BeeType;
import org.bukkit.entity.Entity;

public class ModelEngineHook implements Hook {

    @Override
    public void onEnable(BeePlugin plugin) {
        Logger.success("ModelEngine hook enabled successfully!");
    }

    public void overrideModel(Entity entity, BeeType beeType) {
        ModeledEntity modeledEntity = ModelEngineAPI.createModeledEntity(entity);
        ActiveModel activeModel = ModelEngineAPI.createActiveModel(beeType.model());
        modeledEntity.addModel(activeModel, true);
    }


}
