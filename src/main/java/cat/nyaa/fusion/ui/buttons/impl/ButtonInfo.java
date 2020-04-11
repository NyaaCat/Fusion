package cat.nyaa.fusion.ui.buttons.impl;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.InfoUi;
import cat.nyaa.fusion.ui.buttons.GUIButton;
import cat.nyaa.fusion.ui.impl.DetailRecipeAccess;
import cat.nyaa.fusion.ui.impl.ListRecipeAccess;
import cat.nyaa.fusion.util.Utils;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cat.nyaa.fusion.util.Utils.colored;

public class ButtonInfo extends GUIButton {
    IQueryUiAccess iQueryUiAccess;
    @Serializable
    Material material = Material.DARK_OAK_SIGN;

    @Serializable
    String title = "&aInfo";

    @Serializable(name = "default")
    List<String> default_ = new ArrayList<>();

    @Serializable
    List<String> loreQueryMaterial = new ArrayList<>();

    @Serializable
    List<String> loreQueryResult = new ArrayList<>();

    @Serializable
    List<String> loreDetail = new ArrayList<>();

    {
        default_.add(colored("&ePlace an item to query recipes"));
        default_.add(colored("{currentPage} / {totalPage}"));
        loreQueryMaterial.add(colored("&cDisplaying usage of &r{itemName}&c By Material, total {totalPage}"));
        loreQueryMaterial.add(colored("&aClick to change query mode to Result"));
        loreQueryMaterial.add(colored("&ePlace an item to query recipes."));
        loreQueryResult.add(colored("&cDisplaying usage of &r{itemName}&c By Result, total {totalPage}"));
        loreQueryResult.add(colored("&aClick to change query mode to Material"));
        loreQueryResult.add(colored("&ePlace an item to query recipes."));
        loreDetail.add(colored("&cDisplaying usage of &r{itemName}&c By Result, {currentPage} / {totalPage}"));
    }

    @Override
    public String getAction() {
        return null;
    }

    @Override
    public void doAction(InventoryInteractEvent event, IQueryUiAccess iQueryUiAccess) {
        if (iQueryUiAccess instanceof ListRecipeAccess) {
            ((ListRecipeAccess)iQueryUiAccess).toggleQueryMode();
            Utils.newChain().delay(1)
                    .sync(() ->{
                        ((ListRecipeAccess)iQueryUiAccess).refreshQuery();
                    })
                    .execute();
        }
    }

    @Override
    public ItemStack getModel(IQueryUiAccess iQueryUiAccess) {
        this.iQueryUiAccess = iQueryUiAccess;
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)return itemStack;
        itemMeta.setDisplayName(colored(title));
        if (iQueryUiAccess instanceof ListRecipeAccess){
            ListRecipeAccess listRecipeAccess = (ListRecipeAccess) iQueryUiAccess;
            if (listRecipeAccess.hasQuery()) {
                switch (listRecipeAccess.getQueryMode()) {
                    case MATERIAL:
                        itemMeta.setLore(getLore(loreQueryMaterial, listRecipeAccess));
                        break;
                    case RESULT:
                        itemMeta.setLore(getLore(loreQueryResult, listRecipeAccess));
                        break;
                    default:
                        itemMeta.setLore(getLore(default_, listRecipeAccess));
                }
            }else {
                itemMeta.setLore(getLore(default_, listRecipeAccess));
            }
        }else if (iQueryUiAccess instanceof DetailRecipeAccess){
            DetailRecipeAccess detailRecipeAccess = (DetailRecipeAccess) iQueryUiAccess;
            if (detailRecipeAccess.hasQuery()){
                itemMeta.setLore(getLore(loreDetail, detailRecipeAccess));
            }
            else itemMeta.setLore(getLore(default_, detailRecipeAccess));
        }else {
            itemMeta.setLore(getLore(default_, "", String.valueOf(getTotalPages(iQueryUiAccess)), String.valueOf(iQueryUiAccess.getCurrentPage()+1)));
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private List<String> getLore(List<String> lore, InfoUi listRecipeAccess) {
        ItemStack queryItem = listRecipeAccess.getQueryItem();
        String itemName = listRecipeAccess.hasQuery()? getItemName(queryItem):"";
        String totalPage = String.valueOf(getTotalPages(listRecipeAccess));
        String currentPage = String.valueOf(listRecipeAccess.getCurrentPage()+1);

        return getLore(lore, itemName, totalPage, currentPage);
    }

    private List<String> getLore(List<String> lore, String itemName, String totalPage, String currentPage) {
        return lore.stream().map(s -> {
            s = s.replaceAll("\\{itemName}", itemName);
            s = s.replaceAll("\\{totalPage}", totalPage);
            s = s.replaceAll("\\{currentPage}", currentPage);
            return colored(s);
        }).collect(Collectors.toList());
    }

    private String getItemName(ItemStack queryItem) {
        ItemMeta itemMeta = queryItem.getItemMeta();
        String itemName = "";
        if (itemMeta == null){
            itemName = Material.AIR.name();
        }else {
            if (itemMeta.hasDisplayName()){
                itemName = itemMeta.getDisplayName();
            }else if (itemMeta.hasLocalizedName()){
                itemName = itemMeta.getLocalizedName();
            }else {
                itemName = queryItem.getType().name();
            }
        }
        return itemName;
    }
}
