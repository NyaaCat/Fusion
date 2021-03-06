package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.fusion.ui.impl.DetailRecipeAccess;
import cat.nyaa.fusion.ui.impl.ListRecipeAccess;
import cat.nyaa.fusion.util.Utils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

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

    private static Map<HumanEntity, Stack<BaseUi>> windowStack = new HashMap<>();

    private static Listener listener = new Listener() {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event){
            Inventory clickedInventory = event.getInventory();

            if (trackedInventory.containsKey(clickedInventory)){
                event.setCancelled(true);
                BaseUi baseUi = trackedInventory.get(clickedInventory);
                if (!isValidMouseAction(event, clickedInventory)){
                    return;
                }
                int rawSlot = ((InventoryClickEvent) event).getRawSlot();
                event.setCancelled(false);
                if (baseUi.isContentClicked(rawSlot)){
                    baseUi.onContentInteract(event);
                }else if (baseUi.isResultClicked(rawSlot)){
                    baseUi.onResultInteract(event, clickedInventory.getItem(rawSlot));
                }else if (baseUi.isButtonClicked(event.getRawSlot())){
                    baseUi.onButtonClicked(event, baseUi.getButtonAt(event.getRawSlot()));
                }else {
                    event.setCancelled(true);
                }
            }
        }

        protected boolean isValidMouseAction(InventoryClickEvent event, Inventory clickedInventory) {
            if (event.getClickedInventory()!= clickedInventory){
                InventoryAction action = event.getAction();

                if (action.equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                    ItemStack currentItem = event.getCurrentItem();
                    return false;
                    //disabled due to some issues.

//                        new BukkitRunnable(){
//                            @Override
//                            public void run() {
//                                int found = 0;
//                                try {
//                                    for (int i = 0; i < 9; i++) {
//                                        if (baseUi.getItemAt(i).equals(RecipeManager.getEmptyElement())) {
//                                            found = i;
//                                            baseUi.setItemAt(i, currentItem);
//                                            event.getView().setItem(event.getRawSlot(), new ItemStack(Material.AIR));
//                                            break;
//                                        }
//                                    }
//                                }catch (Exception e){
//                                    Bukkit.getLogger().log(Level.SEVERE, "exception during item transaction. rolling back.");
//                                    baseUi.setItemAt(found, RecipeManager.getEmptyElement().getItemStack());
//                                    event.getView().setItem(event.getRawSlot(), currentItem);
//                                }
//                            }
//                        }.runTaskLater(FusionPlugin.plugin, 1);
                }
                event.setCancelled(false);
                return false;
            }
            return true;
        }

        @EventHandler
        public void onSampleItemClick(InventoryClickEvent event){
            ItemStack currentItem = event.getCurrentItem();
            if (!trackedInventory.containsKey(event.getClickedInventory())){
                if (Utils.isFakeItem(currentItem)){
                    event.setCancelled(true);
                    event.setCurrentItem(null);
                }
            }
        }
        @EventHandler
        public void onInventoryDrag(InventoryDragEvent event){
            Inventory clickedInventory = event.getInventory();

            if (trackedInventory.containsKey(clickedInventory)){
                event.setCancelled(true);
                BaseUi baseUi = trackedInventory.get(clickedInventory);
                if (event.getRawSlots().size() == 1){
                    if (event.getRawSlots().iterator().next() == 9){
                        InventoryClickEvent event1 = new InventoryClickEvent(event.getView(), InventoryType.SlotType.CONTAINER, 9, ClickType.LEFT, InventoryAction.PLACE_ALL);
                        baseUi.onContentInteract(event1);
                        event.setCancelled(event1.isCancelled());
                        return;
                    }
                }

                Set<Integer> inventorySlots = ((InventoryDragEvent) event).getRawSlots();
                boolean invalid = inventorySlots.stream()
                        .mapToInt(Integer::intValue)
                        .anyMatch(integer -> !baseUi.isContentClicked(integer));
                int size = event.getView().getTopInventory().getSize();
                boolean related = event.getRawSlots().stream().mapToInt(Integer::intValue)
                        .anyMatch(integer -> integer < size);
                event.setCancelled(invalid && related);
                if (!invalid){
                    baseUi.onContentInteract(event);
                }
            }
        }
        @EventHandler
        public void onInventoryInteract(InventoryInteractEvent event){
            Inventory clickedInventory = event.getInventory();

            if (trackedInventory.containsKey(clickedInventory)){
                event.setCancelled(true);
                BaseUi baseUi = trackedInventory.get(clickedInventory);
                if (event instanceof InventoryDragEvent){
                    boolean invalid = ((InventoryDragEvent) event).getInventorySlots().stream()
                            .mapToInt(Integer::intValue)
                            .anyMatch(integer -> !baseUi.isContentClicked(integer));
                    event.setCancelled(invalid);
                    return;
                }
                if (event instanceof InventoryClickEvent){
                    int rawSlot = ((InventoryClickEvent) event).getRawSlot();
                    boolean validClick = baseUi.isContentClicked(rawSlot);
                    event.setCancelled(false);
                    if (validClick){
                        baseUi.onRawInteract(event);
                    }else if (baseUi.isResultClicked(rawSlot)){
                        baseUi.onResultInteract(event, clickedInventory.getItem(rawSlot));
                    }else {
                        event.setCancelled(false);
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
                trackedInventory.remove(clickedInventory);
            }
        }
    };

    private static BiMap<Inventory, BaseUi> trackedInventory = HashBiMap.create();

    public static CraftingTableAccess newCraftingSession(Player player){
        Inventory fusion = Bukkit.createInventory(player, InventoryType.CHEST, "Fusion");
        CraftingTableAccess craftingTableAccess = new CraftingTableAccess(Utils.getGuiSection(0, 2, 3, 5), fusion);
        trackedInventory.put(fusion, craftingTableAccess);
        return craftingTableAccess;
    }

    public static ListRecipeAccess newListRecipeAccess(Player player){
        Inventory fusion = Bukkit.createInventory(player, InventoryType.CHEST, "Fusion");
        List<IRecipe> recipes = ListRecipeAccess.getDefaultRecipes();
        ListRecipeAccess listRecipeAccess = new ListRecipeAccess(Utils.getGuiSection(0, 2, 3, 5), fusion, recipes);
        listRecipeAccess.refreshUi();
        trackedInventory.put(fusion, listRecipeAccess);
        Stack<BaseUi> stack = windowStack.computeIfAbsent(player, humanEntity -> new Stack<>());
        stack.clear();
        return listRecipeAccess;
    }

    public static DetailRecipeAccess newDetailRecipeAccess(Player player, IRecipe recipe){
        Inventory fusion = Bukkit.createInventory(player, InventoryType.CHEST, "Fusion");
        List<IRecipe> recipes = RecipeManager.getInstance().getRecipes();
        int recipeIndex = recipes.indexOf(recipe);
        DetailRecipeAccess detailRecipeAccess = new DetailRecipeAccess(Utils.getGuiSection(0, 2, 3, 5), fusion, recipes, recipeIndex);
        detailRecipeAccess.refreshUi();
        trackedInventory.put(fusion, detailRecipeAccess);
        return detailRecipeAccess;
    }

    public static DetailRecipeAccess newDetailRecipeAccess(Player player, List<IRecipe> recipes, int index){
        Inventory fusion = Bukkit.createInventory(player, InventoryType.CHEST, "Fusion");
        DetailRecipeAccess detailRecipeAccess = new DetailRecipeAccess(Utils.getGuiSection(0, 2, 3, 5), fusion, recipes, index);
        detailRecipeAccess.refreshUi();
        trackedInventory.put(fusion, detailRecipeAccess);
        return detailRecipeAccess;
    }

    public static void closeAllInventory() {
        trackedInventory.values().forEach(baseUi -> baseUi.getInventory().getViewers().forEach(humanEntity -> {
            InventoryCloseEvent inventoryCloseEvent = new InventoryCloseEvent(humanEntity.getOpenInventory());
            baseUi.onInventoryClose(inventoryCloseEvent);
            humanEntity.closeInventory();
        }));
    }

    public static void openUiFor(HumanEntity whoClicked, BaseUi child, BaseUi parent) {
        Stack<BaseUi> stack = windowStack.computeIfAbsent(whoClicked, player -> new Stack<>());
        if (parent == null){
            stack.clear();
        }else {
            stack.push(parent);
        }
        whoClicked.openInventory(child.getInventory());
    }

    public static void onUiBack(HumanEntity player){
        Stack<BaseUi> stack = windowStack.computeIfAbsent(player, humanEntity -> new Stack<>());
        if (stack.empty()){
            return;
        }
        BaseUi pop = stack.pop();
        Inventory inventory = pop.getInventory();
        trackedInventory.put(inventory, pop);
        pop.onReopen();
        Utils.newChain().delay(1)
                .sync(() ->{
                    player.openInventory(inventory);
                }).execute();
    }
}
