package cat.nyaa.fusion.util;

import cat.nyaa.fusion.FusionPlugin;
import cat.nyaa.fusion.I18n;
import cat.nyaa.nyaacore.utils.InventoryUtils;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static TaskChainFactory factory;
    private static final String KEY_MODEL = "fusion_model";
    private static final NamespacedKey NAMESPACED_KEY_MODEL = new NamespacedKey(FusionPlugin.plugin, KEY_MODEL);

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
        fakeItem.setAmount(itemStack.getAmount());
        List<String> lore = itemMeta.getLore();
        if (lore == null){
            lore = new ArrayList<>();
        }
        lore.add(I18n.format("fake_item_lore"));
        if (fakeMeta == null) return null;
        String displayName = itemMeta.getDisplayName();
        if (!displayName.equals("")){
            fakeMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r"+ displayName));
        }
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
        markSample(fakeMeta);
        fakeItem.setItemMeta(fakeMeta);
        return fakeItem;
    }

    public static void markSample(ItemMeta fakeMeta) {
        fakeMeta.getPersistentDataContainer().set(NAMESPACED_KEY_MODEL, PersistentDataType.INTEGER, 1);
    }

    public static void markSample(ItemStack itemStack){
        if (itemStack == null || itemStack.getItemMeta() == null)return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        markSample(itemMeta);
        itemStack.setItemMeta(itemMeta);
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

    public static boolean isFakeItem(ItemStack currentItem) {
        if (currentItem == null){
            return false;
        }
        ItemMeta itemMeta = currentItem.getItemMeta();
        return itemMeta != null && itemMeta.getPersistentDataContainer().has(NAMESPACED_KEY_MODEL, PersistentDataType.INTEGER);
    }
}
