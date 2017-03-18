package com.bookmarkanator.ui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.ui.interfaces.*;
import com.bookmarkanator.util.*;

public class UIController implements GUIControllerInterface
{
    private static UIController controller;

    //Interfaces
    private BKTypes bkTypesInterface;
    private SelectedTagsInterface selectedTagsInterface;
    private AvailableTagsInterface availableTagsInterface;
    private BookmarksListInterface bookmarksInterface;
    private SearchInterface searchInterface;
    private MenuInterface menuInterface;
    private QuickPanelInterface quickPanelInterface;
    private NewBookmarkSelectionInterface newBookmarkSelectionInterface;

    private Set<AbstractBookmark> visibleBookmarks;
    private Set<String> availableTags;
    private Set<String> selectedTags;
    private Set<String> allTypes;
    private Set<String> visibleTypes;
    private Set<String> showOnlyTheseTypes;

    //Search related variables
    private String searchTerm;
    private Map<String, Boolean> searchInclusions;

    //Selected Tag Related
    private String selectedTagsOperation = INCLUDE_BOOKMARKS_WITH_ALL_TAGS;

    //Search constants
    public static final String SEARCH_TYPES_KEY = "BOOKMARK-TYPES-SEARCH";
    public static final String SEARCH_TAGS_KEY = "BOOKMARK-TAGS-SEARCH";
    public static final String SEARCH_BOOKMARK_TEXT_KEY = "BOOKMARK-TEXT-SEARCH";
    public static final String SEARCH_BOOKMARK_NAMES_KEY = "BOOKMARK-NAME-SEARCH";
    //Other constants
    public static final String INCLUDE_BOOKMARKS_WITH_ALL_TAGS = "ALL TAGS";
    public static final String INCLUDE_BOOKMARKS_WITH_ANY_TAGS = "ANY TAG";
    public static final String INCLUDE_BOOKMARKS_WITHOUT_TAGS = "WITHOUT TAGS";

    public UIController()
        throws Exception
    {
        ContextInterface context = Bootstrap.IOInterface().getContext();
        ModuleLoader.use().addClassToTrack(AbstractUIBookmark.class);
        ModuleLoader.use().addModulesToClasspath();

        this.visibleBookmarks = new HashSet<>();
        this.visibleBookmarks.addAll(context.getBookmarks());
        this.availableTags = new HashSet<>();
        this.selectedTags = new HashSet<>();

        this.allTypes = context.getTypesLoaded(context.getBookmarks());
        this.visibleTypes = new HashSet<>();
        this.showOnlyTheseTypes = new HashSet<>();
        this.showOnlyTheseTypes.addAll(this.allTypes);
        this.searchInclusions = new HashMap<>();
        searchInclusions.put(UIController.SEARCH_TYPES_KEY, false);
        searchInclusions.put(UIController.SEARCH_BOOKMARK_TEXT_KEY, true);
        searchInclusions.put(UIController.SEARCH_BOOKMARK_NAMES_KEY, true);
        searchInclusions.put(UIController.SEARCH_TAGS_KEY, true);
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
        this.newBookmarkSelectionInterface.setTypes(getAllTypesUIs());
        this.getBookmarksListUI().setVisibleBookmarks(this.getVisibleBookmarks());
        this.getAvailableTagsUI().setAvailableTags(this.getAvailableTags());
        this.getTypesUI().setTypes(this.getVisibleTypes(), this.getShowOnlyTheseTypes());
    }

    @Override
    public Settings getSettings()
    {
        return Bootstrap.use().getSettings();
    }

    @Override
    public void setSelectedTags(Set<String> tags)
        throws Exception
    {
        if (tags == null)
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
        if (this.searchInclusions.get(key) != null)
        {
            this.searchInclusions.put(key, value);
            this.updateUI();
        }
    }

