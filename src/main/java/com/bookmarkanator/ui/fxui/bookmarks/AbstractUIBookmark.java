package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import javafx.scene.image.*;

public abstract class AbstractUIBookmark
{
    private AbstractBookmark abstractBookmark;
    private ContextInterface context;

    public AbstractUIBookmark()
    {
    }

    public AbstractUIBookmark(ContextInterface context)
    {
        this.context = context;
    }

    public void setBookmark(AbstractBookmark abstractBookmark)
    {
        this.abstractBookmark = abstractBookmark;
    }

    public AbstractBookmark getBookmark()
    {
        return this.abstractBookmark;
    }

    public ContextInterface getContext()
    {
        return context;
    }

    // ============================================================
    //
    // Abstract Methods
    // ============================================================

    public abstract Image getTypeIcon();

    public abstract void show()throws Exception;

    public abstract AbstractBookmark edit()
        throws Exception;

    public abstract void delete() throws Exception;

    public abstract AbstractBookmark newBookmarkView()
        throws Exception;

    /**
     * Using class name is the name that this bookmark requires
     * @return  The name of the bookmark that this UI element needs to use.
     */
    public abstract String getRequiredBookmarkClassName();
}
