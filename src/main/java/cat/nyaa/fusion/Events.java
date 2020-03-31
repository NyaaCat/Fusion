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
        initItem();
    }

    private void initItem() {
        ItemMeta itemMeta = HINT_ITEM_LOADING.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(I18n.format("hint.name.loading"));
            itemMeta.setLore(Arrays.asList(I18n.format("hint.lore.loading").split("\n")));
        }
        MATCHER_LOADING.itemTemplate = HINT_ITEM_LOADING;
    }

    private static final ItemStack HINT_ITEM_LOADING = new ItemStack(Material.BARRIER);
    private static final BasicItemMatcher MATCHER_LOADING = new BasicItemMatcher();

    @EventHandler(priority = EventPriority.HIGH)
    public void onCraftChange(InventoryClickEvent event){
        Inventory clickedInventory = event.getClickedInventory();
        if (UiManager.getInstance().isCraftSession(clickedInventory) && clickedInventory instanceof CraftingInventory){
            event.setCancelled(true);
            CraftingInventory inv = (CraftingInventory) event.getInventory();
            int slot = event.getSlot();
            if (slot == 9){
                if (isResultValid(inv)){
                    ((CraftingInventory) clickedInventory).setMatrix(new ItemStack[9]);
                    event.setCancelled(false);
                    return;
                }
            }
            Utils.newChain().sync(input -> {
                inv.setResult(HINT_ITEM_LOADING);
                return null;
            }).async(input -> {
                IRecipeGUIAccess session = UiManager.getInstance().getSession(inv);
                return BukkitFuser.getInstance().fuseItem(session);
            }).sync(input -> {
                inv.setResult(input);
                return null;
            }).execute();
        }
    }

    private boolean isResultValid(CraftingInventory clickedInventory) {
        ItemStack result = clickedInventory.getResult();
        boolean valid = result != null && !result.getType().equals(Material.AIR) && !MATCHER_LOADING.matches(result);
        return valid;
    }
}
