package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ListRecipeAccess implements IRecipeGUIAccess {
    private final int rows = 3;
    private final int cols = 5;

    private final MatrixCoordinate matrixCoordinate;
    private final Inventory inventory;
    private final List<IRecipe> recipes;

    ListRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, List<IRecipe> recipe){
        this.matrixCoordinate = new MatrixCoordinate(sectionIndexes, 3, 6);
        this.inventory = inventory;
        this.recipes = recipe;
    }

    @Override
    public void setContent(List<IElement> rawRecipe, IElement resultItem) {
        setContent(rawRecipe);
    }

    @Override
    public void setContent(List<IElement> rawRecipe) {

    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        inventory.setItem(matrixCoordinate.access(row, col), itemStack.getItemStack());
    }

    @Override
    public void setItemAt(int index, IElement itemStack) {
        setItemAt(index / 5, index % 5, itemStack);
    }

    @Override
    public IElement getItemAt(int row, int col) {
        return RecipeManager.getItem(inventory.getItem(matrixCoordinate.access(row, col)));
    }

    @Override
    public IElement getItemAt(int index) {
        return getItemAt(index / 5, index % 5);
    }

    @Override
    public void setResultItem(IElement item) {

    }

    @Override
    public boolean isValidClick(int rawSlot) {
        int i = matrixCoordinate.indexOf(rawSlot);
        return i >= 0 && i < recipes.size() ;
    }
}
