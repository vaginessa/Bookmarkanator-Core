package com.bookmarkanator.io;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.util.*;
import org.apache.logging.log4j.*;

/**
 * This class represents the main interface for interacting with bookmarks that are loaded/saved to/from the file system.
 * When loading/laving the bookmarks will be written/read in xml format. Each bookmark has a section of text within the xml
 * that it can save to (in any format), but is also responsible for parsing when the bookmark is loaded.
 */
public class FileContext implements ContextInterface
{
    private static final Logger logger = LogManager.getLogger(FileContext.class.getCanonicalName());
    private Map<UUID, AbstractBookmark> bookmarks;
    private Search<UUID> bookmarkNames;
    private Search<UUID> bookmarkTypeNames;//Such as text, web, terminal, mapping, whatever...
//    private Search<UUID> bookmarkText;//The text the bookmark contains.
    private Map<String,Set<UUID> > fullTextSearchMap;//The map used to search only words of the bookmarks.
    private Search<UUID> bookmarkTags;
    private int numSearchResults;//How many search results to return.
    private BKIOInterface bkioInterface;// Currently this is FileIo, but it could be any interface. For instance it could point to a database, or web service.
    private boolean isDirty;
    private boolean alwaysClean;

    public FileContext()
    {
        bookmarks = new HashMap<>();
        bookmarkNames = new Search<>();
        bookmarkTypeNames = new Search<>();
//        bookmarkText = new Search<>();
        bookmarkTags = new Search<>();
        fullTextSearchMap = new HashMap<>();
        numSearchResults = 20;
    }

    // ============================================================
    // Bookmark Methods
    // ============================================================

    @Override
    public void setBKIOInterface(BKIOInterface bkioInterface)
    {
        this.bkioInterface = bkioInterface;
    }

    @Override
    public BKIOInterface getBKIOInterface()
    {
        return this.bkioInterface;
    }

    @Override
    public Set<UUID> getBookmarkIDs()
    {
        return Collections.unmodifiableSet(bookmarks.keySet());
    }

    @Override
    public Set<AbstractBookmark> getBookmarks()
    {
        Set<AbstractBookmark> bks = new HashSet<>();
        for (UUID uuid : bookmarks.keySet())
        {
            bks.add(bookmarks.get(uuid));
        }

        return Collections.unmodifiableSet(bks);
    }

    /**
     * Gets a single bookmark.
     *
     * @param uuid The Id of the bookmark
     * @return A bookmark with this id or null.
     */
    @Override
    public AbstractBookmark getBookmark(UUID uuid)
    {
        return bookmarks.get(uuid);
    }

