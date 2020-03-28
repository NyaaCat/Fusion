package cat.nyaa.fusion.config;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.nyaacore.configuration.PluginConfigure;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class ConfigMain extends PluginConfigure {
    @Serializable
    public String language = "en_US";

    @Serializable
    public boolean enabled = true;

    @Serializable(name = "gui.shortcut.enabled")
    public boolean guiBlockEnabled = true;

    @Serializable(name = "gui.shortcut.block")
    public Material guiBlock = Material.CRAFTING_TABLE;

    @Serializable(name = "gui.shortcut.enabled_world")
    public List<String> enabledWorld = new ArrayList<>();

    @Override
    protected JavaPlugin getPlugin() {
        return FusionPlugin.plugin;
    }
}
