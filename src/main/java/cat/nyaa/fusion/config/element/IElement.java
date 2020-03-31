package cat.nyaa.fusion.config.element;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public interface IElement extends ISerializable, IElementHolder {
    boolean match(ItemStack itemStack);
    ItemStack getItemStack();

    void queryRecipes(Consumer<List<IRecipe>> consumer);
}
