package com.bookmarkanator.ui.defaultui.interfaces;

import com.bookmarkanator.bookmarks.*;

public interface BookmarkUIInterface
{
    /**
     * Causes the bookmarkInterface to show a create bookmark interface. When done it returns the created bookmark.
     * @param config  The specific config object for this type of UI system.
     * @param toEdit  The bookmark to edit if editing.
     * @return  The new or edited bookmark.
     */
    BookmarkUIInterface newOrEdit(Object config, AbstractBookmark toEdit);

    /**
     * Gets the actual bookmark object this UI item references.
     * @return  The Actual bookmark object.
     */
    AbstractBookmark getBookmark();
}
