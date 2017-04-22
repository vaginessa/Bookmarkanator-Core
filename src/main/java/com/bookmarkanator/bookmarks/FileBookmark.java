package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class FileBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(FileBookmark.class.getCanonicalName());
    @Override
    public Set<String> getSearchWords()
    {
        return null;
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
        return 0;
    }
}
