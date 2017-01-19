package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.defaultui.interfaces.*;

public class GUIController implements GUIControllerInterface
{
    //Interfaces
    private BKTypesInterface bkTypes;
    private SelectedTagsInterface selectedTags;
    private AvailableTagsInterface availableTags;
    private BookmarksListInterface bookmarks;
    private SearchInterface search;
    private MenuInterface menu;
    private QuickPanelInterface quickPanel;


    private Bootstrap bootstrap;

    @Override
    public void setBootstrap(Bootstrap bootstrap)
    {

    }

    @Override
    public void setSelectedTags(List<String> tags)
    {

    }

    @Override
    public void setSearchTerm(String searchTerm)
    {

    }

    @Override
    public void setShowType(String type, boolean show)
    {

    }

    @Override
    public Bootstrap getBootstrap()
    {
        return null;
    }

    @Override
    public Set<String> getAvailableTags()
    {
        return null;
    }

    @Override
    public Set<String> getTypes()
    {
        return null;
    }

    @Override
    public List<BookmarkUIInterface> getVisibleBookmarks()
    {
        return null;
    }
}
