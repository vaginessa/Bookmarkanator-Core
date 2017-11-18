package com.bookmarking.io;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.error.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;

/**
 * This is the interface that other classes will use to load, save, and search bookmarks and their data.
 */
public interface IOInterface
{

    String SETTINGS_FILE_CONTEXT = "ioSettings";

    //------------------------------------
    // Init and save
    //------------------------------------

    /**
     * Used to initiate the particular back end. If this initialization process needs to notify the front end the IOUIInterface should be supplied
     * prior to calling this method.
     *
     * @param config Configuration string needed by this implementation.
     * @throws Exception
     */
    void init(String config)
        throws Exception;

    /**
     * Used to save any data to the back end implemented.
     */
    void save()
        throws Exception;

    /**
     * Used to save any data to the back end implemented with configuration parameters.
     *
     * @param config Any configs the implementing class requires for saving.
     */
    void save(String config)
        throws Exception;

    /**
     * Used to close any IO streams that are still open for example
     */
    void close()
        throws Exception;

    //------------------------------------
    // UI
    //------------------------------------

    /**
     * Gets the current IO interface that was supplied to this implementation.
     *
     * @return
     */
    IOUIInterface getUIInterface();

    /**
     * Sets the IOUIInterface this implementation will use to notify the front end implementation.
     *
     * @param uiInterface
     */
    void setUIInterface(IOUIInterface uiInterface);

    //------------------------------------
    // Search
    //------------------------------------

    /**
     * Gets a list of all search words present.
     *
     * @param bookmarkIds The bookmark Id's to extract search words from.
     * @return A set of search words that are present in the collection of supplied bookmarks.
     * @throws Exception
     */
    Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception;

    /**
     * Get all search words present in all bookmarks.
     *
     * @return A set of search words present for all bookmarks.
     * @throws Exception
     */
    Set<String> getAllSearchWords()
        throws Exception;

    /**
     * Returns a list of bookmarks that satisfy the search criteria.
     *
     * @param options Search options used to gather the list of results
     * @return A list of bookmarks that fit within the search options supplied.
     * @throws ParseException
     */
    List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException;

    /**
     * Returns subset of the supplied bookmarks that satisfy the search criteria.
     *
     * @param bookmarks The list of bookmarks to apply the search options on.
     * @param options   Search options used to gather the list of results
     * @return A list of bookmarks that fit within the search options supplied.
     * @throws ParseException
     */
    List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
        throws ParseException;

    //------------------------------------
    // Bookmark
    //------------------------------------

    /**
     * Get all bookmarks
     *
     * @return Returns a set of all bookmarks present
     */
    Set<AbstractBookmark> getAllBookmarks();

    /**
     * Get the user displayable names of all bookmarks
     *
     * @return A collection of all bookmark names
     */
    Collection<String> getAllBookmarkNames();

    /**
     * Extracts a list of bookmark names contained
     * @param bookmarkIds
     * @return
     */
    List<String> getBookmarkNames(Collection<UUID> bookmarkIds);

    /**
     * Get all bookmark classes present
     *
     * @return A set of all classnames present
     */
    Set<Class> getAllBookmarkClasses();

    /**
     * Extracts classes from Bookmarks with Id's in the supplied collection.
     * @param bookmarkIds  A collection of bookmark Id's to return classes for.
     * @return  A set of classes that represent all classes present in the list of bookmarks matched to supplied Id's.
     */
    Set<Class> getBookmarkClasses(Collection<UUID> bookmarkIds);

    /**
     * Obtains a single bookmark by bookmark Id
     * @param bookmarkId  The Id of the bookmark to find
     * @return  The bookmark with this Id or null.
     */
    AbstractBookmark getBookmark(UUID bookmarkId);

