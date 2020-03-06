package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.config.recipe.IRecipe;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RecipeManager {
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    private Map<String, IRecipe> recipeById;
    private Map<String, IRecipe> recipeByName;
    private Map<Integer, IRecipe> recipeByElementCount;

    public RecipeManager() {
        recipeById = new LinkedHashMap<>();
        recipeByName = new LinkedHashMap<>();
        recipeByElementCount = new LinkedHashMap<>();
    }


    void sampleLock(){
        if (!ensureLock()){
            new BukkitRunnable(){
                @Override
                public void run() {
                    sampleLock();
                }
            }.runTaskLater(FusionPlugin.plugin, 1);
            return;
        }

    }

    boolean ensureLock(){
        if (!Bukkit.getServer().isPrimaryThread()) {
            rwLock.readLock().lock();
            return true;
        }else {
            return !rwLock.isWriteLocked();
        }
    }

}
