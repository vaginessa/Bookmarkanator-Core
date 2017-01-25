package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.ui.defaultui.interfaces.*;
import com.bookmarkanator.util.*;

public class GUIController implements GUIControllerInterface
{
    //Interfaces
    private BKTypesInterface bkTypesInterface;
    private SelectedTagsInterface selectedTagsInterface;
    private AvailableTagsInterface availableTagsInterface;
    private BookmarksListInterface bookmarksInterface;
    private SearchInterface searchInterface;
    private MenuInterface menuInterface;
    private QuickPanelInterface quickPanelInterface;

    private Bootstrap bootstrap;

    private Set<AbstractBookmark> visibleBookmarks;
    private Set<String> availableTags;
    private Set<String> selectedTags;
    private Set<String> typeNames;
    private Map<String, Boolean> showTypes;

    //Search related variables
    private String searchTerm;
    private boolean includeTypes;
    private boolean includeTags;
    private boolean includeBookmarks;
    private boolean includeBookmarkText;

    public static final String SEARCH_TYPES_KEY = "types";
    public static final String SEARCH_TAGS_KEY = "tags";
    public static final String SEARCH_BOOKMARKS_KEY = "bookmarks";
    public static final String SEARCH_BOOKMARKS_TEXT_KEY = "bookmarks-text";

    public GUIController(Bootstrap bootstrap)
        throws Exception
    {
        assert bootstrap != null;
        this.bootstrap = bootstrap;

        this.visibleBookmarks = new HashSet<>();
        this.visibleBookmarks.addAll(bootstrap.getBkioInterface().getContext().getBookmarks());
        this.availableTags = new HashSet<>();
        this.selectedTags = new HashSet<>();
        this.typeNames = new HashSet<>();
    }

    @Override
    public void initUI()
        throws Exception
    {
        assert bkTypesInterface != null;
        assert selectedTagsInterface != null;
        assert availableTagsInterface != null;
        assert bookmarksInterface != null;
        assert searchInterface != null;
        assert menuInterface != null;
        assert quickPanelInterface != null;

        this.updateBookmarksData();
        this.getBookmarksListUI().setVisibleBookmarks(this.getVisibleBookmarks());
        this.getAvailableTagsUI().setAvailableTags(this.getAvailableTags());
        this.getTypesUI().setTypes(this.getTypes());
    }

    @Override
    public Bootstrap getBootstrap()
    {
        return this.bootstrap;
    }

    @Override
    public Settings getSettings()
    {
        return getBootstrap().getSettings();
    }

    @Override
    public void setSelectedTags(Set<String> tags)
        throws Exception
    {
        this.selectedTags = tags;
        this.updateUI();
    }

    @Override
    public void addSelectedTag(String tag)
        throws Exception
    {
        this.selectedTags.add(tag);
        this.updateUI();
    }

    @Override
    public void removeSelectedTag(String tag)
        throws Exception
    {
        this.selectedTags.remove(tag);
        this.updateUI();
    }

    @Override
    public Set<String> getSelectedTags()
    {
        return this.selectedTags;
    }

    @Override
    public void setSearchTerm(String searchTerm)
        throws Exception
    {
        this.searchTerm = searchTerm;
        this.updateUI();
    }

    @Override
    public String getSearchTerm()
    {
        return this.searchTerm;
    }

    @Override
    public void setSearchInclusions(String key, boolean value)
        throws Exception
    {
        boolean update = false;
        switch (key)
        {
            case GUIController.SEARCH_BOOKMARKS_TEXT_KEY:
                this.includeBookmarkText = value;
                update = true;
                break;
            case GUIController.SEARCH_BOOKMARKS_KEY:
                this.includeBookmarks = value;
                update = true;
                break;
            case GUIController.SEARCH_TAGS_KEY:
                this.includeTags = value;
                update = true;
                break;
            case GUIController.SEARCH_TYPES_KEY:
                this.includeTypes = value;
                update = true;
                break;
        }
        if (update)
        {
            this.updateUI();
        }
    }

    @Override
    public boolean getSearchInclusion(String key)
        throws Exception
    {
        switch (key)
        {
            case GUIController.SEARCH_BOOKMARKS_TEXT_KEY:
                return this.includeBookmarkText;
            case GUIController.SEARCH_BOOKMARKS_KEY:
                return this.includeBookmarks;
            case GUIController.SEARCH_TAGS_KEY:
                return this.includeTags;
            case GUIController.SEARCH_TYPES_KEY:
                return this.includeTypes;
        }
        throw new Exception("Search inclusion key \"" + key + "\" is not a valid search inclusion key.");
    }

