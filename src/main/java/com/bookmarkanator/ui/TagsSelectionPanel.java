package com.bookmarkanator.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * Created by micah on 4/12/16.
 */
public class TagsSelectionPanel extends ListableItemsPanel {
    private Set<String> tags;
    private ListableItemsPanel bookmarksPanel;
    private SelectedTagsPanel selectedTagsPanel;

    public TagsSelectionPanel() {

        setBorder(BorderFactory.createLineBorder(Color.black));
        tags = new HashSet<>();
    }

    public void refresh()
    {
        for (String s: tags)
        {
            this.add(new TagPanel(s));
        }
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
        refresh();
    }
}
