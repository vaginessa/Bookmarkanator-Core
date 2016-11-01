package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class SequenceBookmark extends AbstractBookmark {
    private Map<UUID, AbstractBookmark> items;

    @Override
    public String getTypeName() {
        return "sequence";
    }

    @Override
    public void action(Context context) throws Exception {

    }

    @Override
    public String toXML() {
        return null;
    }

    @Override
    public void fromXML(String xml) {

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
