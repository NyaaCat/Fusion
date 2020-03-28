package cat.nyaa.fusion.ui.buttons;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.nyaacore.configuration.ISerializable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class GUIButton implements ISerializable {
    ItemStack itemStack;

    public abstract String getTitle();

    public abstract List<String> getLores();

    public abstract String getAction();

    public abstract void doAction(IQueryUiAccess iQueryUiAccess);

}
