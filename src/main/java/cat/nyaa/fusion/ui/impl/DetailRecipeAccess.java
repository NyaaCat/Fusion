package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.*;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
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
    protected void addSplitters() {
        super.addSplitters();
        ItemStack blueSplitter = createSplitter(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack lightBlueSplitter = createSplitter(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        for (int i = 0; i < 3; i++) {
            inventory.setItem(matrixCoordinate.access(i, 3), blueSplitter);
        }
        inventory.setItem(matrixCoordinate.access(0, 4), lightBlueSplitter);
        inventory.setItem(matrixCoordinate.access(2, 4), lightBlueSplitter);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, Utils.getFakeItem(itemStack.getItemStack()));
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

    @Override
    public void refreshUi() {
        if (recipes == null || recipes.size() == 0)return;
        if (getCurrentPage()>recipes.size()){
            setPage(0);
        }
        List<IElement> rawRecipe = recipes.get(getCurrentPage()).getRawRecipe();
        setContent(rawRecipe);
        setResultItem(recipe.getResultItem());
    }

    @Override
    public void setResultItem(IElement item) {
        Utils.newChain().delay(2).sync(() -> {
            inventory.setItem(resultSlot.access(matrixCoordinate), Utils.getFakeItem(item.getItemStack()));
        }).execute();
    }

    @Override
    public int getPageSize() {
        return 1;
    }

    @Override
    public void onContentInteract(InventoryInteractEvent event) {
        event.setCancelled(true);
    }

    @Override
    public void onResultInteract(InventoryInteractEvent event, ItemStack itemStack) {
        event.setCancelled(true);
    }

    @Override
    public boolean isResultClick(int rawSlot) {
        return rawSlot == resultSlot.access(matrixCoordinate);
    }
}
