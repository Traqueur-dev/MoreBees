package fr.traqueur.morebees.api.nms.v1_21_R3;

import fr.traqueur.morebees.api.nms.EntityService;
import fr.traqueur.morebees.api.nms.NMSVersion;

public class EntityService_1_21_R3 implements EntityService {
    @Override
    public boolean isCompatible() {
        return NMSVersion.CURRENT == NMSVersion.V1_21_R3;
    }
}
