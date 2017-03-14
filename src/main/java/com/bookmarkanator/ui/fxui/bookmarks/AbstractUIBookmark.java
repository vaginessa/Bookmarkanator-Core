package com.bookmarkanator.ui.fxui.bookmarks;

import com.bookmarkanator.bookmarks.*;
import javafx.scene.image.*;

public abstract class AbstractUIBookmark
{
    private AbstractBookmark abstractBookmark;

    public void setBookmark(AbstractBookmark abstractBookmark)
    {
        this.abstractBookmark = abstractBookmark;
    }

    public AbstractBookmark getBookmark()
    {
        return this.abstractBookmark;
    }

    public abstract Image getTypeIcon();

    public abstract void show()throws Exception;

    public abstract AbstractBookmark edit()
        throws Exception;

    public abstract void delete() throws Exception;

    public abstract AbstractBookmark newBookmarkView()
        throws Exception;

    /**
     * Using class name is the name that this bookmark requires
     * @return
     */
    public abstract String getUsingClassName();
}
