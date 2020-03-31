package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.*;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipeAccess extends BaseUi {
    private final IRecipe recipe;

    public DetailRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, IRecipe recipe){
        super(sectionIndexes, inventory);
        this.recipe = recipe;
        setContent(recipe.getRawRecipe());
        setResultItem(recipe.getResultItem());
    }

    @Override
    public void setItemAt(int index, IElement itemStack) {
        setItemAt(index / 3, index % 3, itemStack);
    }

    @Override
    public IElement getItemAt(int row, int col) {
        int actualIndex = matrixCoordinate.access(row, col);
        ItemStack item = inventory.getItem(actualIndex);
        return RecipeManager.getItem(item);
    }

    @Override
    public IElement getItemAt(int index) {
        return getItemAt(index / 3, index % 3);
    }

    @Override
    public boolean isValidClick(int rawSlot) {
        return validClicks.contains(rawSlot);
    }
}
