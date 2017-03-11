package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;
import javafx.scene.image.*;

public class TerminalBookmarkUI extends AbstractUIBookmark
{

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
        return null;
    }
}
