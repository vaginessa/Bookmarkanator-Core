package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.util.*;

/**
 * The Context is a bookmark specific context. It allows searching and sorting of bookmarks. It allows reading and writing of data for the bookmarks.
 * and it also allows the bookmark methods to be called that will prepare the data they will use.
 */
public class Context {
    private Map<UUID, AbstractBookmark> bookmarks;
    private Search bookmarkNames;
    private Search bookmarkTypeNames;//Such as text, web, terminal, mapping, whatever...
    private Search bookmarkText;//The text the bookmark contains.
    private Search bookmarkTags;

    //Bookmarks can store data here and communicate with other bookmarks. They can read/write their own data, but only read the data of other bookmark types.
    private Map<String, Map<String, Object>> contextObject;//<Class name of bookmark, Map<Bookmark data key, bookmark data object>>

    public Context() {
        bookmarks = new HashMap<>();
    }

    // ============================================================
    // Bookmark Methods
    // ============================================================
    public Set<UUID> getBookmarkIDs()
    {
        return Collections.unmodifiableSet(bookmarks.keySet());
    }

    public Set<AbstractBookmark> getBookmarks()
    {
        Set<AbstractBookmark> bks = new HashSet<>();
        for (UUID uuid: bookmarks.keySet())
        {
            bks.add(bookmarks.get(uuid));
        }

        return Collections.unmodifiableSet(bks);
    }

    public AbstractBookmark getBookmark(UUID uuid)
    {
        return bookmarks.get(uuid);
    }

    public void setBookmark(AbstractBookmark bookmark) throws Exception {

        if (bookmark.getId()==null)
        {
            throw new Exception("Bookmark ID must be set");
        }

        String id = bookmark.getId().toString();




        bookmarks.put(bookmark.getId(), bookmark);
    }

    public AbstractBookmark removeBookmark(UUID bookmarkID)
    {
     AbstractBookmark abs = getBookmark(bookmarkID);

        if (abs!=null)
        {

        }

        return abs;
    }

    private void addItems(TextAssociator textAssociator, Set<String> words, String id)
    {
        for (String st: words)
        {
            textAssociator.add(id, st);
        }
    }

    // ============================================================
    // Searching and sorting Methods
    // ============================================================

    public List<AbstractBookmark> search(String text)
    {
        List<AbstractBookmark> res = new ArrayList<>();
        //Search order:
        //
        //Bookmark whole name matches
        //Tag whole name matches
        //Bookmark name words matches
        //Tag partial name matches
        //Bookmark


//        private TextAssociator tagsMap;//searching for tags
//        private TextAssociator bookmarkWholeNames;//searching for words in bookmark names
//        private TextAssociator bookmarkNameWords;//searching for words in bookmark names
//        private TextAssociator bookmarkTextWords;//searching for words in the text of bookmarks.
//        private TextAssociator partialTagsMap;//all tag substrings
//        private TextAssociator partialNameWordsMap;//all bookmark name substrings.
//        private TextAssociator partialTextWordsMap;//all bookmark text substrings.


        return res;
    }

}
