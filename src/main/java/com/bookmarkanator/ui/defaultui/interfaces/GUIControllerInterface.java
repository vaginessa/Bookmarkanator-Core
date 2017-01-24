package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;

public interface GUIControllerInterface
{
    //Core
    void setBootstrap(Bootstrap bootstrap);
    Bootstrap getBootstrap();

    Settings getSettings();

    //Bookmarks

    /**
     * Gets a list of visible bookmarks with all the search terms selected. It takes into account the search term, show types fields, and selected tags.
     * @return  A list of visible bookmarks.
     * @throws Exception
     */
    List<AbstractBookmark> getVisibleBookmarks()
        throws Exception;

    /**
     * Should be called after the interfaces are set so that the controller can set an initial state of the UI.
     * @throws Exception
     */
    void initUI()
        throws Exception;

    //Search
    void setSearchTerm(String searchTerm);
    void setSearchInclusions(String key, boolean value);

    boolean getSearchInclusion(String key);
    String getSearchTerm();

    //Tags
    void setSelectedTags(List<String> tags);

    Set<String> getSelectedTags();
    Set<String> getAvailableTags();

    //Types
    void setShowType(String type, boolean show);
    Set<String> getTypes();

    // ============================================================
    // Interface Getter and Setter Methods
    // ============================================================

    void setTypesUI(BKTypesInterface types);
    void setSelectedTagsUI(SelectedTagsInterface selectedTagsUI);
    void setAvailableTagsUI(AvailableTagsInterface availableTagsUI);
    void setBookmarksListUI(BookmarksListInterface bookmarksListUI);
    void setSearchUI(SearchInterface searchUI);
    void setMenuUi(MenuInterface menuUI);
    void setQuickPanelUI(QuickPanelInterface quickPanelUI);

    BKTypesInterface getTypesUI();
    SelectedTagsInterface getSelectedTagsUI();
    AvailableTagsInterface getAvailableTagsUI();
    BookmarksListInterface getBookmarksListUI();
    SearchInterface getSearchUI();
    MenuInterface getMenuUi();
    QuickPanelInterface getQuickPanelUI();

}
