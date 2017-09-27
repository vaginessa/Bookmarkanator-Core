package com.bookmarking.io;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.search.*;
import com.bookmarking.settings.*;
import com.bookmarking.ui.*;

/**
 * This is the interface that other classes will use to load, save, and search bookmarks and their data.
 */
public interface IOInterface extends SettingsServiceInterface
{

    //------------------------------------
    // Init and save methods
    //------------------------------------

    void init(String config)
        throws Exception;

    void save()
        throws Exception;

    void save(String config)
        throws Exception;

    void close()
        throws Exception;

    //------------------------------------
    // Other methods
    //------------------------------------

    IOUIInterface getUIInterface();
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

    Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception;

    Set<String> getAllSearchWords()throws Exception;

    List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException;
    List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks,SearchOptions options)
        throws ParseException;

    //------------------------------------
    // Bookmark methods
    //------------------------------------

    Set<AbstractBookmark> getAllBookmarks();
    Set<String> getAllBookmarkNames();
    Set<String> getAllBookmarkClassNames();

    AbstractBookmark getBookmark(UUID bookmarkId);

    List<AbstractBookmark> getBookmarkList(String params, Object... obj);

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

        for(AbstractBookmark abs: bookmarks)
        {
            res.addAll(abs.getTags());
        }
        return res;
    }

    Set<String> getAllTags();

    List<AbstractBookmark> renameTag(String originalTagName, String newTagName)throws Exception;

    List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)throws Exception;

    Set<AbstractBookmark> deleteTag(String tagToDelete);

    Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete);

    //------------------------------------
    // Bookmark Type methods
    //------------------------------------

    Set<String> extractTypeNames(Collection<UUID> bookmarkIds);

    Set<String> getAllTypeNames();


    // Not sure if we want to keep these:

    String getOther(String params, Object... obj);

    List<String> getOtherList(String params, Object... obj);
    AbstractBookmark getBookmark(String params, Object... obj);
}
