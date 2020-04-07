package cat.nyaa.fusion.ui.buttons.impl;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ButtonNextPage extends GUIButton {
    @Serializable
    String title = "&aNextPage";

    @Serializable
    List<String> lores = new ArrayList<>();

    @Serializable
    Material material = Material.ARROW;

    {
        lores.add("click to jump to next page");
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public List<String> getLores() {
        return lores;
    }

    @Override
    public String getAction() {
        return "nextPage";
    }

    @Override
    public void doAction(IQueryUiAccess iQueryUiAccess) {
        int currentPage = iQueryUiAccess.getCurrentPage();
        int totalPages = getTotalPages(iQueryUiAccess);
        int nextPage = currentPage+1;

        if (currentPage == totalPages - 1){
            nextPage = 0;
        }

        iQueryUiAccess.setPage(nextPage);
        iQueryUiAccess.refreshUi();
    }

    @Override
    public ItemStack getModel() {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',title));
        List<String> collect = lores.stream().map(s -> ChatColor.translateAlternateColorCodes('&', s)).collect(Collectors.toList());
        itemMeta.setLore(collect);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
