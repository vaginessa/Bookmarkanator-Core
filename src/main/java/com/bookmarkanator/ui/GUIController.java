package com.bookmarkanator.ui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.ui.interfaces.*;
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
    private Set<String> allTypes;
    private Set<String> visibleTypes;
    private Set<String> showOnlyTheseTypes;

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
        ContextInterface context = bootstrap.getBkioInterface().getContext();
        this.bootstrap = bootstrap;

        this.visibleBookmarks = new HashSet<>();
        this.visibleBookmarks.addAll(context.getBookmarks());
        this.availableTags = new HashSet<>();
        this.selectedTags = new HashSet<>();

        this.allTypes = context.getTypes(context.getBookmarks());
        this.visibleTypes = new HashSet<>();
        this.showOnlyTheseTypes = new HashSet<>();
        this.showOnlyTheseTypes.addAll(this.allTypes);
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
        this.showOnlyTheseTypes.addAll(this.getAllTypes());
        this.getBookmarksListUI().setVisibleBookmarks(this.getVisibleBookmarks());
        this.getAvailableTagsUI().setAvailableTags(this.getAvailableTags());
        this.getTypesUI().setTypes(this.getVisibleTypes(), this.getShowOnlyTheseTypes());
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
        if (tags==null)
        {
            this.selectedTags.clear();
        }
        else
        {
            this.selectedTags = tags;
        }
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
        if (show)
        {
            this.showOnlyTheseTypes.add(type);
        }
        else
        {
            this.showOnlyTheseTypes.remove(type);
        }
        this.updateUI();
    }

    @Override
    public void showTypes(Set<String> types)
        throws Exception
    {
        this.showOnlyTheseTypes.clear();

        if (types!=null)
        {
            this.showOnlyTheseTypes.addAll(types);
        }
        this.updateUI();
    }

    @Override
    public boolean toggleShowType(String type)
        throws Exception
    {
        boolean res;
        if (this.showOnlyTheseTypes.contains(type))
        {
            this.showOnlyTheseTypes.remove(type);
            res = false;
        }
        else
        {
            this.showOnlyTheseTypes.add(type);
            res = true;
        }

        this.updateUI();
        return res;
    }

    @Override
    public Set<String> getAvailableTags()
    {
        return this.availableTags;
    }

    @Override
    public Set<String> getVisibleTypes()
    {
        return this.visibleTypes;
    }

    @Override
    public Set<String> getShowOnlyTheseTypes()
    {
        return this.showOnlyTheseTypes;
    }

    @Override
    public Set<String> getAllTypes()
    {
        return this.allTypes;
    }

    @Override
    public void setTypesUI(BKTypesInterface types)
    {
        this.bkTypesInterface = types;
        types.setGUIController(this);
    }

    @Override
    public void setSelectedTagsUI(SelectedTagsInterface selectedTagsUI)
    {
        this.selectedTagsInterface = selectedTagsUI;
        selectedTagsUI.setGUIController(this);
    }

    @Override
    public void setAvailableTagsUI(AvailableTagsInterface availableTagsUI)
    {
        this.availableTagsInterface = availableTagsUI;
        availableTagsUI.setGUIController(this);
    }

    @Override
    public void setBookmarksListUI(BookmarksListInterface bookmarksListUI)
    {
        this.bookmarksInterface = bookmarksListUI;
        bookmarksListUI.setGUIController(this);
    }

    @Override
    public void setSearchUI(SearchInterface searchUI)
    {
        this.searchInterface = searchUI;
        searchUI.setGUIController(this);
    }

    @Override
    public void setMenuUi(MenuInterface menuUI)
    {
        this.menuInterface = menuUI;
        menuUI.setGUIController(this);
    }

    @Override
    public void setQuickPanelUI(QuickPanelInterface quickPanelUI)
    {
        this.quickPanelInterface = quickPanelUI;
        quickPanelUI.setGUIController(this);
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
        this.bkTypesInterface.setTypes(this.getAllTypes(), this.getShowOnlyTheseTypes());
        this.selectedTagsInterface.setSelectedTags(this.getSelectedTags());
    }

    private void updateBookmarksData()
        throws Exception
    {
        ContextInterface context = this.bootstrap.getBkioInterface().getContext();
        this.allTypes = context.getTypes(context.getBookmarks());

        if (getSearchTerm() != null && !getSearchTerm().isEmpty())
        {//The user is searching. Update lists from that context.
            this.visibleBookmarks = search();
        }
        else
        {
            this.visibleBookmarks.clear();
            Set<AbstractBookmark> tmpBKs = new HashSet<>();
            tmpBKs.addAll(getVisibleBookmarkTypes(context.getBookmarks()));

            if (!this.selectedTags.isEmpty())
            {
                this.visibleBookmarks.addAll(Filter.use(tmpBKs).keepWithAllTags(this.getSelectedTags()).results());
            }
            else
            {
                this.visibleBookmarks.addAll(tmpBKs);
            }
            this.availableTags = context.getTags(this.visibleBookmarks);
            this.availableTags.removeAll(this.selectedTags);
            this.visibleTypes = context.getTypes(this.visibleBookmarks);
        }
    }

    private List<AbstractBookmark> getVisibleBookmarkTypes(Set<AbstractBookmark> bookmarks)
        throws Exception
    {
       return Filter.use(bookmarks).keepBookmarkTypes(this.showOnlyTheseTypes).results();
    }

}
