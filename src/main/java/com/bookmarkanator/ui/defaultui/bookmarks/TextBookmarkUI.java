package com.bookmarkanator.ui.defaultui.bookmarks;

import com.bookmarkanator.bookmarks.*;

public class TextBookmarkUI extends AbstractUIBookmark
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

        return this.getBookmark().getText();
    }

    @Override
    public Object action(Object config)
    {
        return null;
    }
}
