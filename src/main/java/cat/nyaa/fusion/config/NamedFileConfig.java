package cat.nyaa.fusion.config;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public abstract class NamedFileConfig extends FileConfigure {
    public String fileName = "";
    protected String prefix = "";
    private File file;

    public NamedFileConfig(String name){
        this.fileName = name;
    }

    public String getConfigName() {
        return fileName;
    }

    public String getPrefix() {
        return prefix;
    }

    public File getFile(){
        if (file == null){
            synchronized (this){
                if (file == null){
                    file = new File(getPlugin().getDataFolder(), getFileName());
                }
            }
        }
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    protected String getFileName() {
        StringBuilder stringBuilder = new StringBuilder();
        if(file != null){
            return stringBuilder.append(getFileDirName())
                    .append(File.separatorChar)
                    .append(file.getName())
                    .toString();
        }
        return stringBuilder.append(getFileDirName())
                .append(File.separatorChar)
                .append(getConfigName())
                .append(".yml").toString();
    }

    protected abstract String getFileDirName();

    @Override
    protected JavaPlugin getPlugin() {
        return FusionPlugin.plugin;
    }
}
