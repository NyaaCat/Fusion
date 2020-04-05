package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.inst.BaseElement;
import cat.nyaa.fusion.inst.ElementMeta;
import cat.nyaa.nyaacore.BasicItemMatcher;
import cat.nyaa.nyaacore.utils.ItemStackUtils;
import org.bukkit.Material;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;

@ElementMeta(priority = EventPriority.LOWEST)
public class VanillaElement extends BaseElement {
    @Serializable
    String nbt = "";

    ItemStack itemStack;

    BasicItemMatcher itemMatcher = new BasicItemMatcher();
    public static final VanillaElement EMPTY_ELEMENT = new VanillaElement("empty");

    public VanillaElement() {
        super();
    }

    public VanillaElement(String name) {
        super(name);
    }

    @Override
    public boolean match(ItemStack itemStack) {
        if(itemStack.getType().isAir()){
            return this.itemStack.getType().isAir();
        }
        return itemMatcher.matches(itemStack);
    }

    @Override
    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    public void setItemStack(ItemStack itemStack){
        if (itemStack == null || itemStack.getType().isAir()){
            nbt = "";
            this.itemStack = new ItemStack(Material.AIR);
            itemMatcher.itemTemplate = this.itemStack;
            return;
        }
        nbt = ItemStackUtils.itemToBase64(itemStack);
        this.itemStack = itemStack;
        itemMatcher.itemTemplate = this.itemStack;
    }

    @Override
    public boolean isSuitableElement(ItemStack itemStack) {
        return true;
    }

    @Override
    public IElement create(ItemStack itemStack) {
        if (itemStack == null){
            return EMPTY_ELEMENT;
        }
        VanillaElement vanillaElement = new VanillaElement(itemStack.getType().name());
        vanillaElement.setItemStack(itemStack);
        return vanillaElement;
    }

    @Override
    public String serialize() {
        return nbt;
    }

    @Override
    public boolean isSuitableElement(String s) {
        return true;
    }

    @Override
    public String getNamespace() {
        return "vanilla";
    }
}
