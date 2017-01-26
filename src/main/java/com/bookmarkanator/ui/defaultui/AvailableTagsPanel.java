package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.ui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class AvailableTagsPanel extends BasicUIItem implements AvailableTagsInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private Set<String> availableTags;

    public AvailableTagsPanel()
    {
        availableTags = new HashSet<>();
    }

    private ActionListBox actionListBox;

    public AvailableTagsPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;

        TerminalSize size = new TerminalSize(30, 10);
        actionListBox = new ActionListBox(size);
    }

    private synchronized void removeMe(String itemName)
    {
        System.out.println("Adding "+itemName);
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        mainPanel.addComponent(actionListBox);

        return mainPanel;
    }

    private void fillTags()
    {
        actionListBox.clearItems();

        for (final String tag: this.availableTags)
        {
            actionListBox.addItem(tag, new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        getGUIController().addSelectedTag(tag);
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public void setAvailableTags(Set<String> availableTags)
    {
        this.availableTags = availableTags;
        fillTags();
    }

    @Override
    public Set getAvailableTags()
    {
        return this.availableTags;
    }
}
