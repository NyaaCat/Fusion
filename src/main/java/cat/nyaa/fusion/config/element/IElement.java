package cat.nyaa.fusion.config.element;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.nyaacore.configuration.ISerializable;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainAbortAction;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface IElement extends ISerializable {
    boolean match(ItemStack itemStack);
    ItemStack getItemStack();

    void queryRecipes(Consumer<List<IRecipe>> consumer);
}
