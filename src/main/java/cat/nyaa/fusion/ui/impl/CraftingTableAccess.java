package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.BaseUi;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CraftingTableAccess extends BaseUi {
    public CraftingTableAccess(List<Integer> recipeSpace, Inventory largeChestInventory) {
        super(recipeSpace, largeChestInventory);
    }

    @Override
    public void setItemAt(int index, IElement itemStack) {
        setItemAt(index/3, index%3, itemStack);
    }

    @Override
    public IElement getItemAt(int row, int col) {
        return getItemAt(row*3+col);
    }

    @Override
    public IElement getItemAt(int index) {
        return RecipeManager.getItem(inventory.getItem(index));
    }

    @Override
    public boolean isValidClick(int rawSlot) {
        return rawSlot < 10;
    }

}
