package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;
import javafx.scene.image.*;

public class EncryptedBookmarkUI extends AbstractUIBookmark
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
    public AbstractBookmark newBookmarkView()
        throws Exception
    {
        return null;
    }

    @Override
    public String getUsingClassName()
    {
        return EncryptedBookmark.class.getCanonicalName();
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
}
