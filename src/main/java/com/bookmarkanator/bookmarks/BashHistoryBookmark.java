package com.bookmarkanator.bookmarks;

import java.util.*;

/**
 * The idea behind this bookmark is to be able to record all bash history in a sorted unique manner. to make it easier to search. Also it should allow
 * one to somehow indicate one is starting and stopping work on a certain project so the bash history can be correlated with the task.
 */
public class BashHistoryBookmark extends AbstractBookmark
{
//    public BashHistoryBookmark(ContextInterface contextInterface)
//    {
//        super(contextInterface);
//    }

    @Override
    public String getTypeName()
    {
        return "BashHistory";
    }

    @Override
    public List<String> getTypeLocation()
    {
        return null;
    }

    @Override
    public void runAction()
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
