package cat.nyaa.fusion.config;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class NamedFileConfig extends FileConfigure {
    public String fileName = "";
    protected String prefix = "";

    public NamedFileConfig(String name){
        this.fileName = name;
    }

    public String getConfigName() {
        return fileName;
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    protected String getFileName() {
        return getFileDirName().concat("/")
                .concat(getConfigName())
                .concat(".yml");
    }

    protected abstract String getFileDirName();

    @Override
    protected JavaPlugin getPlugin() {
        return FusionPlugin.plugin;
    }
}
