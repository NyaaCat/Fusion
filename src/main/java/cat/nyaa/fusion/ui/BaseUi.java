package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.config.element.IElement;
import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static cat.nyaa.fusion.util.Utils.getGuiSection;

public abstract class BaseUi implements IQueryUiAccess, IRecipeGUIAccess, MatrixAccess, InteractiveUi, InventoryHolder {
    protected final MatrixCoordinate matrixCoordinate;
    protected final Inventory inventory;
    protected final UiCoordinate resultSlot;
    protected final List<UiCoordinate> slots;
    protected final List<Integer> validClicks;

    protected BaseUi(List<Integer> sectionIndexes, Inventory inventory){
        this.matrixCoordinate = new MatrixCoordinate(sectionIndexes, 3, 5);
        int initialCapacity = matrixCoordinate.rows() * matrixCoordinate.columns();
        slots = new ArrayList<>(initialCapacity);
        validClicks = new ArrayList<>(initialCapacity);
        initSlots(matrixCoordinate);
        resultSlot = new UiCoordinate(1, 4);
        this.inventory = inventory;
        addSplitters();
    }

    protected void initSlots(MatrixCoordinate matrixCoordinate) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                UiCoordinate e = new UiCoordinate(i, j);
                slots.add(e);
                validClicks.add(e.access(matrixCoordinate));
            }
        }
    }

    protected void addSplitters() {
        ItemStack blueSplitter = createSplitter(Material.BLUE_STAINED_GLASS_PANE);
        ItemStack lightBlueSplitter = createSplitter(Material.LIGHT_BLUE_STAINED_GLASS_PANE);
        for (int i = 0; i < 3; i++) {
            inventory.setItem(i*9+1, blueSplitter);
        }
        for (int i = 0; i < 3; i++) {
            inventory.setItem(i*9+7, blueSplitter);
        }
    }

    protected static ItemStack createSplitter(Material material) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @Override
    public void setContent(List<IElement> rawRecipe, IElement resultItem) {
        setContent(rawRecipe);
        setResultItem(resultItem);
    }

    @Override
    public void setContent(List<IElement> rawRecipe) {
        for (int i = 0; i < rawRecipe.size(); i++) {
            setItemAt(i, rawRecipe.get(i));
        }
    }

    @Override
    public List<IElement> getContent() {
        List<IElement> content = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            content.add(getItemAt(i));
        }
        return content;
    }

    @Override
    public void setResultItem(IElement item) {
        inventory.setItem(resultSlot.access(matrixCoordinate), item.getItemStack());
    }

    @Override
    public void setItemAt(int row, int col, IElement itemStack) {
        int actualIndex = matrixCoordinate.access(row, col);
        inventory.setItem(actualIndex, itemStack.getItemStack());
    }

    @Override
    public int size() {
        return matrixCoordinate.size();
    }

    @Override
    public int rows() {
        return matrixCoordinate.rows();
    }

    @Override
    public int columns() {
        return matrixCoordinate.columns();
    }

    public abstract void onInventoryClose(InventoryCloseEvent event);

    private static Map<Integer, GUIButton> buttons = new HashMap<>();
    private static IRecipeGUIAccess recipeGui;
    private static List<Integer> recipeSpace = getGuiSection(0, 2, 3, 5);
    private int page = 0;
    protected List<IRecipe> recipes = new ArrayList<>();

    @Override
    public void onButtonClicked(InventoryInteractEvent event, GUIButton button) {
        event.setCancelled(true);
        if (!(event instanceof InventoryClickEvent)){
            return;
        }
        button.doAction(this);
    }

    @Override
    public List<IRecipeGUIAccess> getRecipeGUIs() {
        return Collections.singletonList(recipeGui);
    }

    @Override
    public void setPage(int page) {
        this.page = page;
        refreshUi();
    }

    @Override
    public int getCurrentPage() {
        return page;
    }

    @Override
    public int getSize() {
        return recipes.size();
    }

    @Override
    public void setRecipes(List<IRecipe> recipes) {
        this.recipes = recipes;
        refreshUi();
    }

    @Override
    public GUIButton getButtonAt(int index) {
        return buttons.get(index);
    }

    @Override
    public void setButtonAt(int index, GUIButton button) {
        buttons.put(index, button);
    }

    private void setButtonAt(int row, int col, GUIButton button){
        setButtonAt(row*9+col, button);
    }

    public void onRawInteract(InventoryInteractEvent event){

    }

    public boolean isResultClick(int rawSlot){
        return resultSlot.access(matrixCoordinate) == rawSlot;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
