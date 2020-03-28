package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class CraftingTableAccess implements IRecipeGUIAccess{
    private CraftingInventory inventory;

    public CraftingTableAccess(CraftingInventory inventory){
        this.inventory = inventory;
    }

    @Override
    public void setContent(List<IElement> rawRecipe, IElement resultItem) {
        setContent(rawRecipe);
        inventory.setResult(resultItem.getItemStack());
    }

    @Override
    public void setContent(List<IElement> rawRecipe) {
        ItemStack[] matrix = rawRecipe.stream().map(IElement::getItemStack)
                .collect(Collectors.toList()).toArray(new ItemStack[]{});
        inventory.setMatrix(matrix);
    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        setItemAt(row*3+col, itemStack);
    }

    @Override
    public void setItemAt(int index, IElement itemStack) {
        inventory.setItem(index, itemStack.getItemStack());
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
    public void setResultItem(IElement item) {
        inventory.setResult(item.getItemStack());
    }

    @Override
    public boolean isValidClick(int rawSlot) {
        return rawSlot < 10;
    }

}
