package cat.nyaa.fusion.ui.buttons.impl;

import cat.nyaa.fusion.ui.IQueryUiAccess;
import cat.nyaa.fusion.ui.buttons.GUIButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonNextPage extends GUIButton {
    @Serializable
    String title = "&aNextPage";

    @Serializable
    List<String> lores = new ArrayList<>();

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
        int size = iQueryUiAccess.getSize();
        int nextPage = currentPage+1;

        if (currentPage == size - 1){
            nextPage = 0;
        }

        iQueryUiAccess.setPage(nextPage);
    }

}
