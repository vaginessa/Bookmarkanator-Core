package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public interface BookmarksListInterface extends GUIItemInterface
{
    void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception;
    Set<AbstractBookmark> getVisibleBookmarks();
}
