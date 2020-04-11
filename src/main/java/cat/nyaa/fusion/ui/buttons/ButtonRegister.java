package cat.nyaa.fusion.ui.buttons;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.ui.buttons.impl.ButtonBack;
import cat.nyaa.fusion.ui.buttons.impl.ButtonInfo;
import cat.nyaa.fusion.ui.buttons.impl.ButtonNextPage;
import cat.nyaa.fusion.ui.buttons.impl.ButtonPreviousPage;
import cat.nyaa.nyaacore.configuration.FileConfigure;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.plugin.java.JavaPlugin;

public class ButtonRegister extends FileConfigure implements ISerializable {
    private static ButtonRegister INSTANCE;
    @Serializable
    public ButtonNextPage NEXT_PAGE = new ButtonNextPage();

    @Serializable
    public ButtonPreviousPage PREVIOUS_PAGE = new ButtonPreviousPage();

    @Serializable
    public ButtonInfo INFO = new ButtonInfo();

    @Serializable
    public ButtonBack BACK = new ButtonBack();



    private ButtonRegister(){}

    @Override
    protected String getFileName() {
        return "buttons.yml";
    }

    @Override
    protected JavaPlugin getPlugin() {
        return FusionPlugin.plugin;
    }

    public static ButtonRegister getInstance(){
        if (INSTANCE == null){
            synchronized (ButtonRegister.class){
                if (INSTANCE ==null){
                    INSTANCE = new ButtonRegister();
                }
            }
        }
        return INSTANCE;
    }
}
