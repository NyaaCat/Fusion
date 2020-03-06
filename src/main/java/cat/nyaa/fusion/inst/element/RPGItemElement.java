package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.inventory.ItemStack;
import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Optional;

public class RPGItemElement implements IElement {
    private RPGItem elementRgi;
    private WeakReference<ItemStack> itemReference;

    public RPGItemElement(RPGItem elementRgi) {
        this.elementRgi = elementRgi;
    }

    @Override
    public boolean match(ItemStack itemStack) {
        Optional<RPGItem> opt = ItemManager.toRPGItemByMeta(itemStack);
        if (!opt.isPresent()){
            return false;
        }
        RPGItem rpgItem = opt.get();
        return rpgItem.getUid() == elementRgi.getUid();
    }

    @Override
    public ItemStack getItemStack() {
        ItemStack itemStack = itemReference.get();
        return itemStack == null ? createAndGetItem() : itemReference.get();
    }

    private ItemStack createAndGetItem() {
        itemReference = new WeakReference<>(elementRgi.toItemStack());
        return itemReference.get();
    }

}
