package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;
import com.bookmarkanator.util.*;

/**
 * The Context is a bookmark specific context. It allows searching and sorting of bookmarks. It allows reading and writing of data for the bookmarks.
 * and it also allows the bookmark methods to be called that will prepare the data they will use. All reading and writing is done in com.bookmarkanator.xml by default.
 */
public class FileContext implements ContextInterface
{
    private Map<UUID, AbstractBookmark> bookmarks;
    //Note: the two maps below are for checking if a bookmark being deleted or edited has other bookmarks that depend on it.
    private Map<UUID, Set<UUID>> bkDependsOnMap;//<bookmark id, set<bk id's that this bk depends on>>
    private Map<UUID, Set<UUID>> whatDependsOnBKMap;//<bookmark id, set<bk id's that depend on this bk>>
    private Search<UUID> bookmarkNames;
    private Search<UUID> bookmarkTypeNames;//Such as text, web, terminal, mapping, whatever...
    private Search<UUID> bookmarkText;//The text the bookmark contains.
    private Search<UUID> bookmarkTags;
    private int numSearchResults;
    private BKIOInterface bkioInterface;

    //Bookmarks can store data here and communicate with other bookmarks. They can read/write their own data, but only read the data of other bookmark types.
    private Map<String, Map<String, Object>> contextObject;//<Class name of bookmark, Map<Bookmark data key, bookmark data object>>

    public FileContext()
    {
        bookmarks = new HashMap<>();
        bookmarkNames = new Search<>();
        bookmarkTypeNames = new Search<>();
        bookmarkText = new Search<>();
        bookmarkTags = new Search<>();
        contextObject = new HashMap<>();
        bkDependsOnMap = new HashMap<>();
        whatDependsOnBKMap = new HashMap<>();
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

    /**
     * Gets a set of bookmark Id's of bookmarks that depend on this bookmark in some way. Basically asking the question what do I depend on?
     * *Note: Individual bookmarks are responsible for setting their dependents.
     *
     * @param bookmarkId The bookmark to check for dependents.
     * @return A set of dependent bookmark Id's
     */
    @Override
    public Set<UUID> getDependents(UUID bookmarkId)
    {
        return bkDependsOnMap.get(bookmarkId);
    }

    /**
     * Gets a set of bookmark Id's that this bookmark depends on. Basically asking the question what depends on me?
     * *Note: Individual bookmarks are responsible for setting thei dependents. When dependents are set this depends on list is automatically updated.
     *
     * @param bookmarkId
     * @return
     */
    @Override
    public Set<UUID> getDependsOn(UUID bookmarkId)
    {
        return whatDependsOnBKMap.get(bookmarkId);
    }

    /**
     * Indicates one bookmark (theBookmark) depends on another bookmark (dependingBookmark)
     *
     * @param theBookmark       The bookmark that will depend on another bookmark
     * @param dependingBookmark The bookmark that is being depended upon.
     * @return The number of bookmarks 'theBookmark' depends upon.
     */
    @Override
    public int addDependency(UUID theBookmark, UUID dependingBookmark)
    {
        Set<UUID> dependsOnSet = bkDependsOnMap.get(theBookmark);

        if (dependsOnSet == null)
        {
            dependsOnSet = new HashSet<>();
        }

        dependsOnSet.add(dependingBookmark);//adding a bookmark Id that this bookmark depends on.

        Set<UUID> dependsOnMeSet = whatDependsOnBKMap.get(dependingBookmark);

        if (dependsOnMeSet == null)
        {
            dependsOnMeSet = new HashSet<>();
        }

        dependsOnMeSet.add(theBookmark);//adding this bookmark to the list of bookmarks that the depnd on the depending bookmark.

        return dependsOnSet.size();
    }

    /**
     * Removes the indicator that one bookmark (theBookmark) depends on another bookmark (dependingBookmark)
     *
     * @param theBookmark       The bookmark that will stop depending on 'dependingBookmark'
     * @param dependingBookmark The bookmark that will no longer link to 'theBookmark'
     * @return The number of bookmarks that 'theBookmark' currently depends on.
     */
    @Override
    public int removeDependency(UUID theBookmark, UUID dependingBookmark)
    {
        Set<UUID> dependsOnSet = bkDependsOnMap.get(theBookmark);

        if (dependsOnSet != null)
        {
            dependsOnSet.remove(dependingBookmark);//'theBookmark' no longer depends on 'dependingBookmark'
        }

        Set<UUID> dependsOnMeSet = whatDependsOnBKMap.get(dependingBookmark);

        if (dependsOnMeSet != null)
        {
            dependsOnMeSet.remove(theBookmark);//'theBookmark' is no longer listed as something that is depending on 'dependingBookmark'
        }

        return dependsOnSet == null ? 0 : dependsOnSet.size();
    }

    /**
     * Gets all bookmark UUID's
     *
     * @return A set of bookmark Id's
     */
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

        UUID id = bookmark.getId();

        bookmarkNames.add(id, bookmark.getName());
        bookmarkTypeNames.add(id, bookmark.getTypeName());
        bookmarkText.add(id, bookmark.getText());
        bookmarkTags.add(id, bookmark.getTags());

        bookmarks.put(bookmark.getId(), bookmark);
    }

