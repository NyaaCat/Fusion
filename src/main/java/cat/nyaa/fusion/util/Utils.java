package cat.nyaa.fusion.util;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.I18n;
import cat.nyaa.nyaacore.utils.InventoryUtils;
import cat.nyaa.nyaacore.utils.ItemTagUtils;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

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
            return fakeItem;
        }
        List<String> lore = itemMeta.getLore();
        if (lore == null){
            lore = new ArrayList<>();
        }
        lore.add(I18n.format("fake_item_lore"));
        fakeMeta.setDisplayName(itemMeta.getDisplayName());
        fakeMeta.setLore(lore);
        if (itemMeta.hasCustomModelData()){
            fakeMeta.setCustomModelData(itemMeta.getCustomModelData());
        }
        Map<Enchantment, Integer> enchants = itemMeta.getEnchants();
        if (!enchants.isEmpty()) {
            enchants.forEach((enchantment, integer) -> {
                fakeMeta.addEnchant(enchantment, integer, true);
            });
        }
        fakeItem.setItemMeta(fakeMeta);
        try {
            ItemTagUtils.setInt(fakeItem, "fusion_model", 1);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "exception creating model");
            e.printStackTrace();
        }
        return fakeItem;
    }

    public static List<Integer> getGuiSection(int row, int index, int rows, int cols) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                indexes.add((i + row) * 9 + index + j);
            }
        }
        return indexes;
    }

    public static void returnItems(HumanEntity player, List<ItemStack> toReturn) {
        toReturn.forEach(itemStack -> {
            if (itemStack != null) {
                if (!InventoryUtils.addItem(player.getInventory(), itemStack)) {
                    Location location = player.getLocation();
                    World world = player.getWorld();
                    world.dropItem(location, itemStack);
                }
            }
        });

    }
}
