package cat.nyaa.fusion;

import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.inst.recipe.BaseRecipe;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.fusion.ui.impl.ListRecipeAccess;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.Message;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AdminCommands extends CommandReceiver {

    private final Plugin plugin;
    private final ILocalizer i18n;

    /**
     * @param plugin for logging purpose only
     * @param _i18n
     */
    public AdminCommands(Plugin plugin, ILocalizer _i18n) {
        super(plugin, _i18n);
        this.plugin = plugin;
        i18n = _i18n;
    }

    @Override
    public String getHelpPrefix() {
        return "fusion";
    }

    @SubCommand(value = "craft", permission = "fusion.user")
    public void onCraft(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        CraftingTableAccess craftingTableAccess = UiManager.newCraftingSession(player);
        player.openInventory(craftingTableAccess.getInventory());
    }

    @SubCommand(value = "list", permission = "fusion.user")
    public void onList(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        ListRecipeAccess craftingTableAccess = UiManager.newListRecipeAccess(player);
        player.openInventory(craftingTableAccess.getInventory());
    }

    @SubCommand(value = "reload", permission = "fusion.user")
    public void onReload(CommandSender sender, Arguments arguments){
        FusionPlugin.plugin.onReload();
        new Message(I18n.format("reload.complete")).send(sender);
    }

    @SubCommand(value = "addRecipe", permission = "fusion.admin", tabCompleter = "addRecipeCompleter")
    public void onAddRecipe(CommandSender sender, Arguments arguments){
        Player player = asPlayer(sender);
        String name = arguments.nextString();

        PlayerInventory inventory = player.getInventory();
        BaseRecipe recipe = new BaseRecipe(name);
        for (int i = 1; i < 4; i++) {
            for (int j = 0; j < 3; j++) {
                ItemStack itemAt = getItemAt(i, j, inventory);
                recipe.addElement(RecipeManager.getItem(itemAt));
            }
        }
        ItemStack itemAt = getItemAt(2, 3, inventory);
        recipe.setResult(RecipeManager.getItem(itemAt));
        RecipeManager.getInstance().add(name, recipe);
        new Message(I18n.format("recipe.add.success", name)).send(sender);
    }

    public List<String> addRecipeCompleter(CommandSender sender, Arguments arguments) {
        List<String> completeStr = new ArrayList<>();
        switch (arguments.remains()) {
            case 1:
                completeStr.add("<name>");
                break;
        }
        return filtered(arguments, completeStr);
    }

    public List<String> sampleCompleter(CommandSender sender, Arguments arguments) {
        List<String> completeStr = new ArrayList<>();
        switch (arguments.remains()) {
            case 1:
                break;
        }
        return filtered(arguments, completeStr);
    }

    private static List<String> filtered(Arguments arguments, List<String> completeStr) {
        String next = arguments.at(arguments.length() - 1);
        return completeStr.stream().filter(s -> s.startsWith(next)).collect(Collectors.toList());
    }



    private ItemStack getItemAt(int row, int col, Inventory inventory){
        return inventory.getItem(row*9 + col);
    }
}
