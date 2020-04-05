package cat.nyaa.fusion.fuser;

import cat.nyaa.fusion.config.recipe.IRecipe;
import cat.nyaa.fusion.inst.RecipeManager;
import cat.nyaa.fusion.ui.IRecipeGUIAccess;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BukkitFuser implements Fuser {
    private static BukkitFuser fuser;

    public static BukkitFuser getInstance(){
        if (fuser == null){
            synchronized (BukkitFuser.class){
                if (fuser == null){
                    fuser = new BukkitFuser();
                }
            }
        }
        return fuser;
    }

    private BukkitFuser(){

    }


    @Override
    public IRecipe fuseItem(IRecipeGUIAccess guiAccess) {
        List<ItemStack> content = guiAccess.getContent();
        return RecipeManager.getInstance().getRecipe(content);
    }
}
