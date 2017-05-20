package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class TerminalBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TerminalBookmark.class.getCanonicalName());

    @Override
    public Set<String> getSearchWords()
    {
        return null;
    }

    @Override
    public String getTypeName()
    {
        return "Terminal";
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
    public void setContent(String content)
    {
        //Parse format here into the command required.
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}
