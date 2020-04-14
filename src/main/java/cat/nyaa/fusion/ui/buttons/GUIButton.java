package cat.nyaa.fusion.ui.buttons;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public abstract class GUIButton implements ISerializable, Cloneable{
    ItemStack itemStack;

    public abstract String getAction();

    public abstract void doAction(InventoryInteractEvent event, IQueryUiAccess iQueryUiAccess);

    protected int getTotalPages(IQueryUiAccess iQueryUiAccess){
        int size = iQueryUiAccess.getSize();
        int pageSize = iQueryUiAccess.getPageSize();
        int totalPages = (int) Math.ceil((double)size/(double) Math.max(pageSize, 0.1));
        return totalPages;
    }

    public abstract ItemStack getModel(IQueryUiAccess iQueryUiAccess);

    @Override
    public GUIButton clone() {
        try {
            return (GUIButton) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
