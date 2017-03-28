package com.bookmarkanator.ui;

//import java.awt.event.KeyEvent;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import com.bookmarkanator.util.*;

public class UIController implements UIControllerInterface
{
    //Interfaces
    private BKTypesInterface bkTypesInterface;
    private SelectedTagsInterface selectedTagsInterface;
    private AvailableTagsInterface availableTagsInterface;
    private BookmarksListInterface bookmarksInterface;
    private SearchInterface searchInterface;
    private MenuInterface menuInterface;
    private QuickPanelInterface quickPanelInterface;
    private NewBookmarkSelectionInterface newBookmarkSelectionInterface;

    private boolean editMode;

    //Types
    private Set<AbstractUIBookmark> allAvailableBKTypes;
    private Map<String, AbstractUIBookmark> allAvailableBKTypesMap;
    private Set<AbstractUIBookmark> bkTypesForExistingBookmarks;
    private Set<String> typesToHighlight;//Bookmark class name here.
    private Set<String> tagsToHighlightBorders;//Highlights exact search term matches.

    //Bookmarks
    private Set<AbstractUIBookmark> visibleUIBookmarks;

    //Tags
    private Set<String> availableTags;
    private Set<String> highlightTags;
    private SearchOptions.TagsInfo currentTagGroup;
    private Set<SearchOptions.TagsInfo> groupsToHighlight;

    //Search
    private SearchOptions searchOptions;
    private boolean highlightSearchTerm;

    public UIController()
        throws Exception
    {
        ModuleLoader.use().addClassToTrack(AbstractUIBookmark.class);
        ModuleLoader.use().addModulesToClasspath();

        //Types
        this.allAvailableBKTypes = getAllTypesUIs();
        this.allAvailableBKTypesMap = wrapAllTypeUIs(this.allAvailableBKTypes);
        this.bkTypesForExistingBookmarks = new HashSet<>();
        this.typesToHighlight = new HashSet<>();

        this.visibleUIBookmarks = new HashSet<>();
        this.availableTags = new HashSet<>();
        this.highlightTags = new HashSet<>();
        this.groupsToHighlight = new HashSet<>();
        this.tagsToHighlightBorders = new HashSet<>();

        this.searchOptions = new SearchOptions();

        //Initial selection of all types
        this.bkTypesForExistingBookmarks = getBKTypesForExistingBookmarks();

        for (AbstractUIBookmark abstractUIBookmark : bkTypesForExistingBookmarks)
        {
            searchOptions.setSelectedBKType(abstractUIBookmark.getRequiredBookmarkClassName());
        }

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
        this.searchInterface.setSearchOptions(this.searchOptions);//Set this so that the search checkboxes get populated.

        updateUI();
    }

    @Override
    public void updateUI()
        throws Exception
    {

        //TODO The tag groups are not properly being synced. When a tag group is removed, it is reflected in the search, but the current tag group
        //has not changed, and adding or removing tags after that does nothing becaue the current tag group is not in search options.

        //Get all types in the bookmarks list
        this.bkTypesForExistingBookmarks = getBKTypesForExistingBookmarks();

        for (AbstractUIBookmark abstractUIBookmark : bkTypesForExistingBookmarks)
        {
            if (this.searchOptions.getSelectedTypes().contains(abstractUIBookmark.getRequiredBookmarkClassName()))
            {
                searchOptions.setSelectedBKType(abstractUIBookmark.getRequiredBookmarkClassName());
            }
        }

        //Set bookmark types in the types UI
        this.bkTypesInterface.setTypes(this.bkTypesForExistingBookmarks, this.searchOptions.getSelectedTypes(), this.typesToHighlight);

        //Compute visible bookmarks using search options, and selected tags
        this.visibleUIBookmarks = applySearchOptions();

        //set bookmarks in bookmark list
        this.bookmarksInterface.setVisibleBookmarks(this.visibleUIBookmarks);

        //Set available tags in available tags UI
        this.availableTagsInterface.setAvailableTags(this.availableTags, this.highlightTags, this.tagsToHighlightBorders);

        //Set selected tags
        this.selectedTagsInterface
            .setSelectedTags(searchOptions.getTagGroups(), this.highlightTags, this.groupsToHighlight, this.tagsToHighlightBorders);

        //Highlight search term
        this.searchInterface.highlightSearchTerm(this.highlightSearchTerm);
    }

