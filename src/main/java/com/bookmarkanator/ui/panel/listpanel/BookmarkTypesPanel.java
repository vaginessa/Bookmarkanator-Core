package com.bookmarkanator.ui.panel.listpanel;

import com.bookmarkanator.bookmarks.BookmarksUtil;
import com.bookmarkanator.interfaces.ListableItem;
import com.bookmarkanator.ui.panel.itempanel.ListableToggleButtonItemPanel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by micah on 4/22/16.
 */
public class BookmarkTypesPanel extends StringsPanel {

    @Override
    public void refresh()
    {
//        pan.removeAll();
//        itemNames.clear();
//
//        for (ListableItem b : getVisibleLabels()) {
//            ListableToggleButtonItemPanel bp = new ListableToggleButtonItemPanel(b);
//            bp.setAlignmentX(Component.CENTER_ALIGNMENT);
//            pan.add(bp);
//            itemNames.add(b.getName());
//        }
//
//        itemsSearchMap = BookmarksUtil.getListableItemsTextStrings(getLabels());
//        this.scroll.updateUI();
    }

    public List<String> getSelectedTypes()
    {
        List<String> s = new ArrayList<>();

//        for (Component c: pan.getComponents())
//        {
//            if (c instanceof ListableToggleButtonItemPanel)
//            {
//                ListableToggleButtonItemPanel lt = (ListableToggleButtonItemPanel)c;
//
//                if (lt.getButton().isSelected())
//                {
//                    s.add(lt.getButton().getText());
//                }
//            }
//        }
        return s;
    }
}
