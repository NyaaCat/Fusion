package cat.nyaa.fusion;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.BaseUi;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.fusion.ui.impl.DetailRecipeAccess;
import cat.nyaa.fusion.ui.impl.ListRecipeAccess;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DebugCommand extends CommandReceiver implements Listener {
    /**
     * @param plugin for logging purpose only
     * @param _i18n
     */
    public DebugCommand(Plugin plugin, ILocalizer _i18n) {
        super(plugin, _i18n);
    }

    private Set<UUID> tofill = new HashSet<>();
    private Set<UUID> tofillRaw = new HashSet<>();
    @EventHandler
    public void fillInv(InventoryOpenEvent event){
        if (!tofill.contains(event.getPlayer().getUniqueId())){
            return;
        }
        if (event.getInventory().getType().equals(InventoryType.WORKBENCH)){
            Inventory inventory = event.getInventory();
            for (int i = 0; i < inventory.getSize(); i++) {
                ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(String.valueOf(i));
                itemStack.setItemMeta(itemMeta);
                inventory.setItem(i, itemStack);
            }
        }
        tofill.remove(event.getPlayer().getUniqueId());
    }
    @EventHandler
    public void fillRaw(InventoryOpenEvent event){
        if (!tofillRaw.contains(event.getPlayer().getUniqueId())) {
            return;
        }
        try{
            InventoryView view = event.getView();
            int i = view.countSlots();
            for (int j = 0; j < i; j++) {
                Integer integer = j;
                ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(String.valueOf(j));
                itemStack.setItemMeta(itemMeta);
                view.setItem(j, itemStack);
            }
        }finally {
            tofillRaw.remove(event.getPlayer().getUniqueId());
        }
    }

    private void fillInv(Inventory inventory, List<Integer> indexes, String title) {
        for (int i = 0; i < indexes.size(); i++) {
            Integer integer = indexes.get(i);
            ItemStack itemStack = new ItemStack(Material.GOLD_INGOT);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(title + i);
            itemStack.setItemMeta(itemMeta);
            inventory.setItem(integer, itemStack);
        }
    }

    @SubCommand("testCraftingTable")
    public void testCraftingTable(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        CraftingTableAccess craftingTableAccess = UiManager.newCraftingSession(player);
        player.openInventory(craftingTableAccess.getInventory());
    }

    @SubCommand("testListAccess")
    public void testListSession(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        ListRecipeAccess craftingTableAccess = UiManager.newListRecipeAccess(player);
        player.openInventory(craftingTableAccess.getInventory());
    }

    @SubCommand("testDetailAccess")
    public void testDetailSession(CommandSender sender, Arguments arguments) {
        Player player = asPlayer(sender);
        IRecipe recipe = RecipeManager.getInstance().getRecipes().get(0);
        DetailRecipeAccess craftingTableAccess = UiManager.newDetailRecipeAccess(player, recipe);
        player.openInventory(craftingTableAccess.getInventory());
    }

    private void fillContent(BaseUi access){
        List<ItemStack> content = new ArrayList<>();
        for (int i = 0; i < access.size(); i++) {
            ItemStack sample = new ItemStack(Material.DIAMOND, 1);
            ItemMeta itemMeta = sample.getItemMeta();
            itemMeta.setDisplayName(String.valueOf(i));
            sample.setItemMeta(itemMeta);
            content.add(sample);
        }
            access.setContent(content);
    }

    @SubCommand("fillInv")
    public void fillInv(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        tofill.add(player.getUniqueId());
    }

    @SubCommand("inspectInv")
    public void inspectInventoryLoc(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        PlayerInventory inventory = player.getInventory();
        World world = player.getWorld();
        for (int i = 0; i < 41; i++) {
            ItemStack sample = new ItemStack(Material.DIAMOND, 1);
            ItemMeta itemMeta = sample.getItemMeta();
            itemMeta.setDisplayName(String.valueOf(i));
            sample.setItemMeta(itemMeta);
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        world.dropItemNaturally(player.getLocation(), item);
                    }
                }.runTaskLater(FusionPlugin.plugin, i * 2);
            }
            inventory.setItem(i, sample);
        }
    }

    @SubCommand("fillRaw")
    public void fillRawSlot(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        tofillRaw.add(player.getUniqueId());
    }

    @Override
    public String getHelpPrefix() {
        return null;
    }
}
