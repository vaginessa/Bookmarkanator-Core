package com.bookmarkanator.bookmarks;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HTMLFormattedBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(HTMLFormattedBookmark.class.getCanonicalName());
    private static String secretKey;
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
    public String runAction(String actionCommand)
        throws Exception
    {
        // Do nothing.
        return null;
    }

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (HTMLFormattedBookmark.secretKey == null && secretKey != null)
        {
            HTMLFormattedBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public void notifyBeforeAction(AbstractBookmark source, String actionString)
    {

    }

    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }

    @Override
    public void notifyAfterAction(AbstractBookmark source, String actionString)
    {

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
    }

    @Override
    public Set<String> getSearchWords()
        throws Exception
    {
        Set<String> strings = new HashSet<>();
        String content = Jsoup.parse(getContent()).text();
        if (content != null)
        {
            for (String s : content.split("[\\s\\\\\"'\\./-]"))
            {
                strings.add(s);
            }
        }
        return strings;
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
    public int compareTo(AbstractBookmark o)
    {
        return 0;
    }
}
