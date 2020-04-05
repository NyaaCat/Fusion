package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.FusionPlugin;
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
import org.bukkit.scheduler.BukkitRunnable;
import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

import java.lang.ref.WeakReference;
import java.util.Optional;

@ElementMeta(priority = EventPriority.HIGH)
public class RPGItemElement extends BaseElement {
    @Serializable
    String itemName = "";

    private RPGItem elementRgi;
    private ItemStack rgiItem;

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
         return rgiItem == null ? createAndGetItem() : rgiItem;
    }

    private ItemStack createAndGetItem() {
        ItemStack itemStack = elementRgi.toItemStack();
        new BukkitRunnable() {
            @Override
            public void run() {
                elementRgi.updateItem(itemStack);
            }
        }.runTaskLater(FusionPlugin.plugin, 1);
        return itemStack;
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
        RPGItem rpgItem = ItemManager.toRPGItemByMeta(itemStack).orElse(null);
        if (rpgItem == null){
            throw new IllegalStateException("item is not RGI, but caught by handler: "+"rpgitem");
        }else {
            RPGItemElement rpgItemElement = new RPGItemElement(String.valueOf(rpgItem.getUid()), rpgItem);
            rpgItemElement.rgiItem = itemStack;
            return rpgItemElement;
        }
    }

    @Override
    public String serialize() {
        return "rpgitem:"+itemName;
    }

    @Override
    public IElement createFromString(String serialized) {
        String[] split = serialized.split(":");
        if (split.length < 2){
            return RecipeManager.getEmptyElement();
        }
        String name = split[1];
        RPGItem rpgItem = ItemManager.getItem(name).orElse(null);
        if (rpgItem == null){
            return RecipeManager.getEmptyElement();
        }
        ItemStack itemStack = rpgItem.toItemStack();
        return RecipeManager.getItem(itemStack);
    }

    @Override
    public String getNamespace() {
        return "rpgitem";
    }
}
