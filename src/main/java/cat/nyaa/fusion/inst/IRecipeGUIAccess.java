package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.config.element.IElement;

import java.util.List;

public interface IRecipeGUIAccess {
    void setContent(List<IElement> rawRecipe, IElement resultItem);
    void setContent(List<IElement> rawRecipe);

    void setItemAt(int row, int col, IElement itemStack);
    void setItemAt(int index, IElement itemStack);
    IElement getItemAt(int row, int col);
    IElement getItemAt(int index);

    void setResultItem(IElement item);
}
