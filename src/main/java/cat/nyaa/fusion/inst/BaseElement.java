package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.IElementHandler;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.nyaacore.BasicItemMatcher;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseElement implements IElement, IElementHandler {
    @Serializable
    protected String name = "";
    @Serializable
    protected BasicItemMatcher.MatchingMode matchingMode = BasicItemMatcher.MatchingMode.EXACT;

    public BaseElement(){
    }

    @Override
    public void queryRecipes(Consumer<List<IRecipe>> consumer) {
        RecipeManager.createQuery(this, consumer);
    }

    public BaseElement(String name) {
        this.name = name;
    }

}
