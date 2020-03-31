package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.inst.BaseElement;
import cat.nyaa.fusion.inst.ElementMeta;
import cat.nyaa.fusion.inst.IElementHandler;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.nyaacore.configuration.ISerializable;
import cat.nyaa.nyaacore.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

import java.lang.ref.WeakReference;
import java.util.Optional;

@ElementMeta(priority = EventPriority.HIGH)
public class RPGItemElement extends BaseElement {
    @Serializable
    String itemName = "";

    private RPGItem elementRgi;
    private WeakReference<ItemStack> itemReference = new WeakReference<>(new ItemStack(Material.AIR));

    public RPGItemElement() {
        super();
    }

    public RPGItemElement(String name, RPGItem elementRgi) {
        super(name);
        setRGI(elementRgi);
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

    public RPGItem getRGI() {
        return elementRgi;
    }

    public void setRGI(RPGItem elementRgi) {
        this.elementRgi = elementRgi;
        if (elementRgi == null){
            itemName = "";
            return;
        }
        itemName = String.valueOf(elementRgi.getName());
    }

    @Override
    public boolean isSuitableElement(ItemStack itemStack) {
        if (IElementHandler.isModel(itemStack)){
            return false;
        }
        return ItemManager.toRPGItemByMeta(itemStack).isPresent();
    }

    @Override
    public void deserialize(ConfigurationSection config) {
        ISerializable.deserialize(config, this);
        elementRgi = ItemManager.getItem(itemName).orElse(null);
    }

    @Override
    public IElement create(ItemStack itemStack) {
        RPGItem rpgItem = ItemManager.toRPGItem(itemStack).orElse(null);
        if (rpgItem == null){
            throw new IllegalStateException("item is not RGI, but caught by handler: "+"rpgitem");
        }else {
            return new RPGItemElement(String.valueOf(rpgItem.getUid()), rpgItem);
        }
    }

    @Override
    public String serialize() {
        return "rpgitem:"+itemName;
    }

    @Override
    public IElement createFromString(String serialized) {
        String name = serialized.split(":")[1];
        RPGItem rpgItem = ItemManager.getItem(name).orElse(null);
        if (rpgItem == null){
            return RecipeManager.getEmptyElement();
        }
        return RecipeManager.getItem(rpgItem.toItemStack());
    }

    @Override
    public String getCacheKey(ItemStack itemStack) {
        RPGItem rpgItem = ItemManager.toRPGItem(itemStack).orElse(null);
        return rpgItem == null ? ItemStackUtils.itemToBase64(itemStack) :"rpgitem:"+rpgItem.getName();
    }

    @Override
    public String getNamespace() {
        return "rpgitem";
    }
}
