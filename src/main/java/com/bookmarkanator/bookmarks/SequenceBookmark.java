package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class SequenceBookmark extends AbstractBookmark
{
    private List<UUID> items;

    public SequenceBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
        items = new ArrayList<>();
    }

    @Override
    public String getTypeName()
    {
        return "sequence";
    }

    @Override
    public List<String> getTypeLocation()
    {
        List<String> list = new ArrayList<>();
        list.add("Group");
        return list;
    }

    @Override
    public void action(FileContext context)
        throws Exception
    {

    }

    @Override
    public AbstractBookmark getNew()
    {
        return new SequenceBookmark(null);
    }

    @Override
    public String getSettings()
    {
        StringBuilder sb = new StringBuilder();

        for (int c=0;c<items.size();c++)
        {
            UUID bk = items.get(c);
            sb.append(bk.toString());

            if (c!=items.size()-1)
            {//don't add comma at last item.
                sb.append(",");
            }
        }

        return sb.toString();
    }

    @Override
    public void setSettings(String settings)
    {
        String[] strings = settings.split(",");

        for (String s: strings)
        {
            s = s.trim();
            if (!s.isEmpty())
            {
                items.add(UUID.fromString(s));
            }
        }
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

}
