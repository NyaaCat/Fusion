package cat.nyaa.fusion;

import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.nyaacore.ILocalizer;
import cat.nyaa.nyaacore.cmdreceiver.Arguments;
import cat.nyaa.nyaacore.cmdreceiver.CommandReceiver;
import cat.nyaa.nyaacore.cmdreceiver.SubCommand;
import org.bukkit.command.CommandSender;
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
}
