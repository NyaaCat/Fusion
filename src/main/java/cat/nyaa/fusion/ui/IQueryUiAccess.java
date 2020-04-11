package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public interface IQueryUiAccess extends InventoryHolder {
    List<IRecipeGUIAccess> getRecipeGUIs();

    void setRecipes(List<IRecipe> recipes);
    void setPage(int page);
    void refreshUi();
    int getCurrentPage();
    int getPageSize();
    int getSize();

    GUIButton getButtonAt(int index);
    void setButtonAt(int index, GUIButton button);

    void onReopen();
}
