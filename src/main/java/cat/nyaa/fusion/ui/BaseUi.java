package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.config.element.IElement;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseUi implements IRecipeGUIAccess{
    protected final MatrixCoordinate matrixCoordinate;
    protected final Inventory inventory;
    protected final UiCoordinate resultSlot;
    protected final List<UiCoordinate> slots;
    protected final List<Integer> validClicks;

    protected BaseUi(List<Integer> sectionIndexes, Inventory inventory){
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
        addSplitters();
    }

    protected void addSplitters() {
        ItemStack blueSplitter = createSplitter(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack lightBlueSplitter = createSplitter(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        for (int i = 0; i < 3; i++) {
            inventory.setItem(matrixCoordinate.access(i, 3), blueSplitter);
        }
        inventory.setItem(matrixCoordinate.access(0, 4), lightBlueSplitter);
        inventory.setItem(matrixCoordinate.access(2, 4), lightBlueSplitter);
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
    public List<IElement> getContent() {
        List<IElement> content = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            content.add(getItemAt(i));
        }
        return content;
    }

    @Override
    public void setResultItem(IElement item) {
        inventory.setItem(resultSlot.access(matrixCoordinate), item.getItemStack());
    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, itemStack.getItemStack());
    }

}
