package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import com.googlecode.lanterna.*;
import com.googlecode.lanterna.gui2.*;

public class BookmarksPanel extends BasicUIItem implements BookmarksListInterface
{
    private Panel mainPanel;
    private MultiWindowTextGUI gui;
    private ActionListBox actionListBox;
    private List<AbstractUIBookmark> bookmarkUIInterfaces;
    public Set<AbstractBookmark> bookmarks;

    public BookmarksPanel()
    {
        TerminalSize size = new TerminalSize(30, 10);
        actionListBox = new ActionListBox(size);
        bookmarks = new HashSet<>();
    }

//    public BookmarksPanel(MultiWindowTextGUI gui)
//    {
//        this.gui = gui;
//
//
//
//        actionListBox.addItem("Bookmark 1", new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                removeMe("Bookmark 1");
//            }
//        });
//        actionListBox.addItem("Bookmark 2", new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                removeMe("Bookmark 2");
//            }
//        });
//        actionListBox.addItem("Bookmark 3", new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                removeMe("Bookmark 3");
//            }
//        });
//    }

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


    private void fillList()
        throws Exception
    {
        this.actionListBox.clearItems();

        for (AbstractBookmark bk: this.getVisibleBookmarks())
        {
            String classToLoadKey = MainWindow.UI_PREFIX_VALUE+""+MainWindow.UI_CLASS_PREFIX_VALUE+""+bk.getClass().getCanonicalName();
            String className = (String)this.getGUIController().getSettings().getSetting(classToLoadKey);
            final AbstractUIBookmark<String,String, String, String> bkui = ModuleLoader.use().loadClass(className, AbstractUIBookmark.class, this.getGUIController().getBootstrap().getClassLoader());

            assert bkui !=null;

            bkui.setAbstractBookmark(bk);


            this.actionListBox.addItem(bkui.getBookmarkListItemView(), new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        bkui.getBookmark().action();
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
//                        getGUIController().getBootstrap().acceptException(e);//pipe exception to a place that can handle it.
                    }
                }
            });
        }
    }

    @Override
    public void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception
    {
        this.bookmarks = bookmarks;
        fillList();
    }

    @Override
    public Set<AbstractBookmark> getVisibleBookmarks()
    {
        return this.bookmarks;
    }

    @Override
    public boolean isEditMode()
    {
        return false;
    }
}
