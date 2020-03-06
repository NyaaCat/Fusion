package cat.nyaa.fusion.config.recipe;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.inst.IRecipeGUIAccess;
import cat.nyaa.nyaacore.configuration.ISerializable;

import java.util.List;

public interface IRecipe extends ISerializable {
    IElement getResultItem();
    List<IElement> getRawRecipe();
    int getElementCount();

    default void updateGUI(IRecipeGUIAccess guiAccess){
        List<IElement> rawRecipe = getRawRecipe();
        guiAccess.setContent(rawRecipe, getResultItem());
    }
}
