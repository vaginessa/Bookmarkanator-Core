package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.io.*;

public class FileBookmark extends AbstractBookmark
{
    public FileBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
    }

    @Override
    public String getTypeName()
    {
        return "File";
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
    public void destroy()
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
