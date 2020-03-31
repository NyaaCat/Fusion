package cat.nyaa.fusion.inst;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.element.IElementHolder;
import cat.nyaa.nyaacore.utils.ItemStackUtils;
import cat.nyaa.nyaacore.utils.ItemTagUtils;
import org.bukkit.inventory.ItemStack;

import static cat.nyaa.fusion.util.Utils.getFakeItem;

public interface IElementHandler extends IElementHolder {
    boolean isSuitableElement(ItemStack itemStack);
    default boolean isSuitableElement(String s){
        if (s.contains(":")){
            String prefix = s.split(":")[0];
            return prefix.equals(getNamespace());
        }else return true;
    }

    String getNamespace();

    IElement create(ItemStack itemStack);

    default IElement createFromString(String serialized){
        return RecipeManager.getItem(ItemStackUtils.itemFromBase64(serialized));
    }
    String serialize();

    @Override
    default IElementHandler getElementHandler(){
        return this;
    }

    default String getCacheKey(ItemStack itemStack){
        return ItemStackUtils.itemToBase64(itemStack);
    }

    static boolean isModel(ItemStack itemStack) {
        return ItemTagUtils.getInt(itemStack, "fusion_model").isPresent();
    }

    static ItemStack getModel(ItemStack itemStack) {
        return getFakeItem(itemStack);
    }
}