    @Override
    public void setShowType(String type, boolean show)
        throws Exception
    {
        this.showTypes.put(type, show);
        this.updateUI();
    }

    @Override
    public Set<String> getAvailableTags()
    {
        return this.availableTags;
    }

    @Override
    public Set<String> getTypes()
    {
        return this.typeNames;
    }

    @Override
    public void setTypesUI(BKTypesInterface types)
    {
        this.bkTypesInterface = types;
    }

    @Override
    public void setSelectedTagsUI(SelectedTagsInterface selectedTagsUI)
    {
        this.selectedTagsInterface = selectedTagsUI;
    }

    @Override
    public void setAvailableTagsUI(AvailableTagsInterface availableTagsUI)
    {
        this.availableTagsInterface = availableTagsUI;
    }

    @Override
    public void setBookmarksListUI(BookmarksListInterface bookmarksListUI)
    {
        this.bookmarksInterface = bookmarksListUI;
    }

    @Override
    public void setSearchUI(SearchInterface searchUI)
    {
        this.searchInterface = searchUI;
    }

    @Override
    public void setMenuUi(MenuInterface menuUI)
    {
        this.menuInterface = menuUI;
    }

    @Override
    public void setQuickPanelUI(QuickPanelInterface quickPanelUI)
    {
        this.quickPanelInterface = quickPanelUI;
    }

    @Override
    public BKTypesInterface getTypesUI()
    {
        return this.bkTypesInterface;
    }

    @Override
    public SelectedTagsInterface getSelectedTagsUI()
    {
        return this.selectedTagsInterface;
    }

    @Override
    public AvailableTagsInterface getAvailableTagsUI()
    {
        return this.availableTagsInterface;
    }

    @Override
    public BookmarksListInterface getBookmarksListUI()
    {
        return this.bookmarksInterface;
    }

    @Override
    public SearchInterface getSearchUI()
    {
        return this.searchInterface;
    }

    @Override
    public MenuInterface getMenuUi()
    {
        return this.menuInterface;
    }

    @Override
    public QuickPanelInterface getQuickPanelUI()
    {
        return this.quickPanelInterface;
    }

    @Override
    public Set<AbstractBookmark> getVisibleBookmarks()
    {
        return this.visibleBookmarks;
    }

    private Set<AbstractBookmark> search()
        throws Exception
    {
        ContextInterface context = this.bootstrap.getBkioInterface().getContext();

        Set<AbstractBookmark> res = new HashSet<>();
        if (this.includeTags && this.includeTypes && this.includeBookmarkText && this.includeBookmarks)
        {
            res.addAll(context.searchAll(this.getSearchTerm()));
        }
        else
        {
            if (this.includeTags)
            {
                res.addAll(context.searchTagsLoosly(this.getSearchTerm()));
            }
            if (this.includeTypes)
            {
                res.addAll(context.searchBookmarkTypes(this.getSearchTerm()));
            }
            if (this.includeBookmarks)
            {
                res.addAll(context.searchBookmarkNames(this.getSearchTerm()));
            }
            if (this.includeBookmarkText)
            {
                res.addAll(context.searchBookmarkText(this.getSearchTerm()));
            }
        }

        return res;
    }

    @Override
    public void updateUI()
        throws Exception
    {
        updateBookmarksData();

        this.bookmarksInterface.setVisibleBookmarks(this.getVisibleBookmarks());
        this.availableTagsInterface.setAvailableTags(this.getAvailableTags());
        this.bkTypesInterface.setTypes(this.getTypes());
        this.selectedTagsInterface.setSelectedTags(this.getSelectedTags());
    }

    private void updateBookmarksData()
        throws Exception
    {
        ContextInterface context = this.bootstrap.getBkioInterface().getContext();
        if (getSearchTerm() != null && !getSearchTerm().isEmpty())
        {//The user is searching. Update lists from that context.
            this.visibleBookmarks = search();
        }
        else
        {
            this.visibleBookmarks.clear();
            if (!this.selectedTags.isEmpty())
            {
                this.visibleBookmarks.addAll(Filter.use(context.getBookmarks()).keepWithAllTags(this.getSelectedTags()).results());
//                this.visibleBookmarks.addAll(context.searchTagsExact(this.getSelectedTags()));
            }
            else
            {
                this.visibleBookmarks.addAll(context.getBookmarks());
            }
            this.availableTags = context.getTags(this.visibleBookmarks);
            this.availableTags.removeAll(this.selectedTags);
            this.typeNames = context.getTypes(this.visibleBookmarks);
        }
    }
}