    /**
     * Get bookmarks with supplied Id's
     * @param bookmarkIds  The bookmark Id's to obtain.
     * @return  A set of bookmarks found with the supplied Id's
     */
    Set<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds);

    /**
     * Add a new bookmark.
     * @param bookmark  The new bookmark.
     * @throws Exception
     */
    void addBookmark(AbstractBookmark bookmark)
        throws Exception;

    /**
     * Add a collection of bookmarks all at once.
     * @param bookmarks  The collection of bookmarks to add.
     * @throws Exception
     */
    void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    /**
     * Locates the bookmark with the Id of the supplied bookmark and replaces it.
     *
     * Note: the updated bookmark should be a complete representation. Id does not do incremental updates.
     * @param bookmark  The bookmark to update.
     * @throws Exception
     */
    void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    /**
     * Update a collection of bookmarks
     * @param bookmarks  The collection of bookmarks to update.
     * @throws Exception
     */
    void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    /**
     * Remove the bookmark with the specified Id.
     * @param bookmarkId  The Id of the bookmark to delete.
     * @return  The removed bookmark.
     */
    AbstractBookmark deleteBookmark(UUID bookmarkId);

    /**
     * Delete bookmark.
     * @param bookmark  The bookmark to delete
     * @return  The bookmark that was deleted (returned to allow for changes during deletion)
     */
    AbstractBookmark deleteBookmark(AbstractBookmark bookmark);

    //------------------------------------
    // Tags
    //------------------------------------

    /**
     * Perform a brute force search (tag.contains(text) or text.contains(tag)) for the string withing tags.
     * @param searchTerm  the search term
     * @return  All tags found
     */
    default Set<String> searchTags(String searchTerm)
    {
        Set<String> tags = getAllTags();
        if (searchTerm == null || searchTerm.trim().isEmpty())
        {
            return tags;
        }

        searchTerm = searchTerm.toLowerCase().trim();

        Set<String> res = new HashSet<>();

        for (String tag : tags)
        {
            String s = tag.toLowerCase();
            if (s.contains(searchTerm) || searchTerm.contains(s))
            {
                res.add(tag);
            }
        }

        return res;
    }

    /**
     * Default method to extract recommended tags for a specific bookmark. This method simply uses .contains for each tag so it
     * is not the fastest way of accomplishing this goal.
     * @param abstractBookmark  the bookmark to get recomended tags for.
     * @param numResults  How many recommended tags to try and get. Will truncate list to this number if to large.
     * @return A map of recommended tags. Map<Level int, Set of tags> the Integer is the recommendation level of the associated list of
     * tags. Zero is top recommendations, getting less recommended the higher the number gets.
     */
    default Map<Integer, Set<String>> getRecommendedTags(AbstractBookmark abstractBookmark, int numResults)
    {
        if (abstractBookmark == null || abstractBookmark.getTags().isEmpty())
        {
            return new HashMap<>();
        }
        Map<Integer, Set<String>> intTagMap = new HashMap<>();

        try
        {
            SearchOptions searchOptions = new SearchOptions();
            searchOptions.add(Operation.TagOptions.ALL_TAGS, abstractBookmark.getTags());
            List<AbstractBookmark> bks = applySearchOptions(searchOptions);

            Set<String> tags = extractTags(bks);
            tags.removeAll(abstractBookmark.getTags());

            Set<String> tmp = intTagMap.get(0);

            if (tmp==null)
            {
                tmp = new HashSet<>();
                intTagMap.put(0, tmp);
            }

            for (String tag: tags)
            {
                tmp.add(tag);
                if (tmp.size()>=numResults)
                {
                    break;
                }
            }

            if (tmp.size()<numResults)
            {
                Set<String> tmp2 = intTagMap.get(1);

                if (tmp2==null)
                {
                    tmp2 = new HashSet<>();
                    intTagMap.put(1, tmp2);
                }

                searchOptions = new SearchOptions();
                searchOptions.add(Operation.TagOptions.ANY_TAG, abstractBookmark.getTags());
                bks = applySearchOptions(searchOptions);

                tags = extractTags(bks);
                tags.removeAll(abstractBookmark.getTags());
                tags.removeAll(intTagMap.get(0));

                for (String tag: tags)
                {
                    tmp2.add(tag);
                    if (tmp2.size()+tmp.size()>=numResults)
                    {
                        break;
                    }
                }
            }
        }
        catch (ParseException e)
        {
            ErrorHandler.handle(e);
        }

        return intTagMap;
    }

    /**
     * A convenience method to extract tags from a set of bookmarks supplied.
     * @param bookmarks  The source of the tags.
     * @return  A set of all tags contained in the supplied bookmarks.
     */
    default Set<String> extractTags(Collection<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abs : bookmarks)
        {
            res.addAll(abs.getTags());
        }
        return res;
    }

    /**
     * Default method to extract tags from a list of bookmarks. Uses brute force to extract tags. If large lists are going to be used a more elegant
     * solution should be implemented.
     *
     * @param bookmarks  The list of bookmarks to extract tags from.
     * @return  A set of tags contained in all the supplied bookmarks.
     */
    default Set<String> extractTags(List<AbstractBookmark> bookmarks)
    {
        Objects.requireNonNull(bookmarks);
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abs : bookmarks)
        {
            res.addAll(abs.getTags());
        }
        return res;
    }

    /**
     * Gets all tags from all bookmarks present.
     * @return  A set of every tag present in all the bookmarks in the system.
     */
    Set<String> getAllTags();

    SortedMap<String, Set<String>> getAllTagsAlphabetical(Set<String> tagsToRemove);

    SortedMap<String, Set<String>> orderTagsAlphabetical(Set<String> tags);

    /**
     * Renames a tag in the entire system.
     * @param originalTagName  The tag to rename
     * @param newTagName  The new tag name
     * @return  A list of bookmarks affected.
     * @throws Exception
     */
    List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
        throws Exception;

    /**
     * Replaces several tags with a single tag everywhere in the system.
     * @param replacement  the replacing tag.
     * @param tagsToReplace  the set of tags to replace with the single tag.
     * @return  A list of bookmarks affected
     * @throws Exception
     */
    List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
        throws Exception;

    /**
     * Deletes a tag everywhere
     * @param tagToDelete  The tag name to delete.
     * @return  A list of bookmarks affected
     */
    Set<AbstractBookmark> deleteTag(String tagToDelete);

    /**
     * Deletes the set of tags everywhere.
     * @param tagsToDelete  The set of tags to remove from all bookmarks.
     * @return  A list of bookmarks affected
     */
    Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete);

    //------------------------------------
    // Bookmark Types
    //------------------------------------

    /**
     * Returns a set of bookmark types present in the collection of bookmark Id's
     * @param bookmarkIds  The collection of Id's to extract types for.
     * @return  A set of types (such as text, web, etc...)
     */
    Set<String> extractTypeNames(Collection<UUID> bookmarkIds);

    /**
     * @return  A set of all types present in this IOInterface
     */
    Set<String> getAllTypeNames();

    //------------------------------------
    // Settings Types
    //------------------------------------

    /**
     * Obtains the settings present in this interface.
     * @return  Settings stored in this interface
     */
    Settings getSettings();

    /**
     * Set the settings for this interface.
     * @param settings  The new settings.
     */
    void setSettings(Settings settings);

    //------------------------------------
    // Undo/Redo
    //------------------------------------

    void undo() throws Exception;
    void redo() throws Exception;

    /**
     * Start over with the undo/redo stack.
     */
    void clearUndoRedoStack() throws Exception;
}
