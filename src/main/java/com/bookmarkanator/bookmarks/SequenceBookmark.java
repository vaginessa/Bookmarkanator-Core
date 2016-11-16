package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class SequenceBookmark extends AbstractBookmark
{
    private Map<UUID, AbstractBookmark> items;

    public SequenceBookmark(ContextInterface contextInterface)
    {
        super(contextInterface);
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
        return "<bk-sequence>This item in a list!</bk-sequence>";
    }

    @Override
    public void setSettings(String xml)
    {

    }

    public void addBookmark(AbstractBookmark bookmark)
    {
        items.put(bookmark.getId(), bookmark);
    }

    public void removeBookmark(UUID bookmarkID)
    {
        items.remove(bookmarkID);
    }

    public List<AbstractBookmark> getBookmarks()
    {
        return Collections.unmodifiableList(new ArrayList(items.values()));
    }

}
