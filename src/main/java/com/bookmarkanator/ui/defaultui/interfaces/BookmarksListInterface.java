package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;

public interface BookmarksListInterface extends GUIItemInterface
{
    void setBookmarksList(List<BookmarkUIInterface> bookmarks);
    List<BookmarkUIInterface> getBookmarksList();
}
