package cat.nyaa.fusion.ui;

import cat.nyaa.fusion.config.QueryMode;
import cat.nyaa.fusion.ui.buttons.ButtonRegister;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class InfoUi extends BaseUi {
    int infoSlot = 0;
    private QueryMode queryMode = QueryMode.MATERIAL;

    protected InfoUi(List<Integer> sectionIndexes, Inventory inventory) {
        super(sectionIndexes, inventory);
    }

    @Override
    public void refreshUi() {
        setButtonAt(infoSlot, ButtonRegister.getInstance().INFO);
    }


    public void toggleQueryMode() {
        switch (queryMode) {
            case MATERIAL:
                queryMode = QueryMode.RESULT;
                break;
            case RESULT:
                queryMode = QueryMode.MATERIAL;
                break;
        }
    }

    public void setQueryMode(QueryMode queryMode) {
        this.queryMode = queryMode;
    }

    public int getInfoSlot() {
        return infoSlot;
    }

    public void setInfoSlot(int infoSlot) {
        this.infoSlot = infoSlot;
    }

    public abstract boolean hasQuery();

    public QueryMode getQueryMode() {
        return queryMode;
    }

    public abstract ItemStack getQueryItem();

    public abstract void setQueryItem(ItemStack itemStack);
}
