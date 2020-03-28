package cat.nyaa.fusion.inst.element;

import cat.nyaa.fusion.config.element.IElement;
import org.bukkit.inventory.ItemStack;

public class VanillaElement extends BaseElement {

    @Serializable
    String nbt = "";

    public VanillaElement(String name) {
        super(name);
    }

    @Override
    public boolean match(ItemStack itemStack) {
        return false;
    }

    @Override
    public ItemStack getItemStack() {
        return null;
    }
}
