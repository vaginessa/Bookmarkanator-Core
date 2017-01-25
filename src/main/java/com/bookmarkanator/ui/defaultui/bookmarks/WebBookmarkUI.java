package com.bookmarkanator.ui.defaultui.bookmarks;

import com.bookmarkanator.bookmarks.*;

public class WebBookmarkUI extends AbstractUIBookmark
{

    @Override
    public Object getTypeView()
    {
        return this.getBookmark().getTypeName();
    }

    @Override
    public Object getBookmarkListItemView()
    {
        return this.getBookmark().getName();
    }

    @Override
    public Object getBookmarkView(AbstractBookmark abstractBookmark, boolean viewOnly)
    {
        return null;
    }
}
