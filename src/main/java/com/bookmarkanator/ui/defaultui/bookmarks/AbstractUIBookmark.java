package com.bookmarkanator.ui.defaultui.bookmarks;

import com.bookmarkanator.bookmarks.*;

public abstract class AbstractUIBookmark
{
    private AbstractBookmark abstractBookmark;

    public AbstractBookmark getBookmark()
    {
        return this.abstractBookmark;
    }

    public void setAbstractBookmark(AbstractBookmark abstractBookmark)
    {
        this.abstractBookmark = abstractBookmark;
    }

    public abstract Object getTypeView();

    public abstract Object getBookmarkListItemView();

    public abstract Object getBookmarkWindow(AbstractBookmark abstractBookmark, boolean viewOnly);
}
