package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public interface BookmarksListInterface extends GUIItemInterface
{
    void setVisibleBookmarks(Set<AbstractBookmark> bookmarks)
        throws Exception;
    Set<AbstractBookmark> getVisibleBookmarks();
}
