package com.bookmarkanator.bookmarks;

import java.util.*;
import com.bookmarkanator.core.*;

public class SequenceBookmark extends AbstractBookmark<List<AbstractBookmark>> {
    private Map<UUID, AbstractBookmark> items;

    @Override
    public String getTypeName() {
        return "sequence";
    }

    @Override
    public List<AbstractBookmark> action(Context context) {
        return getBookmarks();
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
