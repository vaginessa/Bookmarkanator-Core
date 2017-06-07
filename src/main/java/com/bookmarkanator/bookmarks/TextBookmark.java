package com.bookmarkanator.bookmarks;

import java.util.*;
import org.apache.logging.log4j.*;

public class TextBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(TextBookmark.class.getCanonicalName());
    private String content;

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
    public String getTypeName()
    {
        return "Text";
    }

    @Override
    public String runAction(String actionCommand)
        throws Exception
    {
        //do nothing
        return null;
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
