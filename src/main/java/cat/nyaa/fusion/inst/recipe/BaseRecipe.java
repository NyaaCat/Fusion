package cat.nyaa.fusion.inst.recipe;

import cat.nyaa.fusion.config.NamedFileConfig;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.inst.RecipeMatchingMode;
import cat.nyaa.fusion.inst.element.VanillaElement;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BaseRecipe extends NamedFileConfig implements IRecipe {
    @Serializable
    protected String name = "";

    @Serializable(name = "recipe")
    protected List<String> recipiesNbt = new ArrayList<>();

    @Serializable(name = "result")
    protected String resultItemNbt = "";

    @Serializable(name = "matching_mode")
    protected RecipeMatchingMode matchingMode = RecipeMatchingMode.EXACT;

    protected List<IElement> recipies = new ArrayList<>();
    protected ItemStack resultItem;

    public BaseRecipe(String name) {
        super(name);
    }

    public void addElement(IElement element){
        recipies.add(element);
        recipiesNbt.add(element.getElementHandler().serialize());
    }

    public void clear(){
        recipies.clear();
        resultItem = null;
        recipiesNbt.clear();
        resultItemNbt = "";
    }

    @Override
    public IElement getResultItem() {
        return RecipeManager.getItem(resultItem);
    }

    Integer count;

    @Override
    public List<IElement> getRawRecipe() {
        return recipies;
    }

    @Override
    public int getElementCount() {
        if (count == null){
            count = (int) recipies.stream().filter(element -> !element.equals(RecipeManager.getEmptyElement())).count();
        }
        return count;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(List<IElement> matrix) {
        for (int i = 0; i < recipies.size(); i++) {
            IElement itemStack = recipies.get(i);
            IElement item = matrix.get(i);
            if (itemStack == null) {
                if (item != null) {
                    return false;
                }
                continue;
            }
            if (!itemStack.match(item.getItemStack())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        if (!recipiesNbt.isEmpty()) {
            recipiesNbt.forEach(s -> {
                if(s.equals("")){
                    recipies.add(VanillaElement.EMPTY_ELEMENT);
                }else {
                    recipies.add(RecipeManager.getItem(s));
                }
            });
        }
        if (!resultItemNbt.equals("")){
            resultItem = RecipeManager.getItem(resultItemNbt).getItemStack();
        }
    }

    @Override
    protected String getFileDirName() {
        return "recipes";
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResult(IElement item) {
        resultItemNbt = item.getElementHandler().serialize();
        resultItem = item.getItemStack();
    }
}
