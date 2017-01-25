package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class SelectedTagsPanel extends BasicUIItem implements SelectedTagsInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private ActionListBox actionListBox;
    private Set<String> selectedTags;

    public SelectedTagsPanel()
    {
        selectedTags = new HashSet<>();
    }

    public SelectedTagsPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;

        TerminalSize size = new TerminalSize(30, 10);
        actionListBox = new ActionListBox(size);
    }

    private synchronized void removeMe(String itemName)
    {
        System.out.println("Removing "+itemName);
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

        for (final String tag: this.selectedTags)
        {
            actionListBox.addItem(tag, new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        getGUIController().removeSelectedTag(tag);
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
    public void setSelectedTags(Set<String> selectedTags)
    {
        this.selectedTags = selectedTags;
        fillTags();
    }

    @Override
    public Set<String> getSelectedTags()
    {
        return this.selectedTags;
    }
}
