package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.ui.InfoUi;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import cat.nyaa.fusion.ui.buttons.ButtonRegister;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class DetailRecipeAccess extends InfoUi {
    private IRecipe recipe;
    private ItemStack queryItem = null;

    public DetailRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, List<IRecipe> recipes, int recipeIndex){
        super(sectionIndexes, inventory);
        this.recipes = recipes;
        this.recipe = recipes.get(recipeIndex);
        this.setPage(recipeIndex);
        setContent(recipe.getRawRecipe());
        setResultItem(recipe.getResultItem());
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
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    protected void initButton() {
        ButtonRegister instance = ButtonRegister.getInstance();
        setButtonAt(26, instance.NEXT_PAGE);
        setButtonAt(18, instance.PREVIOUS_PAGE);
        setButtonAt(8, instance.BACK);
    }

    @Override
    public void setItemAt(int row, int col, ItemStack itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, Utils.getFakeItem(itemStack));
    }

    @Override
    protected void initSlots(MatrixCoordinate matrixCoordinate) {
        super.initSlots(matrixCoordinate);
    }

    @Override
    public void setItemAt(int index, ItemStack itemStack) {
        setItemAt(index / 3, index % 3, itemStack);
    }

    @Override
    public ItemStack getItemAt(int row, int col) {
        int actualIndex = matrixCoordinate.access(row, col);
        ItemStack item = inventory.getItem(actualIndex);
        return item;
    }

    @Override
    public ItemStack getItemAt(int index) {
        return getItemAt(index / 3, index % 3);
    }

    @Override
    public boolean isContentClicked(int rawSlot) {
        return validClicks.contains(rawSlot);
    }

    @Override
    public void refreshUi() {
        if (recipes == null || recipes.size() == 0)return;
        if (getCurrentPage()>recipes.size()){
            setPage(0);
        }
        recipe = recipes.get(getCurrentPage());
        List<ItemStack> rawRecipe = recipe.getRawRecipe();
        setContent(rawRecipe);
        setResultItem(recipe.getResultItem());
        super.refreshUi();
    }

    @Override
    public boolean hasQuery() {
        return queryItem != null;
    }

    @Override
    public void setQueryItem(ItemStack itemStack){
        queryItem = itemStack;
    }

    @Override
    public ItemStack getQueryItem() {
        return queryItem;
    }

    @Override
    public void setResultItem(ItemStack item) {
        inventory.setItem(resultSlot.access(matrixCoordinate), Utils.getFakeItem(item));
    }


    @Override
    public int getPageSize() {
        return 1;
    }

    @Override
    public void onContentInteract(InventoryInteractEvent event) {
        event.setCancelled(true);

        if (event instanceof InventoryClickEvent){
            checkClone(event);
        }
    }

    private void checkClone(InventoryInteractEvent event) {
        if (!event.getWhoClicked().isOp()){
            return;
        }
        boolean isClone = ((InventoryClickEvent) event).getClick().isCreativeAction();
        boolean isRightClick = ((InventoryClickEvent) event).getClick().isRightClick();
        boolean valid = isClone || isRightClick;
        if (valid){
            ItemStack cursor = ((InventoryClickEvent) event).getCursor();
            int index = validClicks.indexOf(((InventoryClickEvent) event).getSlot());
            ItemStack itemAt = null;
            if (index != -1){
                itemAt = recipes.get(getCurrentPage()).getRawRecipe().get(index);
            }else {
                if (((InventoryClickEvent) event).getSlot() == resultSlot.access(matrixCoordinate)) {
                    itemAt = recipe.getResultItem();
                }
            }
            if (itemAt == null || itemAt.getType().isAir()){
                return;
            }
            if (cursor == null || cursor.getType().isAir() || cursor.isSimilar(itemAt)) {
                if (recipes.size() < getCurrentPage()) {
                    return;
                }

                ItemStack finalItemAt = itemAt;
                Utils.newChain().delay(1)
                        .sync(() -> {
                            InventoryView view = event.getView();
                            ItemStack cursor1 = view.getCursor();
                            boolean similar = false;
                            if (cursor1 == null || cursor1.getType().isAir() || (similar = cursor1.isSimilar(finalItemAt))) {
                                ItemStack clone = finalItemAt.clone();
                                if (similar){
                                    clone.setAmount(Math.min(cursor1.getMaxStackSize(), cursor1.getAmount()+1));
                                }else {
                                    if (isClone){
                                        clone.setAmount(clone.getMaxStackSize());
                                    }
                                }
                                view.setCursor(clone);
                            }
                        }).execute();
            }
        }
    }


    @Override
    public void onResultInteract(InventoryInteractEvent event, ItemStack itemStack) {
        event.setCancelled(true);

        if (event instanceof InventoryClickEvent){
            checkClone(event);
        }
    }

    @Override
    public boolean isResultClicked(int rawSlot) {
        return rawSlot == resultSlot.access(matrixCoordinate);
    }
}
