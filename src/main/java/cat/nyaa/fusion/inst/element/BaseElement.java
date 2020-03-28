package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.nyaacore.BasicItemMatcher;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseElement implements IElement {
    @Serializable
    protected String name = "";
    @Serializable
    protected BasicItemMatcher.MatchingMode matchingMode = BasicItemMatcher.MatchingMode.EXACT;

    @Override
    public void queryRecipes(Consumer<List<IRecipe>> consumer) {
        RecipeManager.createQuery(this, consumer);
    }

    public BaseElement(String name) {
        this.name = name;
    }

}
