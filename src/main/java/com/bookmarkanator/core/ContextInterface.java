package com.bookmarkanator.core;

import java.util.*;
import com.bookmarkanator.bookmarks.*;
import com.bookmarkanator.io.*;

/**
 * The interface that other classes will access to perform operations (searching, sorting, adding, removing) of bookmarks.
 */
public interface ContextInterface
{
    void setBKIOInterface(BKIOInterface bkioInterface);

    BKIOInterface getBKIOInterface();

    Set<UUID> getDependents(UUID bookmarkId);

    Set<UUID> getDependsOn(UUID bookmarkId);

    int addDependency(UUID theBookmark, UUID dependingBookmark);

    int removeDependency(UUID theBookmark, UUID dependingBookmark);

    Set<UUID> getBookmarkIDs();

    Set<AbstractBookmark> getBookmarks();

    AbstractBookmark getBookmark(UUID uuid);

    List<AbstractBookmark> getBookmarks(Set<UUID> bookmarkIds);

    void addAll(List<AbstractBookmark> bookmarks)
        throws Exception;

    void addBookmark(AbstractBookmark bookmark)
            throws Exception;

    AbstractBookmark removeBookmark(UUID bookmarkID);

    void updateBookmark(AbstractBookmark bookmark)
        throws Exception;

    int getNumSearchResults();

    void setNumSearchResults(int numSearchResults);

    List<AbstractBookmark> searchAll(String text);

    List<AbstractBookmark> searchBookmarkNames(String text);

    List<AbstractBookmark> searchTagsLoosly(String text);

    List<AbstractBookmark> searchTagsExact(Set<String> tags);

    List<AbstractBookmark> searchTagsFullMatch(Set<String> tags)
        throws Exception;

    List<AbstractBookmark> searchBookmarkTypes(String text);

    List<AbstractBookmark> searchBookmarkText(String text);

}
