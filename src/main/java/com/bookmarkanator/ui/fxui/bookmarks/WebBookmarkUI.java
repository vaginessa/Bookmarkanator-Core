package com.bookmarkanator.ui.fxui.bookmarks;

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

    @Override
    public Object action()
        throws Exception
    {
        this.getBookmark().action();
        return null;
    }

    @Override
    public Object action(Object config)
        throws Exception
    {
        this.action();
        return null;
    }
}
