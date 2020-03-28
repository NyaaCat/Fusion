package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.ui.impl.CraftingTableAccess;
import cat.nyaa.fusion.ui.impl.InspectSessionAccess;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UiManager {
    public static UiManager INSTANCE;

    private UiManager(){

    }

    public static UiManager getInstance(){
        if (INSTANCE == null){
            synchronized (UiManager.class){
                if (INSTANCE == null){
                    INSTANCE = new UiManager();
                }
            }
        }
        return INSTANCE;
    }

    private Map<UUID, CraftingTableAccess> craftingSession = new HashMap<>();
    private Map<UUID, IRecipeGUIAccess> querySession = new HashMap<>();

}
