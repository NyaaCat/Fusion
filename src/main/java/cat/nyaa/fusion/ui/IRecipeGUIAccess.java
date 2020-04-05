package cat.nyaa.fusion.ui;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface IRecipeGUIAccess {
    void setContent(List<ItemStack> rawRecipe, ItemStack resultItem);
    void setContent(List<ItemStack> rawRecipe);
    List<ItemStack> getContent();

    void setItemAt(int row, int col, ItemStack itemStack);
    void setItemAt(int index, ItemStack itemStack);
    ItemStack getItemAt(int row, int col);
    ItemStack getItemAt(int index);

    void setResultItem(ItemStack item);

    boolean isValidClick(int rawSlot);
}
