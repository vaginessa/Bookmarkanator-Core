package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class HTMLFormattedBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(HTMLFormattedBookmark.class.getCanonicalName());
    private String content;
    @Override
    public String getTypeName()
    {
        return "HTMLText";
    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("");
        return list;
    }

    @Override
    public void runAction()
        throws Exception
    {
        // Do nothing.
    }

    @Override
    public void destroy()
        throws Exception
    {
        // Do nothing.
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new HTMLFormattedBookmark();
    }


    @Override
    public String getContent() throws Exception
    {
        return content;
    }

    @Override
    public void setContent(String content)
        throws Exception
    {
        this.content = content;
    }

    @Override
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}