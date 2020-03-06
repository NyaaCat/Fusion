package cat.nyaa.fusion.config.recipe;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.inst.IRecipeGUIAccess;
import think.rpgitems.power.PropertyHolder;

import java.util.List;

public interface IRecipe extends PropertyHolder {
    IElement getResultItem();
    List<IElement> getRawRecipe();
    int getElementCount();
    String getId();
    String getName();

    default void updateGUI(IRecipeGUIAccess guiAccess){
        List<IElement> rawRecipe = getRawRecipe();
        guiAccess.setContent(rawRecipe, getResultItem());
    }
}
