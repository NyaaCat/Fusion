package cat.nyaa.fusion.util;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.I18n;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class Utils {
    private static TaskChainFactory factory;

    public static TaskChain<?> newChain(){
        if (factory == null) {
            factory = BukkitTaskChainFactory.create(FusionPlugin.plugin);
        }
        return factory.newChain();
    }

    public static ItemStack getFakeItem(ItemStack itemStack) {
        ItemStack fakeItem = new ItemStack(itemStack.getType());
        ItemMeta itemMeta = itemStack.getItemMeta();
        ItemMeta fakeMeta = fakeItem.getItemMeta();
        if (itemMeta == null){
            fakeItem = new ItemStack(Material.AIR);
        }
        List<String> lore = itemMeta.getLore();
        lore.add(I18n.format("fake_item_lore"));
        fakeMeta.setDisplayName(itemMeta.getDisplayName());
        fakeMeta.setLore(lore);
        fakeMeta.setCustomModelData(itemMeta.getCustomModelData());
        Map<Enchantment, Integer> enchants = itemMeta.getEnchants();
        if (!enchants.isEmpty()) {
            enchants.forEach((enchantment, integer) -> {
                fakeMeta.addEnchant(enchantment, integer, true);
            });
        }
        fakeItem.setItemMeta(fakeMeta);
        return fakeItem;
    }
}