    @Override
    public boolean getSearchInclusion(String key)
        throws Exception
    {
        Boolean b = this.searchInclusions.get(key);
        if (b != null)
        {
            return b;
        }
        else
        {
            throw new Exception("Search inclusion key \"" + key + "\" is not a valid search inclusion key.");
        }
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

        if (types != null)
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
    public void setTagMode(String mode)
        throws Exception
    {
        switch (mode)
        {
            case UIController.INCLUDE_BOOKMARKS_WITH_ALL_TAGS:
            case UIController.INCLUDE_BOOKMARKS_WITH_ANY_TAGS:
            case UIController.INCLUDE_BOOKMARKS_WITHOUT_TAGS:
                this.selectedTagsOperation = mode;
                break;
            default:
                throw new Exception("Selected Tag Mode \"" + mode + "\" not supported");
        }
        this.updateUI();
    }

    @Override
    public String getTagMode()
    {
        return this.selectedTagsOperation;
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

    public Set<AbstractUIBookmark> getAllTypesUIs()
        throws Exception
    {
        Set<Class> kbs = ModuleLoader.use().getClassesLoaded(AbstractBookmark.class);
        Map<String, Class> bkClassNames = new HashMap<>();

        for (Class clazz: kbs)
        {
            bkClassNames.put(clazz.getCanonicalName(), clazz);
        }

        Set<AbstractUIBookmark> res = new HashSet<>();
        Set<Class> loadedUIs = ModuleLoader.use().getClassesLoaded(AbstractUIBookmark.class);
        ContextInterface context =Bootstrap.context();

        for (Class clazz: loadedUIs)
        {
            AbstractUIBookmark tmp =  (AbstractUIBookmark)clazz.getConstructor(ContextInterface.class).newInstance(context);
            Class bookmarkClass = bkClassNames.get(tmp.getRequiredBookmarkClassName());
            if (bookmarkClass != null)
            {
                AbstractBookmark abs = ModuleLoader.use().loadClass(bookmarkClass.getCanonicalName(), AbstractBookmark.class);
                if (abs.getTypeName()==null || abs.getTypeName().trim().isEmpty())
                {
                    MLog.warn("Bookmark "+abs.getClass().getCanonicalName()+" has no type string.");
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

    @Override
    public void newBookmark(String type)
        throws Exception
    {
        newBookmarkSelectionInterface.getSelectedBookmarkType().newBookmarkView();
    }

    @Override
    public void setTypesUI(BKTypes types)
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
    public void setNewBookmarkSelectorUI(NewBookmarkSelectionInterface newBookmarkSelectorUI)
    {
        this.newBookmarkSelectionInterface = newBookmarkSelectorUI;
    }

    @Override
    public BKTypes getTypesUI()
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
    public Set<AbstractBookmark> getVisibleBookmarks()
    {
        return this.visibleBookmarks;
    }

    private Set<AbstractBookmark> search(List<AbstractBookmark> bookmarks)
        throws Exception
    {
        if (this.getSearchTerm()==null || this.getSearchTerm().isEmpty())
        {
            return new HashSet<>(bookmarks);
        }

        ContextInterface context = Bootstrap.context();

        boolean includeTags = this.searchInclusions.get(UIController.SEARCH_TAGS_KEY);
        boolean includeTypes = this.searchInclusions.get(UIController.SEARCH_TYPES_KEY);
        boolean includeBookmarkText = this.searchInclusions.get(UIController.SEARCH_BOOKMARK_TEXT_KEY);
        boolean includeBookmarks = this.searchInclusions.get(UIController.SEARCH_BOOKMARK_NAMES_KEY);

        Set<AbstractBookmark> res = new HashSet<>();
        if (includeTags && includeTypes && includeBookmarkText && includeBookmarks)
        {
            res.addAll(context.searchAll(this.getSearchTerm()));
        }
        else
        {
            if (includeTags)
            {
                res.addAll(context.searchTagsLoosly(this.getSearchTerm()));
            }
            if (includeTypes)
            {
                res.addAll(context.searchBookmarkTypes(this.getSearchTerm()));
            }
            if (includeBookmarks)
            {
                res.addAll(context.searchBookmarkNames(this.getSearchTerm()));
            }
            if (includeBookmarkText)
            {
                res.addAll(context.searchBookmarkText(this.getSearchTerm()));
            }
        }

        return new HashSet<>(Filter.use(res).includeIfIn(bookmarks).results());
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
        ContextInterface context = Bootstrap.context();
        this.allTypes = context.getTypesLoaded(context.getBookmarks());

        this.visibleBookmarks.clear();

        List<AbstractBookmark> tmpBKs = new ArrayList<>();
        tmpBKs.addAll(applySelectedTags(getVisibleBookmarkTypes(context.getBookmarks())));
        this.visibleBookmarks = search(tmpBKs);
        this.visibleTypes = context.getTypesLoaded(this.visibleBookmarks);
        this.availableTags = context.getTagsFromBookmarks(this.visibleBookmarks);
        this.availableTags.removeAll(this.selectedTags);
    }

    private List<AbstractBookmark> getVisibleBookmarkTypes(Set<AbstractBookmark> bookmarks)
        throws Exception
    {
        return Filter.use(bookmarks).keepBookmarkTypes(this.showOnlyTheseTypes).results();
    }

    private Set<AbstractBookmark> applySelectedTags(List<AbstractBookmark> bookmarks)
    {
        Set<String> selectedTags = this.getSelectedTags();

        if (selectedTags.isEmpty())
        {
            return new HashSet<>(bookmarks);
        }

        List<AbstractBookmark> tmp = new ArrayList<>();

        switch (this.selectedTagsOperation)
        {
            case UIController.INCLUDE_BOOKMARKS_WITH_ALL_TAGS:
                tmp =  Filter.use(bookmarks).keepWithAllTags(this.getSelectedTags()).results();
                break;
            case UIController.INCLUDE_BOOKMARKS_WITH_ANY_TAGS:
                tmp =  Filter.use(bookmarks).keepWithAnyTag(this.getSelectedTags()).results();
                break;
            case UIController.INCLUDE_BOOKMARKS_WITHOUT_TAGS:
                tmp =  Filter.use(bookmarks).excludeWithTags(this.getSelectedTags()).results();
                break;
        }

        return new HashSet<>(tmp);
    }

    public static UIController use()
    {
        if (controller==null)
        {
            try
            {
                controller = new UIController();
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
        return controller;
    }
}
