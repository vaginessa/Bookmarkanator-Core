package com.bookmarkanator.ui.defaultui.interfaces;

import java.util.*;
import com.bookmarkanator.bookmarks.*;

public interface BookmarksListInterface extends GUIItemInterface
{
    void setVisibleBookmarks(List<AbstractBookmark> bookmarks)
        throws Exception;
    List<AbstractBookmark> getVisibleBookmarks();
}
