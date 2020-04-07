package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.ui.buttons.GUIButton;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

public interface InteractiveUi {
    void onContentInteract(InventoryInteractEvent event);
    void onResultInteract(InventoryInteractEvent event, ItemStack itemStack);
    void onButtonClicked(InventoryInteractEvent event, GUIButton button);

    boolean isResultClicked(int rawSlot);
    boolean isButtonClicked(int rawSlot);
    boolean isContentClicked(int rawSlot);
}
