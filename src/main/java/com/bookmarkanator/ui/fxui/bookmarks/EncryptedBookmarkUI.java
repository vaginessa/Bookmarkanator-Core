package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;

public class EncryptedBookmarkUI extends AbstractUIBookmark
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

    @Override
    public Object action()
    {
        return null;
    }

    @Override
    public Object action(Object config)
    {
        return null;
    }
}
