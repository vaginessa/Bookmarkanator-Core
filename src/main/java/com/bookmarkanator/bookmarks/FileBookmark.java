package com.bookmarkanator.bookmarks;

import java.util.*;

public class FileBookmark extends AbstractBookmark
{

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