    private Set<AbstractUIBookmark> applySearchOptions()
        throws Exception
    {
        Set<AbstractBookmark> tmp;

        if (searchOptions.getSearchTerm() != null && !searchOptions.getSearchTerm().isEmpty())
        {
            tmp = new HashSet<>();
            String searchTerm = searchOptions.getSearchTerm();

            if (searchOptions.isSearchTags())
            {
                tmp.addAll(Bootstrap.context().searchTagsLoosly(searchTerm));
            }

            if (searchOptions.isSearchBookmarkTypes())
            {
                tmp.addAll(Bootstrap.context().searchBookmarkTypes(searchTerm));
            }

            if (searchOptions.isSearchBookmarkText())
            {
                tmp.addAll(Bootstrap.context().searchBookmarkText(searchTerm));
            }

            if (searchOptions.isSearchBookmarkNames())
            {
                tmp.addAll(Bootstrap.context().searchBookmarkNames(searchTerm));
            }
        }
        else
        {
            tmp = Bootstrap.context().getBookmarks();
        }

        List<AbstractBookmark> res = Filter.use(tmp).filterBySearchOptions(searchOptions).results();

        //Set available tags here because we have the list of bookmarks
        this.availableTags = Bootstrap.context().getTagsFromBookmarks(res);

        this.highlightTags.clear();
        this.tagsToHighlightBorders.clear();

        highlightSearchTerm = false;

        Set<String> tagsFromAllGroups = searchOptions.getTagsFromAllGroups();

        if (searchOptions.getSearchTerm() != null && !searchOptions.getSearchTerm().isEmpty())
        {
            //Determine which tags should be highlighted
            for (String s : this.availableTags)
            {
                if (s.contains(searchOptions.getSearchTerm()) || searchOptions.getSearchTerm().contains(s))
                {
                    this.highlightTags.add(s);

                    if (searchOptions.getSearchTerm().equalsIgnoreCase(s))
                    {
                        this.tagsToHighlightBorders.add(s);
                        highlightSearchTerm = true;
                    }
                }
            }
        }

        this.availableTags.removeAll(tagsFromAllGroups);

        return this.wrapAll(res);
    }

    @Override
    public Settings getSettings()
    {
        return Bootstrap.use().getSettings();
    }

    public Set<AbstractUIBookmark> getVisibleUIBookmarks()
    {
        return this.visibleUIBookmarks;
    }

    @Override
    public SearchOptions getSearchOptions()
    {
        return searchOptions;
    }

    @Override
    public void setSearchOptions(SearchOptions searchOptions)
        throws Exception
    {
        this.searchOptions = searchOptions;
        this.updateUI();
    }

    @Override
    public SimilarItemIterator getSimilarItemIterator(String closeSearchTerm)
    {
        List<String> tmpList = new ArrayList<>(tagsToHighlightBorders);
        tmpList.addAll(highlightTags);

        return new SimilarItemIterator(closeSearchTerm,tmpList);
    }

    @Override
    public void toggleShowType(AbstractUIBookmark abstractUIBookmark)
        throws Exception
    {
        if (this.searchOptions.getSelectedTypes().contains(abstractUIBookmark.getRequiredBookmarkClassName()))
        {
            this.searchOptions.setUnselectedBKType(abstractUIBookmark.getRequiredBookmarkClassName());
        }
        else
        {
            this.searchOptions.setSelectedBKType(abstractUIBookmark.getRequiredBookmarkClassName());
        }

        updateUI();
    }

