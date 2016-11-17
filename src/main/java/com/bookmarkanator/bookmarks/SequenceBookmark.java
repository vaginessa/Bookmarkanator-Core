package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class SequenceBookmark extends AbstractBookmark
{
    private List<UUID> items;
    private List<UUID> errorItems;

    public SequenceBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
        items = new ArrayList<>();
        errorItems = new ArrayList<>();
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
    public void action(ContextInterface context)
        throws Exception
    {
        boolean error = false;
        StringBuilder sb = new StringBuilder();
        for (UUID uuid: items)
        {
            AbstractBookmark abs = context.getBookmark(uuid);
            if (abs==null)
            {
                errorItems.add(uuid);
                sb.append("[Error finding id \""+uuid+"\"]");
                error = true;
            }
            else
            {
                abs.action(context);
                sb.append(abs.getText());
            }
        }

        this.setText(sb.toString());

        if (error)
        {
            throw new Exception("One or more referenced bookmarks couldn't be found. Please check error bookmarks list for bookmark Id's.");
        }
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

}
