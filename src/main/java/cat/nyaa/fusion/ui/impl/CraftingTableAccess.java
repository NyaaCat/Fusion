package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.I18n;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.fuser.BukkitFuser;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.BaseUi;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import cat.nyaa.fusion.ui.UiCoordinate;
import cat.nyaa.fusion.util.Utils;
import cat.nyaa.nyaacore.BasicItemMatcher;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CraftingTableAccess extends BaseUi {

    private IRecipe recipe;

    public CraftingTableAccess(List<Integer> recipeSpace, Inventory largeChestInventory) {
        super(recipeSpace, largeChestInventory);
    }

    @Override
    protected void addSplitters() {
        super.addSplitters();
        ItemStack blueSplitter = createSplitter(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack lightBlueSplitter = createSplitter(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        for (int i = 0; i < 3; i++) {
            inventory.setItem(matrixCoordinate.access(i, 3), blueSplitter);
        }
        inventory.setItem(matrixCoordinate.access(0, 4), lightBlueSplitter);
        inventory.setItem(matrixCoordinate.access(2, 4), lightBlueSplitter);
    }


    @Override
    public void setItemAt(int index, ItemStack itemStack) {
        setItemAt(index / 3, index % 3, itemStack);
    }

    @Override
    public ItemStack getItemAt(int row, int col) {
        return inventory.getItem(matrixCoordinate.access(row, col));
    }

    @Override
    public ItemStack getItemAt(int index) {
        return getItemAt(index/3, index%3);
    }

    @Override
    protected void initSlots(MatrixCoordinate matrixCoordinate) {
        super.initSlots(matrixCoordinate);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                UiCoordinate e = new UiCoordinate(i, j);
                slots.add(e);
                validClicks.add(e.access(matrixCoordinate));
            }
        }
    }

    @Override
    public boolean isValidClick(int rawSlot) {
        return validClicks.contains(rawSlot);
    }

    @Override
    public void refreshUi() {
        checkAndRefreshRecipe();
    }

    @Override
    public int getPageSize() {
        return 0;
    }

    @Override
    public void onContentInteract(InventoryInteractEvent event) {
        event.setCancelled(false);
        checkAndRefreshRecipe();
    }

    @Override
    public void onResultInteract(InventoryInteractEvent event, ItemStack itemStack) {
        if (checking || !(event instanceof InventoryClickEvent)) {
            event.setCancelled(true);
            return;
        }
        InventoryAction action = ((InventoryClickEvent) event).getAction();
        IRecipe recipe = getRecipe();
        if (recipe == null) {
            return;
        }
        event.setCancelled(false);
        switch (action) {
            case PICKUP_ALL:
                costItem();
                break;
            case PICKUP_ONE:
                costItem();
                break;
            case MOVE_TO_OTHER_INVENTORY:
                costItem();
            default:
                event.setCancelled(true);
                break;
        }
    }

    private boolean validate(IRecipe recipe) {
        return recipe.matches(getContent());
    }


    private IRecipe getRecipe() {
        return recipe;
    }

    private void costItem() {
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = getItemAt(i);
            if (!itemStack.getType().isAir()) {
                int amount = itemStack.getAmount();
                if (amount == 1){
                    itemStack.setType(Material.AIR);
                    continue;
                }
                itemStack.setAmount(amount - 1);
            }
        }
    }


    private static final ItemStack HINT_ITEM_LOADING = new ItemStack(Material.BARRIER);
    private static final BasicItemMatcher MATCHER_LOADING = new BasicItemMatcher();

    private static void initItem() {
        ItemMeta itemMeta = HINT_ITEM_LOADING.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(I18n.format("hint.name.loading"));
            itemMeta.setLore(Arrays.asList(I18n.format("hint.lore.loading").split("\n")));
        }
        HINT_ITEM_LOADING.setItemMeta(itemMeta);
    }

    static {
        initItem();
    }

    boolean checking = false;

    private void checkAndRefreshRecipe() {
        Utils.newChain().delay(1).sync(input -> {
            checking = true;
            setResultItem(HINT_ITEM_LOADING.clone());
            return null;
        }).async(input -> {
            try{
                return BukkitFuser.getInstance().fuseItem(this);
            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }).sync(input -> {
            checking = false;
            recipe = input;
            if (input == null){
                setResultItem(RecipeManager.getEmptyElement().getItemStack());
                return null;
            }
            setResultItem(input.getResultItem());
            return null;
        }).execute();
    }

    private boolean isResultValid(CraftingInventory clickedInventory) {
        ItemStack result = clickedInventory.getResult();
        boolean valid = result != null && !result.getType().equals(Material.AIR) && !MATCHER_LOADING.matches(result);
        return valid;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        List<ItemStack> toReturn = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ItemStack item = inventory.getItem(matrixCoordinate.access(i, j));
                toReturn.add(item);
            }
        }
        Utils.returnItems(player, toReturn);
    }
}
