package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class FileWatchBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(FileBookmark.class.getCanonicalName());
    private static String secretKey;
    private String content;

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (FileWatchBookmark.secretKey == null && secretKey != null)
        {
            FileWatchBookmark.secretKey = secretKey;
            return true;
        }
        return false;
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
    protected String runTheAction(String action)
        throws Exception
    {
        return null;
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
        throws Exception
    {
        return null;
    }

    @Override
    public void setContent(String content)
        throws Exception
    {
        super.setContent();
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
        // Start watcher thread
    }

    @Override
    public void systemShuttingDown()
    {
        // Kill thread
    }

    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}