    /**
     * Remove a bookmark. This removes the bookmark from the list of bookmarks and all searching methods.
     *
     * @param bookmarkID The Id of the bookmark to remove.
     * @return The removed bookmark.
     */
    @Override
    public AbstractBookmark removeBookmark(UUID bookmarkID)
    {
        AbstractBookmark abs = getBookmark(bookmarkID);

        if (abs != null)
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
        if (bookmark.getId() == null)
        {
            throw new Exception("Bookmark ID must be set");
        }

        if (bookmarks.get(bookmark) == null)
        {
            throw new Exception("Item \"" + bookmark.getId().toString() + "\" not found.");
        }

        removeBookmark(bookmark.getId());

        addBookmark(bookmark);
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

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches the full text returned by a bookmark.
            uuids.addAll(bookmarkText.searchFullTextOnly(text));
        }

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

        if (uuids.size() < getNumSearchResults())
        {
            //If text matches a substring of one of the bookmark text words rotated.
            uuids.addAll(bookmarkText.searchSubstringsOfRotatedWordsOnly(text));
        }

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

        if (uuids.size() < getNumSearchResults())
        {
            //General search because nothing has enough results yet.
            uuids.addAll(bookmarkText.searchAll(text, getNumSearchResults()));
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
        return getBookmarks(bookmarkText.searchAll(text, getNumSearchResults()));
    }

    // ============================================================
    // Sorting and Filtering Methods
    // ============================================================

    @Override
    public List<AbstractBookmark> filterHasAnyTag(List<AbstractBookmark> bookmarkList, Set<String> tags)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : bookmarkList)
        {
            for (String tag : tags)
            {
                if (bk.getTags().contains(tag))
                {
                    results.add(bk);
                    break;
                }
            }
        }
        return results;
    }

    @Override
    public List<AbstractBookmark> filterHasAllTags(List<AbstractBookmark> bookmarkList, Set<String> tags)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : bookmarkList)
        {
            if (bk.getTags().containsAll(tags))
            {
                results.add(bk);
            }
        }
        return results;
    }

    @Override
    public List<AbstractBookmark> filterByBookmarkType(List<AbstractBookmark> bookmarkList, Set<String> bookmarkTypeNames)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : bookmarkList)
        {
            for (String s : bookmarkTypeNames)
            {
                if (bk.getTypeName().equals(s))
                {
                    results.add(bk);
                    break;
                }
            }
        }

        return results;
    }

    /**
     * Filters a list of bookmarks by date range.
     *
     * @param bookmarkList    The list to filter
     * @param includeIfAfter  Include the bookmark if the date is equal to or after this date.
     * @param includeIfBefore Include the bookmark if the date is before this date only.
     * @return A list of date range fildtered bookmarks.
     */
    @Override
    public List<AbstractBookmark> filterDate(List<AbstractBookmark> bookmarkList, Date includeIfAfter, Date includeIfBefore)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        for (AbstractBookmark bk : bookmarkList)
        {
            if (bk.getCreationDate().compareTo(includeIfAfter) > -1 && bk.getCreationDate().compareTo(includeIfBefore) < 0)
            {
                results.add(bk);
            }
        }

        return results;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark names.
     *
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    @Override
    public List<AbstractBookmark> excludeBookmarksWithNamesContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        continueA:
        for (AbstractBookmark bk : bookmarkList)
        {
            for (String s : exclusions)
            {
                if (bk.getName().contains(s))
                {
                    continue continueA;
                }
            }
            results.add(bk);
        }

        return results;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark text.
     *
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    @Override
    public List<AbstractBookmark> excludeBookmarksContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        continueA:
        for (AbstractBookmark bk : bookmarkList)
        {
            for (String s : exclusions)
            {
                if (bk.getText().contains(s))
                {
                    continue continueA;
                }
            }
            results.add(bk);
        }

        return results;
    }

    /**
     * Excludes bookmarks that have any of the specified exclusion strings in the bookmark tags.
     *
     * @param bookmarkList
     * @param exclusions
     * @return
     */
    @Override
    public List<AbstractBookmark> excludeBookmarkTagsContainingText(List<AbstractBookmark> bookmarkList, Set<String> exclusions)
    {
        List<AbstractBookmark> results = new ArrayList<>();

        continueA:
        for (AbstractBookmark bk : bookmarkList)
        {
            for (String s : exclusions)
            {
                for (String st: bk.getTags())
                {
                    if (st.contains(s))
                    {
                        continue continueA;
                    }
                }
            }
            results.add(bk);
        }

        return results;
    }

    //TODO Add sort by functions below.
}
