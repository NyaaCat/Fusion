package cat.nyaa.fusion;

import cat.nyaa.nyaacore.LanguageRepository;
import org.bukkit.plugin.Plugin;

public class I18n extends LanguageRepository {
    private static I18n INSTANCE;
    private String language;

    public I18n(String language) {
        super();
        INSTANCE = this;
        this.language = language;
    }

    public void setLanguage(String language){
        this.language = language;
        load();
    }

    public static String format(String template, Object ... args){
        return INSTANCE.getFormatted(template, args);
    }

    @Override
    protected Plugin getPlugin() {
        return FusionPlugin.plugin;
    }

    @Override
    protected String getLanguage() {
        return language;
    }
}
