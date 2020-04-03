package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.I18n;
import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.fusion.ui.impl.InspectSessionAccess;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public class UiManager {
    public static UiManager INSTANCE;
    private static boolean registered = false;

    private UiManager(){
        if (!registered){
            Bukkit.getServer().getPluginManager().registerEvents(listener, FusionPlugin.plugin);
            registered = true;
        }
    }

    public static UiManager getInstance(){
        if (INSTANCE == null){
            synchronized (UiManager.class){
                if (INSTANCE == null){
                    INSTANCE = new UiManager();
                }
            }
        }
        return INSTANCE;
    }

    private Map<Inventory, CraftingTableAccess> craftingSession = new HashMap<>();
    private Map<Inventory, BaseUi> querySession = new HashMap<>();
    private static Map<Player, Stack<IQueryUiAccess>> windowStack = new HashMap<>();

    public boolean isCraftSession(Inventory clickedInventory) {
        return craftingSession.containsKey(clickedInventory);
    }

    public IRecipeGUIAccess getSession(CraftingInventory inv) {
        return craftingSession.get(inv);
    }

    private static Listener listener = new Listener() {
        @EventHandler
        public void onInventoryClick(InventoryInteractEvent event){
            Inventory clickedInventory = event.getInventory();

            if (trackedInventory.containsKey(clickedInventory)){
                BaseUi baseUi = trackedInventory.get(clickedInventory);
                if (event instanceof InventoryDragEvent){
                    ((InventoryDragEvent) event).getInventorySlots().stream()
                            .mapToInt(Integer::intValue)
                            .anyMatch(integer -> ! baseUi.isValidClick(integer))
                    return;
                }
                if (event instanceof InventoryClickEvent){
                    boolean validClick = baseUi.isValidClick(event.getRawSlot());
                    if (validClick){
                        baseUi.onRawInteract(event);
                    }
                    //todo catch events and pass to next level.
                    return;
                }

            }
        }

        @EventHandler
        public void onInventoryClose(InventoryCloseEvent event){
            Inventory clickedInventory = event.getInventory();
            if (trackedInventory.containsKey(clickedInventory)){
                BaseUi baseUi = trackedInventory.get(clickedInventory);
                baseUi.onInventoryClose(event);
            }
        }
    };

    private static BiMap<Inventory, BaseUi> trackedInventory = HashBiMap.create();

}
