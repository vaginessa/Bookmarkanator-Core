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
    private Search<UUID> bookmarkNames;
    private Search<UUID> bookmarkTypeNames;//Such as text, web, terminal, mapping, whatever...
    private Search<UUID> bookmarkText;//The text the bookmark contains.
    private Search<UUID> bookmarkTags;
    private int numSearchResults;

    //Bookmarks can store data here and communicate with other bookmarks. They can read/write their own data, but only read the data of other bookmark types.
    private Map<String, Map<String, Object>> contextObject;//<Class name of bookmark, Map<Bookmark data key, bookmark data object>>

    public Context() {
        bookmarks = new HashMap<>();
        bookmarkNames = new Search<>();
        bookmarkTypeNames = new Search<>();
        bookmarkText = new Search<>();
        bookmarkTags = new Search<>();
        numSearchResults = 20;
    }

    public int getNumSearchResults()
    {
        return numSearchResults;
    }

    public void setNumSearchResults(int numSearchResults)
    {
        this.numSearchResults = numSearchResults;
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

    public List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds)
    {
        List<AbstractBookmark> bks = new ArrayList<>();

        for (UUID uuid: bookmarkIds)
        {
            AbstractBookmark ab = bookmarks.get(uuid);

            if (ab!=null)
            {
                bks.add(ab);
            }
        }

        return bks;
    }

    public void addAll(List<AbstractBookmark> bookmarks)
        throws Exception
    {
        for (AbstractBookmark abs: bookmarks)
        {
            addBookmark(abs);
        }
    }

    public void addBookmark(AbstractBookmark bookmark) throws Exception {

        if (bookmark.getId()==null)
        {
            throw new Exception("Bookmark ID must be set");
        }

        if (bookmarks.get(bookmark.getId())!=null)
        {
            throw new Exception("Id \""+bookmark.getId()+"\" already exists. If you are trying to modify a bookmark please use the update method.");
        }

        UUID id = bookmark.getId();

        bookmarkNames.add(id, bookmark.getName());
        bookmarkTypeNames.add(id, bookmark.getTypeName());
        bookmarkText.add(id, bookmark.getText());
        bookmarkTags.add(id, bookmark.getTags());

        bookmarks.put(bookmark.getId(), bookmark);
    }

    public AbstractBookmark removeBookmark(UUID bookmarkID)
    {
     AbstractBookmark abs = getBookmark(bookmarkID);

        if (abs!=null)
        {
            UUID id = abs.getId();

            bookmarks.remove(id);
            bookmarkNames.remove(id);
            bookmarkTypeNames.remove(id);
            bookmarkText.remove(id);
            bookmarkTags.remove(id);
        }

        return abs;
    }

    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        if (bookmark.getId()==null)
        {
            throw new Exception("Bookmark ID must be set");
        }

        if (bookmarks.get(bookmark)==null)
        {
            throw new Exception("Item \""+bookmark.getId().toString()+"\" not found.");
        }

        removeBookmark(bookmark.getId());

        addBookmark(bookmark);
    }

    // ============================================================
    // Searching and sorting Methods
    // ============================================================

    public List<AbstractBookmark> searchAll(String text)
    {
        LinkedHashSet<UUID> uuids = new LinkedHashSet<>();//Use a LinkedHashSet to eliminate duplicates in the list, but preserve result order.
        List<AbstractBookmark> res = new ArrayList<>();

        //Search priority:

        //If text matches bookmark name exactly.
        uuids.addAll(bookmarkNames.searchFullTextOnly(text));

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a tags full text.
            uuids.addAll(bookmarkTags.searchFullTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches the bookmark type name exactly
            uuids.addAll(bookmarkTypeNames.searchFullTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches the full text returned by a bookmark.
            uuids.addAll(bookmarkText.searchFullTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a word in multi word bookmark names
            uuids.addAll(bookmarkNames.searchWordsInTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a word in multi word tag names
            uuids.addAll(bookmarkTags.searchWordsInTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a word in a multi word bookmark type name.
            uuids.addAll(bookmarkTypeNames.searchWordsInTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a word in a multi word bookmark type name.
            uuids.addAll(bookmarkTypeNames.searchWordsInTextOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a substring of one of the bookmark name words rotated.
            uuids.addAll(bookmarkNames.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a substring of one of the tag words rotated.
            uuids.addAll(bookmarkTags.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a substring of one of the bookmark text words rotated.
            uuids.addAll(bookmarkText.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //If text matches a substring of one of the type names rotated.
            uuids.addAll(bookmarkTypeNames.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkNames.searchAll(text, getNumSearchResults()));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkTags.searchAll(text, getNumSearchResults()));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkTypeNames.searchAll(text, getNumSearchResults()));
        }

        if (uuids.size()<getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkText.searchAll(text, getNumSearchResults()));
        }

        res.addAll(getBookmarks(uuids));

        return res;
    }

    public List<AbstractBookmark> searchBookmarkNames(String text)
    {
    return null;
    }

    public List<AbstractBookmark> searchTags(String text)
    {
        return null;
    }

    public List<AbstractBookmark> searchBookmarkTypes(String text)
    {
        return null;
    }

    public List<AbstractBookmark> searchBookmarkText(String text)
    {
        return null;
    }

    public List<AbstractBookmark> filterByTags(List<AbstractBookmark> bookmarkList, Set<String> tags)
    {
        return null;
    }

    public List<AbstractBookmark> filterByBookmarkType(List<AbstractBookmark> bookmarkList, Set<String> bookmarkTypeNames)
    {
        return null;
    }

    public List<AbstractBookmark> filterByBookmarkType(Set<Class> bookmarkTypeClasses, List<AbstractBookmark> bookmarkList)
    {
        return null;
    }

    public List<AbstractBookmark> filterDate(List<AbstractBookmark> bookmarkList, Date before, Date after)
    {
        return null;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark names.
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    public List<AbstractBookmark> excludeBookmarkWithNamesContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark text.
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    public List<AbstractBookmark> excludeBookmarksContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark tags.
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    public List<AbstractBookmark> excludeBookmarkTagsContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        return null;
    }

    //TODO Add sort by functions below.
}
