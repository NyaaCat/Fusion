package cat.nyaa.fusion.config.element;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IElement extends ISerializable {
    boolean match(ItemStack itemStack);
    ItemStack getItemStack();

    List<IRecipe> getRecipes();
}
