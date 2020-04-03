package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.BaseUi;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ListRecipeAccess extends BaseUi {
    private final int rows = 3;
    private final int cols = 5;

    private final MatrixCoordinate matrixCoordinate;
    private final Inventory inventory;
    private final List<IRecipe> recipes;

    public ListRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, List<IRecipe> recipe){
        super(sectionIndexes, inventory);
        this.matrixCoordinate = new MatrixCoordinate(sectionIndexes, 3, 6);
        this.inventory = inventory;
        this.recipes = recipe;
    }

    @Override
    public void setItemAt(int index, IElement itemStack) {
        setItemAt(index / 5, index % 5, itemStack);
    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, Utils.getFakeItem(itemStack.getItemStack()));
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
    public boolean isValidClick(int rawSlot) {
        int i = matrixCoordinate.indexOf(rawSlot);
        return i >= 0 && i < recipes.size() ;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        //todo new feature should give item back.
    }

    @Override
    public void refreshUi() {
        List<IElement> collect = recipes.stream()
                .skip(getPageSize() * getCurrentPage())
                .limit(getPageSize())
                .map(IRecipe::getResultItem)
                .collect(Collectors.toList());
        setContent(collect);
    }

    @Override
    public int getPageSize() {
        return 3*5;
    }

    @Override
    public void onContentInteract(InventoryInteractEvent event) {

    }

    @Override
    public void onResultInteract(InventoryInteractEvent event, ItemStack itemStack) {

    }
}
