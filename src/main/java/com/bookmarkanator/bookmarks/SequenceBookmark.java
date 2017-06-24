package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;
import org.apache.logging.log4j.*;

/**
 * A SequenceBookmark is a bookmark that contains a sequence of references to other bookmarks. It is merely an ordered container for other bookmarks.
 */
public class SequenceBookmark extends AbstractBookmark
{
    private static final Logger logger = LogManager.getLogger(SequenceBookmark.class.getCanonicalName());
    private static String secretKey;
    private List<UUID> items;
    private List<UUID> errorItems;

    //TODO: Use main UI as the search functionality of this addon window. Means it cannot be modal.

    public SequenceBookmark()
    {
        super();
        items = new ArrayList<>();
        errorItems = new ArrayList<>();
    }

    @Override
    public boolean setSecretKey(String secretKey)
    {
        if (SequenceBookmark.secretKey == null && secretKey != null)
        {
            SequenceBookmark.secretKey = secretKey;
            return true;
        }
        return false;
    }

    @Override
    public Set<String> getSearchWords()
    {
        return null;
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
        return "Sequence";
    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("Group");
        return list;
    }

    @Override
    public String runAction(String commandString)
        throws Exception
    {
        boolean error = false;
        StringBuilder sb = new StringBuilder();
        for (UUID uuid : items)
        {
            AbstractBookmark abs = Bootstrap.context().getBookmark(uuid);
            if (abs == null)
            {
                errorItems.add(uuid);
                sb.append("[Error finding id \"" + uuid + "\"]");
                error = true;
            }
            else
            {
                abs.runAction();
                sb.append(abs.getContent());
            }
        }

        if (error)
        {
            throw new Exception("One or more referenced bookmarks couldn't be found. Please check error bookmarks list for bookmark Id's.");
        }
        return sb.toString();
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
    public AbstractBookmark getNew()
    {
        return new SequenceBookmark();
    }

    @Override
    public HandleData canHandle(String content)
    {
        return null;
    }

    @Override
    public String getContent()
    {
        StringBuilder sb = new StringBuilder();

        for (int c = 0; c < items.size(); c++)
        {
            UUID bk = items.get(c);
            sb.append(bk.toString());

            if (c != items.size() - 1)
            {//don't add comma at last item.
                sb.append(",");
            }
        }

        return sb.toString();
    }

    @Override
    public void setContent(String content)
    {
        String[] strings = content.split(",");

        for (String s : strings)
        {
            s = s.trim();
            if (!s.isEmpty())
            {
                items.add(UUID.fromString(s));
            }
        }
    }

    public List<UUID> getErrorItems()
    {
        return Collections.unmodifiableList(errorItems);
    }

    public void resetErrors()
    {
        errorItems.clear();
    }

    public void addBookmark(UUID bookmark)
    {
        items.add(bookmark);
    }

    public void removeBookmark(UUID bookmarkID)
    {
        items.remove(bookmarkID);
    }

    public List<UUID> getBookmarks()
    {
        return Collections.unmodifiableList(items);
    }

    @Override
    public int compareTo(AbstractBookmark o)
    {
        //TODO IMPLEMENT
        return 0;
    }
}
