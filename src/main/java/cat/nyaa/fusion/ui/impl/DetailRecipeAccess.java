package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import cat.nyaa.fusion.ui.UiCoordinate;
import cat.nyaa.fusion.ui.UiType;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DetailRecipeAccess implements IRecipeGUIAccess {
    private final MatrixCoordinate matrixCoordinate;
    private final Inventory inventory;
    private final IRecipe recipe;
    private final UiCoordinate resultSlot;
    private final List<UiCoordinate> slots;
    private final List<Integer> validClicks;

    DetailRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, IRecipe recipe){
        this.matrixCoordinate = new MatrixCoordinate(sectionIndexes, 3, 6);
        slots = new ArrayList<>(9);
        validClicks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                UiCoordinate e = new UiCoordinate(i, j);
                slots.add(e);
                validClicks.add(e.access(matrixCoordinate));
            }
        }
        resultSlot = new UiCoordinate(1, 4);
        validClicks.add(resultSlot.access(matrixCoordinate));
        this.inventory = inventory;
        ItemStack blueSplitter = createSplitter(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack lightBlueSplitter = createSplitter(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        for (int i = 0; i < 3; i++) {
            inventory.setItem(matrixCoordinate.access(i, 3), blueSplitter);
        }
        inventory.setItem(matrixCoordinate.access(0, 4), lightBlueSplitter);
        inventory.setItem(matrixCoordinate.access(2, 4), lightBlueSplitter);
        this.recipe = recipe;
        setContent(recipe.getRawRecipe());
        setResultItem(recipe.getResultItem());
    }

    private static ItemStack createSplitter(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void setContent(List<IElement> rawRecipe, IElement resultItem) {
        setContent(rawRecipe);
        setResultItem(resultItem);
    }

    @Override
    public void setContent(List<IElement> rawRecipe) {
        for (int i = 0; i < rawRecipe.size(); i++) {
            setItemAt(i, rawRecipe.get(i));
        }
    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, itemStack.getItemStack());
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
    public void setResultItem(IElement item) {
        int actualIndex = resultSlot.access(matrixCoordinate);
        inventory.setItem(actualIndex, item.getItemStack());
    }

    @Override
    public boolean isValidClick(int rawSlot) {
        return validClicks.contains(rawSlot);
    }
}
