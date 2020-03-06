package cat.nyaa.fusion;

import cat.nyaa.fusion.config.ConfigMain;
import org.bukkit.plugin.java.JavaPlugin;

public class FusionPlugin extends JavaPlugin {
    public static FusionPlugin plugin = null;

    ConfigMain configMain;

    @Override
    public void onEnable() {
        super.onEnable();
        plugin = this;
    }
}