    /**
     * Gets a set of bookmarks that match the supplied bookmark Id's
     *
     * @param bookmarkIds A set of bookmark Id's
     * @return A list of bookmarks (Note: returning a list in case a linkedhashset is used that preserves insertion order. In this case it will return a
     * list with the supplied order preserved).
     */
    @Override
    public List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds)
    {
        List<AbstractBookmark> bks = new ArrayList<>();

        for (UUID uuid : bookmarkIds)
        {
            AbstractBookmark ab = bookmarks.get(uuid);

            if (ab != null)
            {
                bks.add(ab);
            }
            else
            {
                System.out.println("Warning bookmark " + uuid + " cannot be found.");
            }
        }

        return bks;
    }

    /**
     * Add the list of bookmarks to this list of bookmarks.
     *
     * @param bookmarks The list to add.
     * @throws Exception if a bookmark Id is null, or it already exists.
     */
    @Override
    public void addAll(List<AbstractBookmark> bookmarks)
        throws Exception
    {
        for (AbstractBookmark abs : bookmarks)
        {
            addBookmark(abs);
        }
    }

    /**
     * Adds a bookmark to the list of bookmarks.
     *
     * @param bookmark The bookmark to add.
     * @throws Exception if a bookmark Id is null, or it already exists.
     */
    @Override
    public void addBookmark(AbstractBookmark bookmark)
        throws Exception
    {

        if (bookmark.getId() == null)
        {
            throw new Exception("Bookmark ID must be set");
        }

        if (bookmarks.get(bookmark.getId()) != null)
        {
            throw new Exception(
                "Id \"" + bookmark.getId() + "\" already exists. If you are trying to modify a bookmark please use the update method.");
        }

        setDirty();

        UUID id = bookmark.getId();

        bookmarkNames.add(id, bookmark.getName());
        bookmarkTypeNames.add(id, bookmark.getTypeName());
        bookmarkTags.add(id, bookmark.getTags());

        // Adding the text of bookmarks.
        Set<String> strings = bookmark.getSearchWords();

        if (strings!=null)
        {
            for (String s : strings)
            {
                s = s.toLowerCase();
                s = s.trim();

                if (!s.isEmpty())
                {
                    Set<UUID> uuids = fullTextSearchMap.get(s);
                    if (uuids == null)
                    {
                        uuids = new HashSet<>();
                        fullTextSearchMap.put(s, uuids);
                    }

                    uuids.add(id);
                }
            }
        }

        bookmarks.put(bookmark.getId(), bookmark);
    }

    /**
     * Remove a bookmark. This removes the bookmark from the list of bookmarks and all searching methods.
     *
     * @param bookmarkID The Id of the bookmark to remove.
     * @return The removed bookmark.
     */
    @Override
    public AbstractBookmark delete(UUID bookmarkID)
    {
        AbstractBookmark abs = getBookmark(bookmarkID);

        if (abs != null)
        {
            UUID id = abs.getId();

            bookmarks.remove(id);
            bookmarkNames.remove(id);
            bookmarkTypeNames.remove(id);
//            bookmarkText.remove(id);
            bookmarkTags.remove(id);

            setDirty();

            return abs;
        }

        return null;
    }

    @Override
    public List<AbstractBookmark> deleteAll(Set<UUID> bookmarks)
    {
        List<AbstractBookmark> bks = getBookmarks(bookmarks);
        List<AbstractBookmark> res = new ArrayList<>();

        for (AbstractBookmark bookmark : bks)
        {
            bks.add(delete(bookmark.getId()));
        }
        return res;
    }

    /**
     * Updates a bookmark.
     *
     * @param bookmark The bookmark to edit/update
     * @throws Exception if the bookmark Id is null, or the bookmark cannot be found.
     */
    @Override
    public void updateBookmark(AbstractBookmark bookmark)
        throws Exception
    {
        if (bookmark == null || bookmark.getId() == null)
        {
            //            throw new Exception("Bookmark must be non null and ID must be set");
            System.out.println("Supplied bookmark, or it's Id field is");
            return;
        }

        if (getBookmark(bookmark.getId()) == null)
        {
            //            throw new Exception("Item \"" + bookmark.getId().toString() + "\" not found.");
            addBookmark(bookmark);
        }
        else
        {
            delete(bookmark.getId());
            addBookmark(bookmark);
        }

        setDirty();
    }

    // ============================================================
    // Searching Methods
    // ============================================================

    /**
     * @return The number of search results for the search functions to try and return.
     */
    @Override
    public int getNumSearchResults()
    {
        return numSearchResults;
    }

    /**
     * Set the number of search results for the search functions to try and return.
     *
     * @param numSearchResults the number of search results to return if available.
     */
    @Override
    public void setNumSearchResults(int numSearchResults)
    {
        this.numSearchResults = numSearchResults;
    }

    /**
     * This is a general search function that will search all aspects of a bookmark (name, tag names, text, and derivatinos of all those as well)
     *
     * @param text The text to search for.
     * @return The list of results (in order of importance) that the search matched.
     */
    @Override
    public List<AbstractBookmark> searchAll(String text)
    {
        LinkedHashSet<UUID> uuids = new LinkedHashSet<>();//Use a LinkedHashSet to eliminate duplicates in the list, but preserve result order.
        List<AbstractBookmark> res = new ArrayList<>();

        //Search priority:

        //If text matches bookmark name exactly.
        uuids.addAll(bookmarkNames.searchFullTextOnly(text));

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a tags full text.
            uuids.addAll(bookmarkTags.searchFullTextOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches the bookmark type name exactly
            uuids.addAll(bookmarkTypeNames.searchFullTextOnly(text));
        }

//        if (uuids.size() < getNumSearchResults())
//        {
//            //If text matches the full text returned by a bookmark.
//            uuids.addAll(bookmarkText.searchFullTextOnly(text));
//        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a word in multi word bookmark names
            uuids.addAll(bookmarkNames.searchWordsInTextOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a word in multi word tag names
            uuids.addAll(bookmarkTags.searchWordsInTextOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a word in a multi word bookmark type name.
            uuids.addAll(bookmarkTypeNames.searchWordsInTextOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a word in a multi word bookmark type name.
            uuids.addAll(bookmarkTypeNames.searchWordsInTextOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a substring of one of the bookmark name words rotated.
            uuids.addAll(bookmarkNames.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a substring of one of the tag words rotated.
            uuids.addAll(bookmarkTags.searchSubstringsOfRotatedWordsOnly(text));
        }

//        if (uuids.size() < getNumSearchResults())
//        {
//            //If text matches a substring of one of the bookmark text words rotated.
//            uuids.addAll(bookmarkText.searchSubstringsOfRotatedWordsOnly(text));
//        }

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a substring of one of the type names rotated.
            uuids.addAll(bookmarkTypeNames.searchSubstringsOfRotatedWordsOnly(text));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkNames.searchAll(text, getNumSearchResults()));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkTags.searchAll(text, getNumSearchResults()));
        }

        if (uuids.size() < getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkTypeNames.searchAll(text, getNumSearchResults()));
        }

//        if (uuids.size() < getNumSearchResults())
//        {
//            //General search because nothing has enough results yet.
//            uuids.addAll(bookmarkText.searchAll(text, getNumSearchResults()));
//        }
        if (uuids.size()<getNumSearchResults())
        {
            // Search bookmarks text because not enough is found yet.
            res.addAll(searchBookmarkText(text));
        }


        res.addAll(getBookmarks(uuids));

        return res;
    }

    /**
     * Search the names of bookmarks (and all there derivations)
     *
     * @param text The text to search for.
     * @return The results of the search.
     */
    @Override
    public List<AbstractBookmark> searchBookmarkNames(String text)
    {
        return getBookmarks(bookmarkNames.searchAll(text, getNumSearchResults()));
    }

    /**
     * This method searches the bookmark tags for any tags containing the text (or a derivation of the text).
     *
     * @param text The text to do a general tag search for
     * @return A list of bookmarks that contain the text in their tags.
     */
    @Override
    public List<AbstractBookmark> searchTagsLoosly(String text)
    {
        return getBookmarks(bookmarkTags.searchAll(text, getNumSearchResults()));
    }

    /**
     * Find all bookmarks that have one of the supplied tags matching exactly one of the bookmark tags.
     *
     * @param tags The tags to match at least one of.
     * @return A list of Bookmarks that contain at least one of the supplied tags.
     */
    @Override
    public List<AbstractBookmark> searchTagsExact(Set<String> tags)
    {
        return getBookmarks(searchAllTagsExact(tags));
    }

    /**
     * This method locates the Id's of any bookmarks that have tags that match any of the supplied tags. If a bookmark as at least one of the
     * supplied tags it will be added to the results.
     *
     * @param tags The tags to search for.
     * @return The set of bookmark Id's where the tags were found.
     */
    private Set<UUID> searchAllTagsExact(Set<String> tags)
    {
        Set<UUID> uuids = new HashSet<>();

        for (String s : tags)
        {
            Set<UUID> u = bookmarkTags.searchFullTextOnly(s);
            if (u != null)
            {
                uuids.addAll(u);
            }
        }

        return uuids;
    }

    /**
     * Locates all bookmarks that have the whole set of supplied tags. If a bookmark doesnt' have all the tags sent in it will not make it in the list
     * of search results.
     *
     * @param tags The list of tags to use to find bookmarks.
     * @return A list of bookmarks that contain all the supplied tags.
     */
    @Override
    public List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception
    {
        Set<UUID> uuids = searchAllTagsExact(tags);
        Set<UUID> results = new HashSet<>();

        for (UUID uuid : uuids)
        {
            AbstractBookmark abs = bookmarks.get(uuid);

            if (abs == null)
            {
                throw new Exception("A bookmark was found in a tag search but is not in the list of bookmarks.");
            }

            if (abs.getTags() != null && !abs.getTags().isEmpty() && abs.getTags().containsAll(tags))
            {
                results.add(uuid);
            }
        }

        return getBookmarks(results);
    }

    /**
     * Searches the bookmark type names for any names that loosely the searched for text.
     *
     * @param text The text to search for.
     * @return A list of boomarks that have the supplied text (or a portion of it) in their type names.
     */
    @Override
    public List<AbstractBookmark> searchBookmarkTypes(String text)
    {
        return getBookmarks(bookmarkTypeNames.searchAll(text, getNumSearchResults()));
    }

    /**
     * Loosely searches all the text of the bookmarks to locate any bookmark that contains the supplied text.
     *
     * @param text The text to search for.
     * @return A list of bookmarks that contain the supplied text.
     */
    @Override
    public List<AbstractBookmark> searchBookmarkText(String text)
    {
        text = text.toLowerCase();
        List<AbstractBookmark> res = new ArrayList<>();

        Set<UUID>  ids = fullTextSearchMap.get(text);

        if (ids!=null)
        {
            res.addAll(getBookmarks(ids));
        }

        return res;
    }

    @Override
    public boolean isDirty()
    {
        return this.isDirty;
    }

    @Override
    public void setAlwaysClean(boolean alwaysClean)
    {
        this.alwaysClean = alwaysClean;
    }

    private void setDirty()
    {
        if (!alwaysClean)
        {
            isDirty = true;
        }
    }

    @Override
    public String bookmarksReport(boolean includeTags, int limit)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("***Bookmarks Report***");
        sb.append("\n\n");

        if (includeTags)
        {
            sb.append(bookmarksToStringWTags(getBookmarks(), limit));
        }
        else
        {
            sb.append(bookmarksToString(getBookmarks(), limit));
        }
        return sb.toString();
    }

    private String bookmarksToString(Set<AbstractBookmark> bookmarks, int limit)
    {
        StringBuilder sb = new StringBuilder();
        int c = 0;
        for (AbstractBookmark bookmark : bookmarks)
        {
            if (limit > -1 && c >= limit)
            {
                break;
            }
            sb.append("Bookmark: ");
            sb.append(bookmark.getName());

            c = c + 1;
        }
        return sb.toString();
    }

    private String bookmarksToStringWTags(Set<AbstractBookmark> bookmarks, int limit)
    {
        StringBuilder sb = new StringBuilder();
        int c = 0;

        for (AbstractBookmark bookmark : bookmarks)
        {
            if (limit > -1 && c >= limit)
            {
                break;
            }
            sb.append("Bookmark: \t");
            sb.append(bookmark.getName());
            sb.append("\n");
            sb.append(tagsToString(bookmark.getTags(), 10));

            c = c + 1;
        }
        return sb.toString();
    }

    private String tagsToString(Set<String> tags, int colWidth)
    {
        StringBuilder sb = new StringBuilder();
        int c = 1;
        sb.append("Tags: \t\t");

        for (String tag : tags)
        {
            sb.append(tag);
            sb.append("\t");

            if (colWidth > 0 && c >= colWidth)
            {
                sb.append("\n\t\t\t");
                c = 0;
            }
            c = c + 1;
        }

        sb.append("\n");
        return sb.toString();
    }

    @Override
    public void renameTag(String newTag, String oldTag)
    {
        assert newTag != null && !newTag.isEmpty();
        assert oldTag != null && !oldTag.isEmpty();

        Set<String> theTag = new HashSet<>();
        theTag.add(oldTag);
        Set<UUID> tags = searchAllTagsExact(theTag);

        for (UUID uuid : tags)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            bookmark.getTags().remove(oldTag);
            bookmark.addTag(newTag);
        }

        setDirty();
    }

    @Override
    public void mergeTags(String resultingTag, Set<String> tagsToMerge)
    {
        assert resultingTag != null && !resultingTag.isEmpty();
        assert tagsToMerge != null;

        Set<UUID> tags = searchAllTagsExact(tagsToMerge);

        for (UUID uuid : tags)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            bookmark.getTags().removeAll(tagsToMerge);
            bookmark.addTag(resultingTag);
        }

        setDirty();
    }

    @Override
    public void deleteTags(Set<String> tagsToDelete)
    {
        assert tagsToDelete != null;

        Set<UUID> tags = searchAllTagsExact(tagsToDelete);

        for (UUID uuid : tags)
        {
            AbstractBookmark bookmark = getBookmark(uuid);
            bookmark.getTags().removeAll(tagsToDelete);
        }

        setDirty();
    }

    @Override
    public Set<String> getAllTags()
    {
        return bookmarkTags.getFullTextWords();
    }

    @Override
    public Set<String> getTagsFromBookmarks(Collection<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abstractBookmark : bookmarks)
        {
            res.addAll(abstractBookmark.getTags());
        }

        return res;
    }

    @Override
    public Set<String> getTypes(Set<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abstractBookmark : bookmarks)
        {
            res.add(abstractBookmark.getTypeName());
        }

        return res;
    }

    public Set<String> getTypesClassNames(Set<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abstractBookmark : bookmarks)
        {
            res.add(abstractBookmark.getClass().getCanonicalName());
        }

        return res;
    }

}
