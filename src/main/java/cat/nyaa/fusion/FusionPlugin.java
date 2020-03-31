package cat.nyaa.fusion;

import cat.nyaa.fusion.config.ConfigMain;
import cat.nyaa.fusion.inst.RecipeManager;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {
    public static FusionPlugin plugin = null;

    ConfigMain configMain;
    I18n i18n;
    DebugCommand debugCommand;
    AdminCommands adminCommands;
    Events events;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        configMain = new ConfigMain();
        configMain.load();
        i18n = new I18n(configMain.language);
        debugCommand = new DebugCommand(this, i18n);
        adminCommands = new AdminCommands(this, i18n);
        events = new Events(this);
        getServer().getPluginCommand("fusion_debug").setExecutor(debugCommand);
        getServer().getPluginCommand("fusion").setExecutor(adminCommands);
        getServer().getPluginManager().registerEvents(debugCommand, this);
        getServer().getPluginManager().registerEvents(events, this);
        RecipeManager.getInstance();
    }
}
