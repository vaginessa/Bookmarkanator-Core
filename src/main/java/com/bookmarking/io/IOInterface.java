package com.bookmarking.io;

import java.text.*;
import java.util.*;
import com.bookmarking.bookmark.*;
import com.bookmarking.search.*;

/**
 * This is the interface that other classes will use to load and save bookmark. It could point to files on the file system as FileIo does,
 * or it could point to a database, web service, ftp server, etc...
 */
public interface IOInterface extends SettingsServiceInterface
{

    void init(String config)
        throws Exception;

    void save()
        throws Exception;

    void save(String config)
        throws Exception;

    void close()
        throws Exception;

    AbstractBookmark getBookmark(String params, Object... obj);

    List<AbstractBookmark> getBookmarkList(String params, Object... obj);

    String getOther(String params, Object... obj);

    List<String> getOtherList(String params, Object... obj);

    // Getting

    AbstractBookmark getBookmark(UUID bookmarkId);

    Set<String> getTags(Collection<UUID> bookmarkIds);

    Set<String> getSearchWords(Collection<UUID> bookmarkIds)
        throws Exception;

    Set<String> getTypeNames(Collection<UUID> bookmarkIds);

    Set<String> getBookmarkNames(Collection<UUID> bookmarkIds);

    Set<String> getClassNames(Collection<UUID> bookmarkIds);

    Set<AbstractBookmark> getWithinDateRange(Date includeIfAfter, Date includeIfBefore);

    Set<AbstractBookmark> getWithinLastAccessedDateRange(Date includeIfAfter, Date includeIfBefore);

    Set<AbstractBookmark> getAllBookmarks();

    Set<String> getAllTags();

    Set<String> getAllSearchWords()
        throws Exception;

    Set<String> getAllTypeNames();

    Set<String> getAllBookmarkNames();

    Set<String> getAllClassNames();

    // Setting/adding

    void addBookmark(AbstractBookmark bookmark)
        throws Exception;

    void addAllBookmarks(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    // Updating

    void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    void updateAll(Collection<AbstractBookmark> bookmarks)
        throws Exception;

    List<AbstractBookmark> renameTag(String originalTagName, String newTagName)throws Exception;

    List<AbstractBookmark> replaceTags(String replacement, Set<String> tagsToReplace)throws Exception;

    // Deleting

    AbstractBookmark deleteBookmark(UUID bookmarkId);

    AbstractBookmark deleteBookmark(AbstractBookmark bookmark);

    Set<AbstractBookmark> deleteTag(String tagToDelete);

    Set<AbstractBookmark> deleteTags(Set<String> tagsToDelete);

    // Searching

    List<AbstractBookmark> applySearchOptions(SearchOptions options)
        throws ParseException;
    List<AbstractBookmark> applySearchOptions(Collection<AbstractBookmark> bookmarks,SearchOptions options)
        throws ParseException;

    public default Set<String> getTags(List<AbstractBookmark> bookmarks)
    {
        Set<String> res = new HashSet<>();

        for(AbstractBookmark abs: bookmarks)
        {
            res.addAll(abs.getTags());
        }
        return res;
    }
}
