package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class TextBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TextBookmark.class.getCanonicalName());
    @Override
    public Set<String> getSearchWords()
    {
        Set<String> strings = new HashSet<>();
        for (String s: getText().split(" "))
        {
            strings.add(s);
        }
        return strings;
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
    public String getContent()
    {
        return null;
    }

    @Override
    public void setContent(String xml)
    {

    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}
