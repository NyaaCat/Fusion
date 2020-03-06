package cat.nyaa.fusion.config.element;

import cat.nyaa.fusion.config.recipe.IRecipe;
import org.bukkit.inventory.ItemStack;
import think.rpgitems.power.PropertyHolder;

import java.util.List;

public interface IElement extends PropertyHolder {
    boolean match(ItemStack itemStack);
    ItemStack getItemStack();
    String getID();
    String getName();

    List<IRecipe> getRecipes();
}
