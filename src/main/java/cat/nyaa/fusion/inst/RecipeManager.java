package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.config.NamedDirConfigs;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.element.BaseElement;
import cat.nyaa.fusion.inst.recipe.BaseRecipe;
import cat.nyaa.fusion.util.Utils;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class RecipeManager extends NamedDirConfigs<BaseRecipe> {
    private static RecipeManager INSTANCE;

    private Map<String, IRecipe> recipeById;
    private Map<Integer, IRecipe> recipeByElementCount;

    private RecipeManager(File storageDir, Class<BaseRecipe> targetClass) {
        super(storageDir, targetClass);

        recipeById = new LinkedHashMap<>();
        recipeByElementCount = new LinkedHashMap<>();
    }

    public static RecipeManager getInstance(){
        if (INSTANCE == null){
            synchronized (RecipeManager.class){
                if (INSTANCE == null){
                    INSTANCE = new RecipeManager(new File(FusionPlugin.plugin.getDataFolder(),"recipes"), BaseRecipe.class);
                }
            }
        }
        return INSTANCE;
    }

    private Cache<String, IElement> iElementCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    public static IElement getItem(ItemStack itemStack){
        return null;
    }

    public static TaskChain<List<IRecipe>> queryItem(ItemStack itemStack, TaskChain<?> taskChain){
        return taskChain.async((input) -> {
            return null;
        });
    }


    public static void createQuery(IElement element, Consumer<List<IRecipe>> consumer) {
        Utils.newChain().async((ignored) ->{
            List<IRecipe> recipes = new ArrayList<>();
            getInstance().recipeById.forEach((s, iRecipe) -> {
                if (iRecipe.getRawRecipe().contains(element)){
                    recipes.add(iRecipe);
                }
            });
            return recipes;
        }).sync((recipes)->{
            consumer.accept(recipes);
            return null;
        }).execute();
    }

    private void updateFromConfig(){
        recipeById.clear();
        recipeByElementCount.clear();
        values().forEach(baseRecipe -> {
            int elementCount = baseRecipe.getElementCount();
            String name = baseRecipe.getName();
            recipeById.put(name, baseRecipe);
            recipeByElementCount.put(elementCount, baseRecipe);
        });
    }

    @Override
    public void loadFromDir() {
        super.loadFromDir();

    }

    public void loadFromConfig(){

    }
}