    @Override
    public void showAllTypes()
        throws Exception
    {
        for (AbstractUIBookmark abstractUIBookmark : this.bkTypesForExistingBookmarks)
        {
            this.searchOptions.setSelectedBKType(abstractUIBookmark.getRequiredBookmarkClassName());
        }
        updateUI();
    }

    @Override
    public void hideAllTypes()
        throws Exception
    {
        this.searchOptions.setUnselectAllBKTypes();
        updateUI();
    }

    private SearchOptions.TagsInfo getLastTagGroup()
    {
        return searchOptions.getTagGroups().get(this.searchOptions.getTagGroups().size() - 1);
    }

    private void ensureTagGroup()
    {
        if (this.currentTagGroup == null)
        {
            if (this.searchOptions.getTagGroups().isEmpty())
            {
                this.currentTagGroup = this.searchOptions.new TagsInfo();
                this.searchOptions.getTagGroups().add(this.currentTagGroup);
            }
            else
            {
                this.currentTagGroup = getLastTagGroup();
            }
        }
    }

    @Override
    public void clearAllSelectedTagGroups()
        throws Exception
    {
        this.searchOptions.getTagGroups().clear();
        this.currentTagGroup = null;
        ensureTagGroup();
        updateUI();
    }

    @Override
    public void setTagModeForCurrentGroup(String tagModeForCurrentGroup)
        throws Exception
    {
        ensureTagGroup();
        this.currentTagGroup.setOperation(tagModeForCurrentGroup);
        updateUI();
    }

    @Override
    public void setCurrentGroup(SearchOptions.TagsInfo currentGroup)
        throws Exception
    {
        this.currentTagGroup = currentGroup;
    }

    @Override
    public SearchOptions.TagsInfo getCurrentGroup()
    {
        return this.currentTagGroup;
    }

    @Override
    public void addTagGroup()
        throws Exception
    {
        if (this.currentTagGroup != null)
        {
            SearchOptions.TagsInfo tagsInfo = this.searchOptions.new TagsInfo();
            tagsInfo.setOperation(this.currentTagGroup.getOperation());

            this.searchOptions.getTagGroups().add(tagsInfo);
            this.currentTagGroup = tagsInfo;
            updateUI();
        }
    }

    @Override
    public void removeTagGroup(SearchOptions.TagsInfo tagGroup)
        throws Exception
    {
        this.searchOptions.getTagGroups().remove(tagGroup);

        if (tagGroup == this.currentTagGroup)
        {
            this.currentTagGroup = getLastTagGroup();
        }

        updateUI();
    }

    @Override
    public void removeTagFromGroup(SearchOptions.TagsInfo tagGroup, String tag)
        throws Exception
    {
        int i = this.searchOptions.getTagGroups().indexOf(tagGroup);

        if (i > -1)
        {
            tagGroup = this.searchOptions.getTagGroups().get(i);
            tagGroup.getTags().remove(tag);
        }
        updateUI();
    }

    @Override
    public void selectTag(String tag)
        throws Exception
    {
        ensureTagGroup();
        for (String s : availableTags)
        {
            if (tag.equalsIgnoreCase(s))
            {
                this.currentTagGroup.getTags().add(tag);
                updateUI();
                return;
            }
        }
    }

