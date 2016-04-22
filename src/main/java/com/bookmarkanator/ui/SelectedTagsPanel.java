package com.bookmarkanator.ui;

import java.awt.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.interfaces.*;

/**
 * Created by micah on 4/12/16.
 */
public class SelectedTagsPanel extends ListableItemsPanel{
    public SelectedTagsPanel() {

    }

    @Override
    public void refresh()
    {
        pan.removeAll();
        itemNames.clear();

        for (ListableItem b : getCurrentlyShowingItemsList()) {
            ListableButtonItemPanel bp = new ListableButtonItemPanel(b);
            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
            pan.add(bp);
            itemNames.add(b.getName());
        }

        itemsSearchMap = BookmarksUtil.getListableItemsTextStrings(getItemsList());
        this.scroll.updateUI();
    }
}
