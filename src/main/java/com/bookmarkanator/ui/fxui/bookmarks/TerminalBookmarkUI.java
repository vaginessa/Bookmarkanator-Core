package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import javafx.scene.image.*;

public class TerminalBookmarkUI extends AbstractUIBookmark
{
    public TerminalBookmarkUI()
    {
    }

    public TerminalBookmarkUI(ContextInterface context)
    {
        super(context);
    }

    @Override
    public Image getTypeIcon()
    {
        return null;
    }

    @Override
    public void show()
        throws Exception
    {
        this.getBookmark().action();
    }

    @Override
    public AbstractBookmark edit()
        throws Exception
    {
        return null;
    }

    @Override
    public void delete()
        throws Exception
    {

    }

    @Override
    public AbstractBookmark newBookmarkView()
        throws Exception
    {
        System.out.println(this.getClass().getCanonicalName());
        return null;
    }

    @Override
    public String getRequiredBookmarkClassName()
    {
        return TerminalBookmark.class.getCanonicalName();
    }
}
