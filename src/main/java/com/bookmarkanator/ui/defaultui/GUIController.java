package com.bookmarkanator.ui.defaultui;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
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

//    private Settings settings;

    @Override
    public void initUI()
        throws Exception
    {
        assert bootstrap !=null;

        //Get settings, diff default UI settings in and save if necessary.


//        ClassLoader classLoader = bootstrap.getClassLoader();

//        bkTypes = ModuleLoader.use().loadClass(GUIController.BK_TYPES_DEFAULT_CLASS, BKTypesInterface.class, classLoader);
//        bkTypes.setGUIController(this);
//
//        selectedTags = ModuleLoader.use().loadClass(GUIController.SELECTED_TAGS_DEFAULT_CLASS, SelectedTagsInterface.class, classLoader);
//        selectedTags.setGUIController(this);
//
//        availableTags = ModuleLoader.use().loadClass(GUIController.AVAILABLE_TAGS_DEFAULT_CLASS, AvailableTagsInterface.class, classLoader);
//        availableTags.setGUIController(this);
//
//        bookmarks = ModuleLoader.use().loadClass(GUIController.BOOKMARKS_LIST_DEFAULT_CLASS, BookmarksListInterface.class, classLoader);
//        bookmarks.setGUIController(this);
//
//        search = ModuleLoader.use().loadClass(GUIController.SEARCH_PANEL_DEFAULT_CLASS, SearchInterface.class, classLoader);
//        search.setGUIController(this);
//
//        menu = ModuleLoader.use().loadClass(GUIController.MENU_PANEL_DEFAULT_CLASS, MenuInterface.class, classLoader);
//        menu.setGUIController(this);
//
//        quickPanel = ModuleLoader.use().loadClass(GUIController.QUICK_PANEL_DEFAULT_CLASS, QuickPanelInterface.class, classLoader);
//        quickPanel.setGUIController(this);

        //call getClass from module loader for each class of that type in the settings.
        //init each class as is required by the interface.
        //***Have the MainWindow class get it's classes from this class, and cast them to what it expects. Then it can work with them as it sees fit.
        this.getBookmarksListUI().setVisibleBookmarks(this.getVisibleBookmarks());
    }



    @Override
    public void setBootstrap(Bootstrap bootstrap)
    {
        this.bootstrap = bootstrap;
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
    public void setSelectedTags(List<String> tags)
    {

    }

    @Override
    public Set<String> getSelectedTags()
    {
        return null;
    }

    @Override
    public void setSearchTerm(String searchTerm)
    {

    }

    @Override
    public void setSearchInclusions(String key, boolean value)
    {

    }

    @Override
    public boolean getSearchInclusion(String key)
    {
        return false;
    }

    @Override
    public String getSearchTerm()
    {
        return null;
    }

    @Override
    public void setShowType(String type, boolean show)
    {

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
    public void setTypesUI(BKTypesInterface types)
    {

    }

    @Override
    public void setSelectedTagsUI(SelectedTagsInterface selectedTagsUI)
    {

    }

    @Override
    public void setAvailableTagsUI(AvailableTagsInterface availableTagsUI)
    {

    }

    @Override
    public void setBookmarksListUI(BookmarksListInterface bookmarksListUI)
    {
        this.bookmarks = bookmarksListUI;
    }

    @Override
    public void setSearchUI(SearchInterface searchUI)
    {

    }

    @Override
    public void setMenuUi(MenuInterface menuUI)
    {

    }

    @Override
    public void setQuickPanelUI(QuickPanelInterface quickPanelUI)
    {

    }

    @Override
    public BKTypesInterface getTypesUI()
    {
        return null;
    }

    @Override
    public SelectedTagsInterface getSelectedTagsUI()
    {
        return null;
    }

    @Override
    public AvailableTagsInterface getAvailableTagsUI()
    {
        return null;
    }

    @Override
    public BookmarksListInterface getBookmarksListUI()
    {
        return this.bookmarks;
    }

    @Override
    public SearchInterface getSearchUI()
    {
        return null;
    }

    @Override
    public MenuInterface getMenuUi()
    {
        return null;
    }

    @Override
    public QuickPanelInterface getQuickPanelUI()
    {
        return null;
    }

    @Override
    public List<AbstractBookmark> getVisibleBookmarks()
        throws Exception
    {
        List<AbstractBookmark> l = new ArrayList<>(this.bootstrap.getBkioInterface().getContext().getBookmarks());
        return l;
    }



    private AbstractBookmark getBookmarkUI(AbstractBookmark abstractBookmark)
    {
        return null;
    }
}
