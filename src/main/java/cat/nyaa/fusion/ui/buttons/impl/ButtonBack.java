package cat.nyaa.fusion.ui.buttons.impl;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.UiManager;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonBack extends GUIButton {
    @Serializable
    String title = "&aBack";
    @Serializable
    List<String> lores = new ArrayList<>();
    @Serializable
    Material material = Material.BARRIER;
    {
        lores.add(Utils.colored("&rBack to former level"));
    }

    @Override
    public String getAction() {
        return "back";
    }

    @Override
    public void doAction(InventoryInteractEvent event, IQueryUiAccess iQueryUiAccess) {
        HumanEntity whoClicked = event.getWhoClicked();
        whoClicked.closeInventory();
        UiManager.onUiBack(whoClicked);
    }

    @Override
    public ItemStack getModel(IQueryUiAccess iQueryUiAccess) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)return itemStack;
        itemMeta.setDisplayName(Utils.colored(title));
        itemMeta.setLore(lores.stream().map(Utils::colored).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
