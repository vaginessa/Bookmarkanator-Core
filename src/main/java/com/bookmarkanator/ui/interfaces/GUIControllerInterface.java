package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.core.*;

public interface GUIControllerInterface
{

    Bootstrap getBootstrap();

    Settings getSettings();

    //Bookmarks

    /**
     * Gets a list of visible bookmarks with all the search terms selected. It takes into account the search term, show types fields, and selected tags.
     *
     * @return A list of visible bookmarks.
     * @throws Exception
     */
    Set<AbstractBookmark> getVisibleBookmarks();

    /**
     * Should be called after the interfaces are set so that the controller can set an initial state of the UI.
     *
     * @throws Exception
     */
    void initUI()
        throws Exception;

    void updateUI()
        throws Exception;

    //Search
    void setSearchTerm(String searchTerm)
        throws Exception;

    void setSearchInclusions(String key, boolean value)
        throws Exception;

    boolean getSearchInclusion(String key)
        throws Exception;

    String getSearchTerm();

    //Tags
    void setSelectedTags(Set<String> tags)
        throws Exception;

    void addSelectedTag(String tag)
        throws Exception;

    void removeSelectedTag(String tag)
        throws Exception;

    Set<String> getSelectedTags();

    Set<String> getAvailableTags();

    void setTagMode(String mode)
        throws Exception;

    String getTagMode();

    //Types
    void setShowType(String type, boolean show)
        throws Exception;

    void showTypes(Set<String> types)
        throws Exception;

    boolean toggleShowType(String type)
        throws Exception;

    Set<String> getVisibleTypes();

    Set<String> getShowOnlyTheseTypes();

    Set<String> getAllTypes();
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
