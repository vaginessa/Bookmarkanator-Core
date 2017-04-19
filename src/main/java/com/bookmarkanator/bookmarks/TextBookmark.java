package com.bookmarkanator.bookmarks;

import java.util.*;

public class TextBookmark extends AbstractBookmark
{

    @Override
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public String getTypeName()
    {
        return "Text";
    }

    @Override
    public void runAction()
        throws Exception
    {
        //do nothing
//        System.out.println(this.getData());
    }

    @Override
    public void destroy()
        throws Exception
    {

    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("");
        return list;
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new TextBookmark();
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
        //TODO IMPLEMENT
        return 0;
    }
}
