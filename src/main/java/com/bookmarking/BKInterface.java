package com.bookmarking;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.bootstrap.*;
import com.bookmarking.io.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;

/**
 * Interface to allow the UI to interact with the core library through a single class.
 *
 * The default interface is "LocalInstance", which was created for local java programs. However many other interfaces
 * could be made. For example, one could write a JavaFX program that uses a rest web interface, which would in turn
 * uses LocalInstance to communicate to the core libraries.
 */
public interface BKInterface
{
    // ---------------------------------
    // Bootstrap methods
    // ---------------------------------

    void init();

    /**
     * Settings init allows the calling program to override or set settings. This is useful if for example one
     * has a specific set of classes they wanted the core libraries to load up.
     * @param settings  The settings to use for initiating the core libraries. Default settings will be added to these.
     */
    void init(Settings settings);

    /**
     * Supply ui interface for the init process to send messages to.
     * @param bootstrapUIInterface  The UI interface for the core classes to use.
     */
    void init(BootstrapUIInterface bootstrapUIInterface);

    /**
     * Does any necessary saving prior to exiting.
     * @throws Exception
     */
    void exit()
        throws Exception;

    BootstrapSettings getSettings();

    /**
     * Sets the settings. Overrides any settings currently present.
     * @param settings  The new settings Object for the core classes to use (and what will be written to disk)
     * @return  Settings object with any modifications that have been made during the setting process. Setting default values for example.
     */
    BootstrapSettings setSettings(BootstrapSettings settings);
    void setBootstrapUIInterface(BootstrapUIInterface bootstrapUIInterface);
    BootstrapUIInterface getBootstrapUIInterface();
    // ---------------------------------
    // IO interface methods
    // ---------------------------------
    /**
     * Gets the current IO interface that was supplied to this implementation.
     * @return
     */
    IOInterface getIOInterface();
    /**
     * Sets the IOUIInterface this implementation will use to notify the front end implementation.
     * @param uiInterface
     */
    void setIOUIInterface(IOUIInterface uiInterface);
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
    Set<Class> getAllBookmarkClasses();

    AbstractBookmark getBookmark(UUID bookmarkId);

    Set<String> getBookmarkNames(Collection<UUID> bookmarkIds);

    Set<Class> getBookmarkClasses(Collection<UUID> bookmarkIds);

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

    default Map<Integer, Set<String>> getRecommendedTags(AbstractBookmark abstractBookmark, int numResults)
    {
        return getIOInterface().getRecommendedTags(abstractBookmark, numResults);
    }

    List<AbstractBookmark> renameTag(String originalTagName, String newTagName)
        throws Exception;

    List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)
        throws Exception;

    Set<AbstractBookmark> deleteTag(String tagToDelete);

    Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete);

    Set<String> extractTypeNames(Collection<UUID> bookmarkIds);

    Set<String> getAllTypeNames();

    // ---------------------------------
    // Actions methods
    // ---------------------------------

    List<Class> getActionsPresent();
}
