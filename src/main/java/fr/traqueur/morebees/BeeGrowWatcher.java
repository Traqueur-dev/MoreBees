package fr.traqueur.morebees;

import fr.traqueur.morebees.hooks.ModelEngineHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bee;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BeeGrowWatcher implements Runnable{

    private final BeePlugin plugin;
    private final ModelEngineHook hook;
    private final List<WeakReference<Bee>> trackedBees;

    public BeeGrowWatcher(BeePlugin plugin, ModelEngineHook hook) {
        this.plugin = plugin;
        this.hook = hook;
        this.trackedBees = new ArrayList<>();
    }

    public void track(Bee bee) {
        this.trackedBees.add(new WeakReference<>(bee));
    }

    @Override
    public void run() {
        Iterator<WeakReference<Bee>> iterator = this.trackedBees.iterator();
        while (iterator.hasNext()) {
            Bee bee = iterator.next().get();
            if (bee == null || !bee.isValid()) {
                iterator.remove();
                continue;
            }
            if(!bee.isAdult()) {
                continue;
            }
            iterator.remove();
            Bukkit.getScheduler().runTask(this.plugin,() -> hook.grow(bee));
        }
    }
}
