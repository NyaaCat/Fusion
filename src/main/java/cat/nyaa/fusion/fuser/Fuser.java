package cat.nyaa.fusion.fuser;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import org.bukkit.inventory.ItemStack;

public interface Fuser {
    ItemStack fuseItem(IRecipe iRecipe, IRecipeGUIAccess guiAccess);

}
