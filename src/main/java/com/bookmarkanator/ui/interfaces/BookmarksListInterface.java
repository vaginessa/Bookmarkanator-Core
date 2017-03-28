package com.bookmarkanator.ui.interfaces;

import java.util.*;
import com.bookmarkanator.ui.fxui.bookmarks.*;

public interface BookmarksListInterface extends UIItemInterface
{
    void setVisibleBookmarks(Set<AbstractUIBookmark> bookmarks)
        throws Exception;
}
