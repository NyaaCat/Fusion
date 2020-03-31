package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.config.NamedDirConfigs;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.element.VanillaElement;
import cat.nyaa.fusion.inst.recipe.BaseRecipe;
import cat.nyaa.fusion.util.Utils;
import cat.nyaa.nyaacore.Pair;
import cat.nyaa.nyaacore.utils.ClassPathUtils;
import cat.nyaa.nyaacore.utils.ItemStackUtils;
import co.aikar.taskchain.TaskChain;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;

import static cat.nyaa.fusion.inst.element.VanillaElement.EMPTY_ELEMENT;

public class RecipeManager extends NamedDirConfigs<BaseRecipe> {
    private static RecipeManager INSTANCE;

    private Map<String, IRecipe> recipeById;
    private Map<Integer, List<IRecipe>> recipeByElementCount;


    private static Set<Pair<Integer, IElementHandler>> handlerRegistry = new TreeSet<>(Collections.reverseOrder(Map.Entry.comparingByKey()));

    private RecipeManager(File storageDir, Class<BaseRecipe> targetClass) {
        super(storageDir, targetClass);

        recipeById = new LinkedHashMap<>();
        recipeByElementCount = new LinkedHashMap<>();
        registerElements("cat.nyaa.fusion.inst.element");
    }

    public static IElement getEmptyElement() {
        return EMPTY_ELEMENT;
    }

    private void registerElements(String s) {
        Class<? extends IElementHandler>[] classes = ClassPathUtils.scanSubclasses(FusionPlugin.plugin, s, IElementHandler.class);
        if (classes.length>0) {
            for (int i = 0; i < classes.length; i++) {
                Class<? extends IElementHandler> aClass = classes[i];
                ElementMeta annotation = aClass.getAnnotation(ElementMeta.class);
                if (annotation == null) {
                    continue;
                }
                EventPriority priority = annotation.priority();
                try{
                    Constructor<? extends IElementHandler> declaredConstructor = aClass.getDeclaredConstructor();
                    declaredConstructor.setAccessible(true);
                    IElementHandler iElementHandler = declaredConstructor.newInstance();
                    handlerRegistry.add(new Pair<>(priority.getSlot(), iElementHandler));
                } catch (NoSuchMethodException e) {
                    Bukkit.getLogger().log(Level.SEVERE, "class "+ aClass.getSimpleName()+" don't have a default constructor.", e);
                } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }
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

    private static Cache<String, IElement> iElementCache = CacheBuilder.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES).build();

    static {
        EMPTY_ELEMENT.setItemStack(new ItemStack(Material.AIR));
    }

    public static IElement getItem(String s) {
        if (s.equals("")){
            return EMPTY_ELEMENT;
        }
        IElementHandler iElementHandler = handlerRegistry.stream()
                .filter(pair -> pair.getValue().isSuitableElement(s))
                .findAny()
                .map(Pair::getValue).orElse(new VanillaElement(""));
        return iElementHandler.createFromString(s);
    }

    public static IElement getItem(ItemStack itemStack){
        if (itemStack == null) {
            return EMPTY_ELEMENT;
        }
        String key = getCacheKey(itemStack);
        IElement ele = iElementCache.getIfPresent(key);
        if (ele == null){
            ele = createElement(itemStack);
            iElementCache.put(key, ele);
        }
        return ele;
    }

    private static IElement createElement(ItemStack itemStack) {
        IElementHandler iElementHandler = handlerRegistry.stream()
                .filter(pair -> pair.getValue().isSuitableElement(itemStack))
                .findAny()
                .map(Pair::getValue).orElse(new VanillaElement(""));
        return iElementHandler.create(itemStack);
    }

    private static String getCacheKey(ItemStack item) {
        IElementHandler iElementHandler = handlerRegistry.stream()
                .filter(pair -> pair.getValue().isSuitableElement(item))
                .findAny()
                .map(Pair::getValue).orElse(null);
        return iElementHandler == null ? ItemStackUtils.itemToBase64(item): iElementHandler.getCacheKey(item);
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
            recipeByElementCount.computeIfAbsent(elementCount, (ignored) -> new ArrayList<>()).add(elementCount, baseRecipe);
        });
    }

    public IRecipe getRecipe(List<IElement> itemStacks){
        int count = 0;
        for (int i = 0; i < itemStacks.size(); i++) {
            if (itemStacks.get(i) != null) {
                count++;
            }
        }
        List<IRecipe> recipes = recipeByElementCount.get(count);
        IRecipe iRecipe = recipes.stream().filter(recipe -> recipe.matches(itemStacks)).findAny().orElse(null);
        return iRecipe;
    }

    @Override
    public String add(BaseRecipe config) {
        String key = super.add(config);
        return addConfig(key, config);
    }

    private String addConfig(String key, BaseRecipe config) {
        recipeById.put(key, config);
        config.setName(key);
        int elementCount = config.getElementCount();
        recipeByElementCount.computeIfAbsent(elementCount, (ignored) -> new ArrayList<>()).add(elementCount, config);
        return key;
    }

    @Override
    public String add(String name, BaseRecipe config) {
        String key = super.add(name, config);
        String s = addConfig(key, config);
        saveToDir();
        return s;
    }

    @Override
    public void loadFromDir() {
        super.loadFromDir();
        updateFromConfig();
    }

    public List<IRecipe> getRecipes() {
        return new ArrayList<>(recipeById.values());
    }


    /**
     * register a custom element handler.
     * Higher priority handlers will catch element generate event before lowers.
     * @param priority
     * @param registerTest
     */
    public void register(EventPriority priority, IElementHandler registerTest) {
        handlerRegistry.add(new Pair<>(priority.getSlot(), registerTest));
    }
}