    /**
     * This method gets all bookmark types available. It is used for showing a list of bookmark types to choose from when
     * creating a new bookmark.
     *
     * @return A set of all AbstractUIBookmark's available.
     * @throws Exception
     */
    public Set<AbstractUIBookmark> getAllTypesUIs()
        throws Exception
    {
        Set<Class> kbs = ModuleLoader.use().getClassesLoaded(AbstractBookmark.class);
        Map<String, Class> bkClassNames = new HashMap<>();

        for (Class clazz : kbs)
        {
            bkClassNames.put(clazz.getCanonicalName(), clazz);
        }

        Set<AbstractUIBookmark> res = new HashSet<>();
        Set<Class> loadedUIs = ModuleLoader.use().getClassesLoaded(AbstractUIBookmark.class);

        for (Class clazz : loadedUIs)
        {
            AbstractUIBookmark tmp = (AbstractUIBookmark) clazz.getConstructor().newInstance();
            tmp.setController(this);
            Class bookmarkClass = bkClassNames.get(tmp.getRequiredBookmarkClassName());
            if (bookmarkClass != null)
            {
                AbstractBookmark abs = ModuleLoader.use().loadClass(bookmarkClass.getCanonicalName(), AbstractBookmark.class);
                if (abs.getTypeName() == null || abs.getTypeName().trim().isEmpty())
                {
                    MLog.warn("Bookmark " + abs.getClass().getCanonicalName() + " has no type string.");
                }
                else
                {
                    tmp.setBookmark(abs);
                    res.add(tmp);

                }
            }
        }

        return res;
    }

    private Map<String, AbstractUIBookmark> wrapAllTypeUIs(Set<AbstractUIBookmark> allAvailableBKTypes)
    {
        Map<String, AbstractUIBookmark> res = new HashMap<>();

        for (AbstractUIBookmark abstractUIBookmark : allAvailableBKTypes)
        {
            res.put(abstractUIBookmark.getRequiredBookmarkClassName(), abstractUIBookmark);
        }

        return res;
    }

    private Set<AbstractUIBookmark> getBKTypesForExistingBookmarks()
        throws Exception
    {
        Set<String> types = Bootstrap.context().getTypesClassNames(Bootstrap.context().getBookmarks());
        Set<AbstractUIBookmark> res = new HashSet<>();

        for (AbstractUIBookmark abstractUIBookmark : this.allAvailableBKTypes)
        {
            if (types.contains(abstractUIBookmark.getRequiredBookmarkClassName()))
            {
                res.add(abstractUIBookmark);
            }
        }

        return res;
    }

    private AbstractUIBookmark wrap(AbstractBookmark bookmark)
        throws Exception
    {
        AbstractUIBookmark bkUI = this.allAvailableBKTypesMap.get(bookmark.getClass().getCanonicalName());

        if (bkUI != null)
        {
            bkUI = bkUI.getClass().getConstructor().newInstance();
            bkUI.setBookmark(bookmark);
            bkUI.setController(this);
            return bkUI;
        }
        throw new Exception("Cannot find UI for bookmark " + bookmark.getClass().getCanonicalName());
    }

    private Set<AbstractUIBookmark> wrapAll(Collection<AbstractBookmark> abstractBookmarks)
        throws Exception
    {
        Set<AbstractUIBookmark> res = new HashSet<>();

        for (AbstractBookmark bookmark : abstractBookmarks)
        {
            res.add(this.wrap(bookmark));
        }

        return res;
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
    public void setNewBookmarkSelectorUI(NewBookmarkSelectionInterface newBookmarkSelectorUI)
    {
        this.newBookmarkSelectionInterface = newBookmarkSelectorUI;
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
    public NewBookmarkSelectionInterface getNewBookmarkSelectorUI()
    {
        return newBookmarkSelectionInterface;
    }

    @Override
    public boolean isEditMode()
    {
        return editMode;
    }

    @Override
    public void setEditMode(boolean editMode)
    {
        this.editMode = editMode;

        //Interfaces
        bkTypesInterface.setEditMode(editMode);
        selectedTagsInterface.setEditMode(editMode);
        availableTagsInterface.setEditMode(editMode);
        bookmarksInterface.setEditMode(editMode);
        searchInterface.setEditMode(editMode);
        menuInterface.setEditMode(editMode);
        //        quickPanelInterface.setEditMode(editMode);
        //        newBookmarkSelectionInterface.set;
    }

}
