package cat.nyaa.fusion;

import cat.nyaa.fusion.config.ConfigMain;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {
    public static FusionPlugin plugin = null;

    ConfigMain configMain;
    I18n i18n;
    DebugCommand debugCommand;
    AdminCommands adminCommands;
    UserCommand userCommand;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
        configMain = new ConfigMain();
        configMain.load();
        i18n = new I18n(configMain.language);
        debugCommand = new DebugCommand(this, i18n);
        adminCommands = new AdminCommands(this, i18n);
        userCommand = new UserCommand(this, i18n);
        getServer().getPluginCommand("fusion_debug").setExecutor(debugCommand);
        getServer().getPluginCommand("fusion").setExecutor(adminCommands);
        getServer().getPluginCommand("fusion").setExecutor(debugCommand);
        getServer().getPluginManager().registerEvents(debugCommand, this);
    }
}
