package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class BookmarksPanel extends BasicUIItem implements BookmarksListInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private ActionListBox actionListBox;
    private List<BookmarkUIInterface> bookmarkUIInterfaces;

    public BookmarksPanel(MultiWindowTextGUI gui)
    {
        this.gui = gui;

        TerminalSize size = new TerminalSize(30, 10);
        actionListBox = new ActionListBox(size);
        actionListBox.addItem("Bookmark 1", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Bookmark 1");
            }
        });
        actionListBox.addItem("Bookmark 2", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Bookmark 2");
            }
        });
        actionListBox.addItem("Bookmark 3", new Runnable()
        {
            @Override
            public void run()
            {
                removeMe("Bookmark 3");
            }
        });
    }

    private synchronized void removeMe(String itemName)
    {
        System.out.println(itemName+" Action");
    }

    public Panel getPanel()
    {
        mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        mainPanel.addComponent(actionListBox);

        return mainPanel;
    }

    @Override
    public void setBookmarksList(List<BookmarkUIInterface> bookmarks)
    {
        this.bookmarkUIInterfaces = bookmarks;
    }

    @Override
    public List<BookmarkUIInterface> getBookmarksList()
    {
        return this.bookmarkUIInterfaces;
    }
}
