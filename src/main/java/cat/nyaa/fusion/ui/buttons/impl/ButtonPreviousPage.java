package cat.nyaa.fusion.ui.buttons.impl;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonPreviousPage extends GUIButton {
    @Serializable
    String title = "&aPreviousPage";

    @Serializable
    List<String> lores = new ArrayList<>();

    @Serializable
    Material material = Material.ARROW;

    {
        lores.add("click to jump to previous page");
    }

    @Override
    public String getAction() {
        return "previousPage";
    }

    @Override
    public void doAction(InventoryInteractEvent event, IQueryUiAccess iQueryUiAccess) {
        int currentPage = iQueryUiAccess.getCurrentPage();
        int totalPages = getTotalPages(iQueryUiAccess);
        int nextPage = currentPage - 1;

        if (currentPage == 0){
            nextPage = totalPages - 1;
        }
        
        if (nextPage < 0 || nextPage >= totalPages){
            nextPage = 0;
        }

        iQueryUiAccess.setPage(nextPage);
        iQueryUiAccess.refreshUi();
    }

    @Override
    public ItemStack getModel(IQueryUiAccess iQueryUiAccess) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',title));
        List<String> collect = lores.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        itemMeta.setLore(collect);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
