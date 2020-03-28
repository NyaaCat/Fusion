package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.ui.buttons.GUIButton;

import java.util.List;

public interface IQueryUiAccess {
    List<IRecipeGUIAccess> getRecipeGUIs();

    void setUiType(UiType type);
    void setRecipes(List<IRecipe> recipes);
    void setPage(int page);
    void refreshUi();
    int getCurrentPage();
    int getPageSize();
    int getSize();

    void push(IQueryUiAccess child);
    IQueryUiAccess pop();
    IQueryUiAccess peek();

    GUIButton getButtonAt(int index);
    void setButtonAt(int index, GUIButton button);
}
