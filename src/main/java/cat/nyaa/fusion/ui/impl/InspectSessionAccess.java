package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.I18n;
import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import cat.nyaa.fusion.ui.UiType;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static cat.nyaa.fusion.util.Utils.getFakeItem;
import static cat.nyaa.fusion.util.Utils.getGuiSection;

public class InspectSessionAccess implements IQueryUiAccess {

    private static BiMap<Inventory, InspectSessionAccess> trackedInventory = HashBiMap.create();
    private static Map<Integer, GUIButton> buttons = new HashMap<>();
    private static IRecipeGUIAccess recipeGui;
    private static List<Integer> recipeSpace = getGuiSection(0, 2, 3, 5);

    Inventory largeChestInventory;
    private Player player;
    private UiType uiType = UiType.LIST;
    private List<IRecipe> recipes = new ArrayList<>();
    private Stack<IQueryUiAccess> stack = new Stack<>();
    private int page = 0;

    public InspectSessionAccess(Player player, List<IRecipe> recipes, UiType uiType){
        this.player = player;
        this.recipes = recipes;
        this.uiType = uiType;
        if (!registered){
            Bukkit.getServer().getPluginManager().registerEvents(listener, FusionPlugin.plugin);
            registered = true;
        }
        ensureInventory();
        stack.push(this);
        refreshUi();
    }

    public InspectSessionAccess(InspectSessionAccess parent, List<IRecipe> recipes, UiType uiType){
        this(parent.player, recipes, uiType);
        this.stack = parent.stack;
        stack.push(this);
    }

    @Override
    public void setRecipes(List<IRecipe> recipes) {
        this.recipes = recipes;

    }

    @Override
    public void setUiType(UiType type) {
        this.uiType = type;
        refreshUi();
    }

    @Override
    public List<IRecipeGUIAccess> getRecipeGUIs() {
        return Collections.singletonList(recipeGui);
    }

    @Override
    public void refreshUi() {
        switch (uiType){
            case LIST:
                recipeGui = new ListRecipeAccess(recipeSpace, largeChestInventory, recipes);
                List<IElement> elements = new ArrayList<>();
                recipes.stream()
                        .skip(page * getPageSize())
                        .limit(15)
                        .forEach(recipe -> {
                            ItemStack itemStack = recipe.getResultItem().getItemStack();
                            ItemStack fakeItem = getFakeItem(itemStack);
                            elements.add(RecipeManager.getItem(fakeItem));
                        });
                recipeGui.setContent(elements);
                break;
            case DETAILED:
                recipeGui = new DetailRecipeAccess(recipeSpace, largeChestInventory, recipes.get(page));
                break;
            case CRAFTING:
                recipeGui = new CraftingTableAccess(recipeSpace, largeChestInventory);
        }
    }

    @Override
    public void push(IQueryUiAccess child) {
        stack.push(child);
    }

    @Override
    public IQueryUiAccess pop() {
        return stack.pop();
    }

    @Override
    public IQueryUiAccess peek() {
        return stack.peek();
    }

    private static Listener listener = new Listener() {
        @EventHandler
        public void onInventoryClick(InventoryClickEvent event){
            if (trackedInventory.containsKey(event.getClickedInventory())){
                event.setCancelled(true);
                InspectSessionAccess inspectSessionAccess = trackedInventory.get(event.getClickedInventory());
                if (event.getRawSlot() < 0 || event.getRawSlot() >= inspectSessionAccess.largeChestInventory.getSize()){
                    return;
                }
                if (isClickingButton(event.getRawSlot())){
                    inspectSessionAccess.onButtonClick(event.getRawSlot());
                }else if (isClickingRecipe(event.getRawSlot())){
                    ItemStack clickedItem = event.getView().getItem(event.getRawSlot());
                    //todo to checkout recipes related to this item
                }
            }
        }
    };

    private static boolean isClickingRecipe(int rawSlot) {
        return recipeGui == null ? false : recipeGui.isValidClick(rawSlot);
    }

    private static boolean isClickingButton(int rawSlot) {
        return buttons.containsKey(rawSlot);
    }

    private void onButtonClick(int i) {
        GUIButton guiButton = buttons.get(i);
        if (guiButton == null){
            return;
        }
        guiButton.doAction(this);
    }

    private static boolean registered = false;

    public void ensureInventory(){
        if (largeChestInventory == null || largeChestInventory.getType().equals(InventoryType.CHEST) || largeChestInventory.getSize() != 27){
            largeChestInventory = Bukkit.createInventory(player, 27, I18n.format("inventory.title", this.getCurrentPage(), this.getSize()));
            if (trackedInventory.containsValue(this)){
                trackedInventory.forcePut(largeChestInventory, this);
                return;
            }
            trackedInventory.put(largeChestInventory, this);
        }
    }

    public void openForPlayer(Player player){
        ensureInventory();
        refreshUi();
        player.openInventory(largeChestInventory);
    }

    @Override
    public int getPageSize() {
        int pageSize = 1;
        switch (uiType) {
            case LIST:
                pageSize = 15;
                break;
            case DETAILED:
                break;
        }
        return pageSize;
    }

    @Override
    public int getSize() {
        return recipes.size();
    }

    @Override
    public void setPage(int page) {
        this.page = page;
        refreshUi();
    }

    @Override
    public int getCurrentPage() {
        return this.page;
    }

    @Override
    public GUIButton getButtonAt(int index) {
        return buttons.get(index);
    }

    @Override
    public void setButtonAt(int index, GUIButton button) {
        buttons.put(index, button);
    }
}
