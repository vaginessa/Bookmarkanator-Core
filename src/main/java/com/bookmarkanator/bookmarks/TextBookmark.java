package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class TextBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TextBookmark.class.getCanonicalName());
    private static String secretKey;
    private String content;

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (TextBookmark.secretKey == null && secretKey!=null)
        {
            TextBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }
    @Override
    public Set<String> getSearchWords()
    {
        Set<String> strings = new HashSet<>();
        String content = getContent();
        if (content!=null)
        {
            for (String s : getContent().split("[\\s\\\\\"'\\./-]"))
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
    public HandleData canHandle(String content) {
        return null;
    }

    @Override
    public String getTypeName()
    {
        return "Text";
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
        return content;
    }

    @Override
    public void setContent(String content)
    {
        this.content = content;
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}
