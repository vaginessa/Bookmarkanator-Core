package com.bookmarkanator.ui.defaultui.bookmarks;

import com.bookmarkanator.bookmarks.*;

public abstract class AbstractUIBookmark<T, L, B>
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

    public abstract T getTypeView();

    public abstract L getBookmarkListItemView();

    public abstract B getBookmarkView(AbstractBookmark abstractBookmark, boolean viewOnly);
}
