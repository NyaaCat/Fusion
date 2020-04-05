package cat.nyaa.fusion.config.recipe;

import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IRecipe extends ISerializable {
    ItemStack getResultItem();
    List<ItemStack> getRawRecipe();
    int getElementCount();
    String getName();

    boolean matches(List<ItemStack> matrix);
}
