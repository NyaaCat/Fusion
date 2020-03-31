package cat.nyaa.fusion.inst.recipe;

import cat.nyaa.fusion.config.NamedFileConfig;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.inst.RecipeMatchingMode;
import cat.nyaa.fusion.inst.element.VanillaElement;
import cat.nyaa.nyaacore.configuration.ISerializable;
import cat.nyaa.nyaacore.utils.ItemStackUtils;
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

    protected List<IElement> iElements = new ArrayList<>();
    protected ItemStack resultItem;

    public BaseRecipe(String name) {
        super(name);
    }

    public void addElement(IElement element){
        iElements.add(element);
        recipiesNbt.add(element.getElementHandler().serialize());
    }

    public void clear(){
        iElements.clear();
        resultItem = null;
        recipiesNbt.clear();
        resultItemNbt = "";
    }

    @Override
    public IElement getResultItem() {
        return null;
    }

    @Override
    public List<IElement> getRawRecipe() {
        return null;
    }

    @Override
    public int getElementCount() {
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean matches(List<IElement> matrix) {
        for (int i = 0; i < iElements.size(); i++) {
            IElement itemStack = iElements.get(i);
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
                    iElements.add(VanillaElement.EMPTY_ELEMENT);
                }else {
                    iElements.add(RecipeManager.getItem(s));
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
    }
}
