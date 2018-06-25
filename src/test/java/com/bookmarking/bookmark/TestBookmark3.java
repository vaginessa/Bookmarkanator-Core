package com.bookmarking.bookmark;

import java.util.*;

public class TestBookmark3 extends AbstractBookmark
{
    @Override
    public String[] runAction(String[]... actionStrings)
        throws Exception
    {
        return new String[0];
    }

    @Override
    public String[] getActions()
    {
        return new String[0];
    }

    @Override
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    public String getTypeName()
    {
        return null;
    }

    @Override
    public List<String> getGroupingLocation()
    {
        return null;
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
    public String getContent()
    {
        return null;
    }

    @Override
    public void setContent(String content)
        throws Exception
    {

    }

    @Override
    public Set<String> getSearchWords()
        throws Exception
    {
        return null;
    }

    @Override
    public void systemInit()
    {

    }

    @Override
    public void systemShuttingDown()
    {

    }

    @Override
    public boolean canConsume(String data)
    {
        return false;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}
