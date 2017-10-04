package com.bookmarking.structure;

import java.text.*;
import java.util.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;

/**
 * This is the interface that other classes will use to load, save, and search bookmarks and their data.
 */
public interface IOInterface
{

    //------------------------------------
    // Init and save methods
    //------------------------------------

    /**
     * Used to initiate the particular back end. If this initialization process needs to notify the front end the IOUIInterface should be supplied
     * prior to calling this method.
     * @param config  Configuration string needed by this implementation.
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
     * @param config  Any configs the implementing class requires for saving.
     */
    void save(String config)
        throws Exception;

    /**
     * Used to close any IO streams that are still open for example
     */
    void close()
        throws Exception;

    //------------------------------------
    // Other methods
    //------------------------------------

    /**
     * Gets the current IO interface that was supplied to this implementation.
     * @return
     */
    IOUIInterface getUIInterface();

    /**
     * Sets the IOUIInterface this implementation will use to notify the front end implementation.
     * @param uiInterface
     */
    void setUIInterface(IOUIInterface uiInterface);

    /**
     * The settings this class needs to properly initiate.
     *
     * It will return a set of AbstractSetting's objects with the group and key filled in. The calling class can then locate the value that matches
     * the supplied setting and pass it into the use method.
     *
     * @return Set of AbstractSetting objects with their group name(s), and key(s) populated.
     */
    Set<AbstractSetting> getSettingsKeys();

    /**
     * Gets a list of all search words present.
     * @param bookmarkIds  The bookmark Id's to extract search words from.
     * @return  A set of search words that are present in the collection of supplied bookmarks.
     * @throws Exception
     */
    Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception;

    /**
     * Get all search words present in all bookmarks.
     * @return  A set of search words present for all bookmarks.
     * @throws Exception
     */
    Set<String> getAllSearchWords()
        throws Exception;

    /**
     * Returns a list of bookmarks that satisfy the search criteria.
     * @param options  Search options used to gather the list of results
     * @return  A list of bookmarks that fit within the search options supplied.
     * @throws ParseException
     */
    List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException;

    /**
     * Returns subset of the supplied bookmarks that satisfy the search criteria.
     * @param bookmarks  The list of bookmarks to apply the search options on.
     * @param options  Search options used to gather the list of results
     * @return  A list of bookmarks that fit within the search options supplied.
     * @throws ParseException
     */
    List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks, SearchOptions options)
        throws ParseException;

    //------------------------------------
    // Bookmark methods
    //------------------------------------

    /**
     * Get all bookmarks
     * @return  Returns a set of all bookmarks present
     */
    Set<AbstractBookmark> getAllBookmarks();

    /**
     * Get the user displayable names of all bookmarks
     * @return  A collection of all bookmark names
     */
    Collection<String> getAllBookmarkNames();

    /**
     * Get all bookmark class names present
     * @return  A set of all classnames present
     */
    Set<String> getAllBookmarkClassNames();

    AbstractBookmark getBookmark(UUID bookmarkId);

    List<AbstractBookmark> getBookmarkList(SearchOptions searchOptions);

    Set<String> getBookmarkNames(Collection<UUID> bookmarkIds);

    Set<String> getClassNames(Collection<UUID> bookmarkIds);

    void addBookmark(AbstractBookmark bookmark)
        throws Exception;

    void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    AbstractBookmark deleteBookmark(UUID bookmarkId);

    AbstractBookmark deleteBookmark(AbstractBookmark bookmark);

    //------------------------------------
    // Tag methods
    //------------------------------------

    Set<String> extractTags(Collection<UUID> bookmarkIds);

    default Set<String> extractTags(List<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for (AbstractBookmark abs : bookmarks)
        {
            res.addAll(abs.getTags());
        }
        return res;
    }

    Set<String> getAllTags();

    List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
        throws Exception;

    List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
        throws Exception;

    Set<AbstractBookmark> deleteTag(String tagToDelete);

    Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete);

    //------------------------------------
    // Bookmark Type methods
    //------------------------------------

    Set<String> extractTypeNames(Collection<UUID> bookmarkIds);

    Set<String> getAllTypeNames();

    //------------------------------------
    // Settings Type methods
    //------------------------------------

    Settings getSettings();

    void setSettings(Settings settings);

}
