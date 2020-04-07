package cat.nyaa.fusion.ui.impl;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.BaseUi;
import cat.nyaa.fusion.ui.MatrixCoordinate;
import cat.nyaa.fusion.ui.UiCoordinate;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.ui.buttons.ButtonRegister;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListRecipeAccess extends BaseUi {
    private final int rows = 3;
    private final int cols = 5;

    private final Inventory inventory;

    public ListRecipeAccess(List<Integer> sectionIndexes, Inventory inventory, List<IRecipe> recipe){
        super(sectionIndexes, inventory);
        this.inventory = inventory;
        this.recipes = recipe;
    }

    @Override
    public void setItemAt(int index, ItemStack itemStack) {
        setItemAt(index / 5, index % 5, itemStack);
    }

    @Override
    public void setItemAt(int row, int col, ItemStack itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, Utils.getFakeItem(itemStack));
    }

    @Override
    public ItemStack getItemAt(int row, int col) {
        return inventory.getItem(matrixCoordinate.access(row, col));
    }

    @Override
    public ItemStack getItemAt(int index) {
        return getItemAt(index / 5, index % 5);
    }

    @Override
    public boolean isContentClicked(int rawSlot) {
        if (rawSlot == 9) return true;
        int i = matrixCoordinate.indexOf(rawSlot);
        return i >= 0 && i < recipes.size() ;
    }

    @Override
    protected void initSlots(MatrixCoordinate matrixCoordinate) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                UiCoordinate e = new UiCoordinate(i, j);
                slots.add(e);
                validClicks.add(e.access(matrixCoordinate));
            }
        }
    }

    @Override
    protected void initButton() {
        ButtonRegister instance = ButtonRegister.getInstance();
        setButtonAt(26, instance.NEXT_PAGE);
        setButtonAt(18, instance.PREVIOUS_PAGE);
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        ItemStack item = inventory.getItem(9);
        if (item != null){
            Utils.returnItems(event.getPlayer(), Collections.singletonList(item));
        }
    }

    @Override
    public void refreshUi() {
        if (recipes == null)return;
        List<ItemStack> collect = recipes.stream()
                .skip(getPageSize() * getCurrentPage())
                .limit(getPageSize())
                .map(IRecipe::getResultItem)
                .collect(Collectors.toList());
        setContent(collect);
    }

    @Override
    public int getPageSize() {
        return 3*5;
    }

    @Override
    public void onContentInteract(InventoryInteractEvent event) {
        event.setCancelled(true);
        if (event instanceof InventoryClickEvent){
            int rawSlot = ((InventoryClickEvent) event).getRawSlot();
            if (rawSlot == 9){
                event.setCancelled(false);
                Utils.newChain().delay(1).sync(()->{
                    ItemStack item = inventory.getItem(rawSlot);
                    if (item == null || item.getType().isAir()){
                        this.recipes = RecipeManager.getInstance().getRecipes();
                        refreshUi();
                        return;
                    }
                    RecipeManager.createQuery(RecipeManager.getItem(item.clone()), recipes1 -> {
                        this.recipes = recipes1;
                        page = 0;
                        refreshUi();
                    });
                }).execute();
                return;
            }
            int i = matrixCoordinate.indexOf(rawSlot);
            if (i == -1){
                return;
            }
            IRecipe iRecipe = recipes.stream()
                    .skip(getPageSize() * getCurrentPage())
                    .skip(i)
                    .findFirst().orElse(null);
            if (iRecipe == null){
                return;
            }
            HumanEntity whoClicked = event.getWhoClicked();

            DetailRecipeAccess detailRecipeAccess = UiManager.newDetailRecipeAccess((Player) whoClicked, iRecipe);
            whoClicked.closeInventory();
            Utils.newChain().sync(()->{
                whoClicked.openInventory(detailRecipeAccess.getInventory());
            }).execute();
        }
    }

    @Override
    public void onResultInteract(InventoryInteractEvent event, ItemStack itemStack) {
        event.setCancelled(true);
    }

    @Override
    public boolean isResultClicked(int rawSlot) {
        return false;
    }
}
