package cat.nyaa.fusion;

import cat.nyaa.fusion.fuser.BukkitFuser;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.util.Utils;
import cat.nyaa.nyaacore.BasicItemMatcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Events implements Listener {
    private FusionPlugin plugin;

    public Events(FusionPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftChange(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (UiManager.getInstance().isCraftSession(clickedInventory) && clickedInventory instanceof CraftingInventory){

        }
    }
}
