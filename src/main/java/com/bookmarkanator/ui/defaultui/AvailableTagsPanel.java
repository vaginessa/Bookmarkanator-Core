package com.bookmarkanator.ui.defaultui;

import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class AvailableTagsPanel extends BasicUIItem implements AvailableTagsInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private ActionListBox actionListBox;

    public AvailableTagsPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;

        TerminalSize size = new TerminalSize(30, 10);
        actionListBox = new ActionListBox(size);
        actionListBox.addItem("Item 1", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Item 1");
            }
        });
        actionListBox.addItem("Item 2", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Item 2");
            }
        });
        actionListBox.addItem("Item 3", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Item 3");
            }
        });
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
}
