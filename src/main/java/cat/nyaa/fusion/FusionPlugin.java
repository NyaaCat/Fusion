package cat.nyaa.fusion;

import cat.nyaa.fusion.config.ConfigMain;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.ui.buttons.ButtonRegister;
import cat.nyaa.fusion.util.Utils;
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
        onReload();
        debugCommand = new DebugCommand(this, i18n);
        adminCommands = new AdminCommands(this, i18n);
        events = new Events(this);
//        getServer().getPluginCommand("fusion_debug").setExecutor(debugCommand);
        getServer().getPluginCommand("fusion").setExecutor(adminCommands);
        getServer().getPluginManager().registerEvents(debugCommand, this);
        getServer().getPluginManager().registerEvents(events, this);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        UiManager.closeAllInventory();
    }

    public void onReload(){
        configMain = new ConfigMain();
        configMain.load();
        i18n = new I18n(configMain.language);
        i18n.load();
        ButtonRegister.getInstance().load();
        RecipeManager instance = RecipeManager.getInstance();
        Utils.newChain().delay(1)
                .sync(instance::loadFromDir)
                .execute();
        UiManager.getInstance();
    }
}
