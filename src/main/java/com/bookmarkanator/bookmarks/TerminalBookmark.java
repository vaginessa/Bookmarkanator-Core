package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.io.*;

public class TerminalBookmark extends AbstractBookmark
{
    /**
     * Bookmarks are required to have a constructor that accepts a ContextInterface because it allows extending classes the opportunity to implement
     * custom behaviour upon being initialized.
     *
     * @param contextInterface The Bookmark context object for the custom bookmark to use.
     */
    public TerminalBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    @Override
    public String getTypeName()
    {
        return null;
    }

    @Override
    public List<String> getTypeLocation()
    {
        return null;
    }

    @Override
    public void action()
        throws Exception
    {

    }

    @Override
    public AbstractBookmark getNew()
    {
        return null;
    }

    @Override
    public String getSettings()
    {
        return null;
    }

    @Override
    public void setSettings(String xml)
    {

    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}
