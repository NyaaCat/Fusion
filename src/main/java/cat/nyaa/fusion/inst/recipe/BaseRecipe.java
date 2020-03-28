package cat.nyaa.fusion.inst.recipe;

import cat.nyaa.fusion.config.NamedFileConfig;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
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

    protected List<ItemStack> itemStacks = new ArrayList<>();
    protected ItemStack resultItem;

    public BaseRecipe(String name) {
        super(name);
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
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        if (!recipiesNbt.isEmpty()) {
            recipiesNbt.forEach(s -> {
                if(s.equals("")){
                    itemStacks.add(null);
                }else {
                    itemStacks.add(ItemStackUtils.itemFromBase64(s));
                }
            });
        }
        if (!recipiesNbt.equals("")){
            resultItem = ItemStackUtils.itemFromBase64(resultItemNbt);
        }
    }

    @Override
    protected String getFileDirName() {
        return "recipes";
    }
}
