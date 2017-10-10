package com.bookmarking.bookmark;

import java.util.*;
import org.apache.logging.log4j.*;

public class TestBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TestBookmark.class.getCanonicalName());
    private static String secretKey;
    private String content;
    private Set<String> searchWords;

    public TestBookmark()
    {
        searchWords = new HashSet<>();
    }

    @Override
    public boolean setMessageBoardKey(String messageBoardKey)
    {
        if (TestBookmark.secretKey == null && messageBoardKey != null)
        {
            TestBookmark.secretKey = messageBoardKey;
            return true;
        }
        return false;
    }

    @Override
    public String runAction(String actionString)
        throws Exception
    {
        logger.info("Running action "+actionString);
        return "Ran action "+actionString;
    }

    @Override
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {
        logger.info("Before action notification");
    }

    @Override
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {
        logger.info("After action notification");
    }

    @Override
    public String getTypeName()
    {
        return "Console Out Bookmark";
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
        logger.info("Destroy bookmark action");
    }

    @Override
    public AbstractBookmark getNew()
    {
        return new TestBookmark();
    }

    @Override
    public String getContent()
        throws Exception
    {
        return content;
    }

    @Override
    public void setContent(String content)
        throws Exception
    {
        this.content = content;

        for (String s:this.content.split(" ") )
        {
            searchWords.add(s);
        }
    }

    @Override
    public Set<String> getSearchWords()
        throws Exception
    {
       return this.searchWords;
    }

    @Override
    public void systemInit()
    {
        logger.info("System use");
    }

    @Override
    public void systemShuttingDown()
    {
        logger.info("System shutting down.");
    }

    @Override
    public boolean canConsume(String data)
    {
        return false;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        return getId().compareTo(o.getId());
    }
}
