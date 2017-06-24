package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.core.*;
import com.bookmarkanator.ui.fxui.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;
import com.bookmarkanator.util.*;

public interface UIControllerInterface
{

    Settings getSettings();

    /**
     * Gets a list of visible bookmarks with all the search terms selected. It takes into account the search term, show types fields, and selected tags.
     *
     * @return A list of visible bookmarks.
     * @throws Exception
     */
    Set<AbstractUIBookmark> getVisibleUIBookmarks();

    /**
     * Should be called after the interfaces are set so that the controller can set an initial state of the UI.
     *
     * @throws Exception
     */
    void initUI()
        throws Exception;

    void updateUI()
        throws Exception;

    SearchOptions getSearchOptions();

    void setSearchOptions(SearchOptions searchOptions)
        throws Exception;

    SimilarItemIterator getSimilarItemIterator(String closeSearchTerm);

    //Available Bookmark Types Methods
    void toggleShowType(AbstractUIBookmark abstractUIBookmark)
        throws Exception;

    void showAllTypes()
        throws Exception;

    void hideAllTypes()
        throws Exception;

    //Selected Tags Methods
    void clearAllSelectedTagGroups()
        throws Exception;

    void setTagModeForCurrentGroup(String tagModeForCurrentGroup)
        throws Exception;

    void setCurrentGroup(SearchOptions.TagsInfo currentGroup)
        throws Exception;

    SearchOptions.TagsInfo getCurrentGroup();

    void addTagGroup()
        throws Exception;

    void removeTagGroup(SearchOptions.TagsInfo tagGroup)
        throws Exception;

    void removeTagFromGroup(SearchOptions.TagsInfo tagGroup, String tag)
        throws Exception;

    //Available Tags Methods
    void selectTag(String tag)
        throws Exception;//needs to add the tag only if it is in the list of existing tags.

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

    void setNewBookmarkSelectorUI(NewBookmarkSelectionInterface newBookmarkSelectorUI);

    BKTypesInterface getTypesUI();

    SelectedTagsInterface getSelectedTagsUI();

    AvailableTagsInterface getAvailableTagsUI();

    BookmarksListInterface getBookmarksListUI();

    SearchInterface getSearchUI();

    MenuInterface getMenuUi();

    QuickPanelInterface getQuickPanelUI();

    NewBookmarkSelectionInterface getNewBookmarkSelectorUI();

    boolean isEditMode();

    void setEditMode(boolean editMode);
}
