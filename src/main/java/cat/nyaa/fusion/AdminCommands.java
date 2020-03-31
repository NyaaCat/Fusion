package cat.nyaa.fusion;

import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.inst.recipe.BaseRecipe;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

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
    }

    @SubCommand(value = "addRecipe", permission = "fusion.admin")
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
    }

    private ItemStack getItemAt(int row, int col, Inventory inventory){
        return inventory.getItem(row*9 + col);
    }
}
