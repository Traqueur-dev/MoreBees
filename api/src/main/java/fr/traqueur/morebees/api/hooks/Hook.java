package fr.traqueur.morebees.api.hooks;

import fr.traqueur.morebees.api.BeePlugin;

import java.util.HashSet;
import java.util.Set;

public interface Hook {

    Set<Hook> HOOKS = new HashSet<>();

     static <T extends Hook> Set<T> getByClass(Class<T> type) {
        Set<T> hooks = new HashSet<>();
        for (Hook hook : HOOKS) {
            if (type.isInstance(hook)) {
                hooks.add(type.cast(hook));
            }
        }
        return hooks;
    }

    void onEnable(BeePlugin plugin);
}
